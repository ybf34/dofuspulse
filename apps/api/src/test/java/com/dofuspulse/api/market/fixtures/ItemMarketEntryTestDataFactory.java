package com.dofuspulse.api.market.fixtures;

import com.dofuspulse.api.metrics.fixtures.builders.ItemPriceBuilder;
import com.dofuspulse.api.model.ItemMarketEntry;
import com.dofuspulse.api.projections.ItemPrice;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ItemMarketEntryTestDataFactory {

  public static ItemMarketEntry createMockItemMarketEntry(
      Long itemId,
      LocalDate date,
      Integer price,
      String effect) {

    ItemMarketEntry mockItemMarketEntry = new ItemMarketEntry();
    mockItemMarketEntry.setId(UUID.randomUUID());
    mockItemMarketEntry.setEntryDate(date);
    mockItemMarketEntry.setItemId(itemId);
    mockItemMarketEntry.setPrices(List.of(price));
    mockItemMarketEntry.setEffects(Map.of("1", effect));

    return mockItemMarketEntry;
  }

  public static List<ItemMarketEntry> createMockItemMarketListing(
      Long itemId,
      Integer price,
      String effect,
      LocalDate startDate,
      LocalDate endDate) {
    List<ItemMarketEntry> itemMarketListing = new ArrayList<>();

    while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
      itemMarketListing.add(
          createMockItemMarketEntry(itemId, startDate, price, effect));
      startDate = startDate.plusDays(1);
    }
    return itemMarketListing;
  }

  public static List<ItemPrice> mockItemPriceHistory(
      Long itemId,
      Integer price,
      LocalDate startDate,
      LocalDate endDate) {
    return ItemMarketEntryTestDataFactory.createMockItemMarketListing(itemId, price, "1", startDate,
            endDate)
        .stream()
        .map(snapshot -> ItemPriceBuilder.builder()
            .withItemId(snapshot.getItemId())
            .withPrices(snapshot.getPrices().getFirst())
            .withDate(snapshot.getEntryDate())
            .build())
        .toList();
  }

  public record TestItemPriceDto(Long itemId, LocalDate snapshotDate, List<Integer> prices) {}
}
