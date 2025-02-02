package com.dofuspulse.api.metrics.calculator;

import com.dofuspulse.api.metrics.MetricType;
import com.dofuspulse.api.metrics.calculator.params.DailySalesParam;
import com.dofuspulse.api.metrics.calculator.utils.PriceUtil;
import com.dofuspulse.api.model.ItemSalesSnapshot;
import com.dofuspulse.api.projections.DailySales;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class DailySalesCalculator implements MetricCalculator<DailySalesParam, List<DailySales>> {

  @Override
  public MetricType getType() {
    return MetricType.DAILY_SALES;
  }

  @Override
  public Class<DailySalesParam> getInputType() {
    return DailySalesParam.class;
  }

  @Override
  public List<DailySales> calculate(DailySalesParam data) {

    Map<ListingKey, LocalDate> activeListings = new HashMap<>();
    Map<LocalDate, DailySales> dailyResults = new TreeMap<>();

    Map<LocalDate, List<ItemSalesSnapshot>> snapshotsByDate = data.items().stream()
        .sorted(Comparator.comparing(ItemSalesSnapshot::getSnapshotDate))
        .collect(Collectors.groupingBy(ItemSalesSnapshot::getSnapshotDate));

    for (LocalDate date : snapshotsByDate.keySet().stream().sorted().toList()) {
      Set<ListingKey> currentDayListings = new HashSet<>();
      int dailyListings = 0;

      for (ItemSalesSnapshot snapshot : snapshotsByDate.get(date)) {
        int effectsHash = snapshot.getEffects().hashCode();

        ListingKey key = new ListingKey(
            snapshot.getItemId(),
            snapshot.getPrices(),
            effectsHash
        );
        currentDayListings.add(key);
        dailyListings++;
      }

      int soldCount = 0;
      int expiredCount = 0;
      AtomicInteger addedCount = new AtomicInteger();
      int totalSoldDuration = 0;
      int revenue = 0;

      Iterator<Entry<ListingKey, LocalDate>> it = activeListings.entrySet().iterator();
      while (it.hasNext()) {
        Map.Entry<ListingKey, LocalDate> entry = it.next();
        ListingKey key = entry.getKey();
        LocalDate addedDate = entry.getValue();

        if (!currentDayListings.contains(key)) {
          // Item was sold
          if (date.isBefore(addedDate.plusDays(28))) {
            soldCount++;
            revenue += PriceUtil.getMinimumUnitPrice(key.prices());
            int duration = (int) ChronoUnit.DAYS.between(addedDate, date);
            totalSoldDuration += duration;
            it.remove();
          } else if (date.isAfter(addedDate.plusDays(28))) {
            expiredCount++;
            it.remove();
          }
        }
      }

      currentDayListings.forEach(key -> {
        if (!activeListings.containsKey(key)) {
          addedCount.getAndIncrement();
          activeListings.put(key, date);
        }
      });

      double avgSoldDuration = soldCount > 0 ? (double) totalSoldDuration / soldCount : 0.0;

      dailyResults.put(date, new DailySales(
          date,
          soldCount,
          addedCount.get(),
          expiredCount,
          avgSoldDuration,
          dailyListings,
          revenue
      ));
    }

    return new ArrayList<>(dailyResults.values());
  }

  record ListingKey(long itemId, List<Integer> prices, int effectsHash) {}


}
