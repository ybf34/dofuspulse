package com.dofuspulse.api.metrics.calculator;

import com.dofuspulse.api.metrics.MetricType;
import com.dofuspulse.api.metrics.calculator.params.DailySalesParam;
import com.dofuspulse.api.metrics.calculator.utils.PriceUtil;
import com.dofuspulse.api.projections.DailySales;
import com.dofuspulse.api.projections.ItemMarketEntryProjection;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
    List<ItemMarketEntryProjection> marketEntries = data.itemMarketEntries();

    if (marketEntries.isEmpty()) {
      return List.of();
    }

    List<DailySales> itemDailySalesByDate = new ArrayList<>();
    Map<MarketEntryKey, LocalDate> activeListings = new HashMap<>();

    LocalDate currentDay = marketEntries.getFirst().getEntryDate();
    List<ItemMarketEntryProjection> currentDayEntries = new ArrayList<>();

    for (ItemMarketEntryProjection entry : marketEntries) {
      LocalDate entryDate = entry.getEntryDate();

      if (!entryDate.equals(currentDay)) {
        processDay(currentDay, currentDayEntries, activeListings, itemDailySalesByDate);
        currentDayEntries.clear();
        currentDay = entryDate;
      }
      currentDayEntries.add(entry);
    }

    // Process remaining entries for the last day
    processDay(currentDay, currentDayEntries, activeListings, itemDailySalesByDate);
    return itemDailySalesByDate;
  }

  private void processDay(
      LocalDate date,
      List<ItemMarketEntryProjection> entries,
      Map<MarketEntryKey, LocalDate> activeListings,
      List<DailySales> dailySalesByDate) {

    Set<MarketEntryKey> currentDayListings = new HashSet<>();

    for (ItemMarketEntryProjection snapshot : entries) {
      int effectsHash = snapshot.getEffects().hashCode();
      MarketEntryKey itemMarketEntryKey = new MarketEntryKey(
          snapshot.getItemId(),
          snapshot.getPrices(),
          effectsHash);
      currentDayListings.add(itemMarketEntryKey);
    }

    int soldCount = 0;
    int expiredCount = 0;
    int addedCount = 0;
    int totalSoldDuration = 0;
    int revenue = 0;

    Iterator<Map.Entry<MarketEntryKey, LocalDate>> activeListingIterator =
        activeListings.entrySet().iterator();

    while (activeListingIterator.hasNext()) {
      Map.Entry<MarketEntryKey, LocalDate> entry = activeListingIterator.next();
      MarketEntryKey key = entry.getKey();
      LocalDate addedDate = entry.getValue();

      if (!currentDayListings.contains(key)) {
        LocalDate expirationDate = addedDate.plusDays(LISTING_EXPIRATION_DAYS);
        if (date.isBefore(expirationDate) || date.isEqual(expirationDate)) {
          soldCount++;
          revenue += PriceUtil.getMinimumUnitPrice(key.prices());
          int duration = (int) ChronoUnit.DAYS.between(addedDate, date);
          totalSoldDuration += duration;
        } else if (date.isAfter(expirationDate)) {
          expiredCount++;
        }
        activeListingIterator.remove();
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
            currentDayListings.size(),
            revenue));
  }

  record MarketEntryKey(long itemId, List<Integer> prices, int effectsHash) {}
}