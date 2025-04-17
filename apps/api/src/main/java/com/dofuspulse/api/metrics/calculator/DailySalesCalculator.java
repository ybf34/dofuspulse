package com.dofuspulse.api.metrics.calculator;

import com.dofuspulse.api.metrics.MetricType;
import com.dofuspulse.api.metrics.calculator.params.DailySalesParam;
import com.dofuspulse.api.metrics.calculator.utils.PriceUtil;
import com.dofuspulse.api.model.ItemMarketEntry;
import com.dofuspulse.api.projections.DailySales;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class DailySalesCalculator implements MetricCalculator<DailySalesParam, List<DailySales>> {

  private static final int LISTING_EXPIRATION_DAYS = 28;

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

    if (data.itemMarketEntries() == null || data.itemMarketEntries().isEmpty()) {
      return List.of();
    }

    List<DailySales> dailySalesByDate = new ArrayList<>();
    Map<MarketEntryKey, LocalDate> activeListings = new HashMap<>();

    Map<LocalDate, List<ItemMarketEntry>> itemsMarketEntriesPerDate = data.itemMarketEntries()
        .stream()
        .sorted(Comparator.comparing(ItemMarketEntry::getEntryDate))
        .collect(Collectors.groupingBy(ItemMarketEntry::getEntryDate));

    itemsMarketEntriesPerDate.entrySet()
        .stream()
        .sorted(Entry.comparingByKey())
        .forEach(dailySales -> {

          var date = dailySales.getKey();
          Set<MarketEntryKey> currentDayListings = new HashSet<>();
          int listingCount = 0;

          for (ItemMarketEntry snapshot : dailySales.getValue()) {
            int effectsHash = snapshot.getEffects().hashCode();

            MarketEntryKey itemMarketEntryKey = new MarketEntryKey(
                snapshot.getItemId(),
                snapshot.getPrices(),
                effectsHash);

            currentDayListings.add(itemMarketEntryKey);
            listingCount++;
          }

          int soldCount = 0;
          int expiredCount = 0;
          int addedCount = 0;
          int totalSoldDuration = 0;
          int revenue = 0;

          var activeListingsIterator = activeListings.entrySet()
              .iterator();
          while (activeListingsIterator.hasNext()) {
            Map.Entry<MarketEntryKey, LocalDate> entry = activeListingsIterator.next();
            MarketEntryKey key = entry.getKey();
            LocalDate addedDate = entry.getValue();

            if (!currentDayListings.contains(key)) {
              // Item was sold
              if (date.isBefore(addedDate.plusDays(LISTING_EXPIRATION_DAYS))) {
                soldCount++;
                revenue += PriceUtil.getMinimumUnitPrice(key.prices());
                int duration = (int) ChronoUnit.DAYS.between(addedDate, date);
                totalSoldDuration += duration;
                activeListingsIterator.remove();
              } else if (date.isAfter(addedDate.plusDays(LISTING_EXPIRATION_DAYS))) {
                expiredCount++;
                activeListingsIterator.remove();
              }
            }
          }

          for (MarketEntryKey listing : currentDayListings) {
            if (!activeListings.containsKey(listing)) {
              addedCount++;
              activeListings.put(listing, date);
            }
          }

          double avgSoldDuration = soldCount > 0 ? (double) totalSoldDuration / soldCount : 0.0;

          dailySalesByDate.add(
              new DailySales(
                  date,
                  soldCount,
                  addedCount,
                  expiredCount,
                  avgSoldDuration,
                  listingCount,
                  revenue));
        });

    return dailySalesByDate;
  }

  record MarketEntryKey(long itemId, List<Integer> prices, int effectsHash) {}

}
