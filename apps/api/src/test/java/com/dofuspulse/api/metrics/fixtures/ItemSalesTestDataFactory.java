package com.dofuspulse.api.metrics.fixtures;

import com.dofuspulse.api.metrics.fixtures.builders.ItemPriceBuilder;
import com.dofuspulse.api.model.ItemSalesSnapshot;
import com.dofuspulse.api.projections.ItemPrice;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemSalesTestDataFactory {

  public static ItemSalesSnapshot createMockItemSalesSnapshot(
      Long itemId,
      LocalDate date,
      Integer price,
      String effect) {

    ItemSalesSnapshot mockItemSalesSnapshot = new ItemSalesSnapshot();
    mockItemSalesSnapshot.setSnapshotDate(date);
    mockItemSalesSnapshot.setItemId(itemId);
    mockItemSalesSnapshot.setPrices(List.of(price));
    mockItemSalesSnapshot.setEffects(Map.of("1", effect));

    return mockItemSalesSnapshot;
  }

  public static List<ItemSalesSnapshot> createMockItemMarketListing(
      Long itemId,
      Integer price,
      String effect,
      LocalDate startDate,
      LocalDate endDate) {
    List<ItemSalesSnapshot> itemMarketListing = new ArrayList<>();

    while (startDate.isBefore(endDate) || startDate.isEqual(endDate)) {
      itemMarketListing.add(
          createMockItemSalesSnapshot(itemId, startDate, price, effect));
      startDate = startDate.plusDays(1);
    }
    return itemMarketListing;
  }

  public static List<ItemPrice> mockIngredientsPrices(
      List<Long> ingredientIds,
      LocalDate startDate,
      LocalDate endDate) {
    return ingredientIds.stream()
        .flatMap(id -> ItemSalesTestDataFactory.createMockItemMarketListing(id, 100, "i", startDate,
                endDate)
            .stream()
            .map(snapshot -> ItemPriceBuilder.builder()
                .withItemId(snapshot.getItemId())
                .withPrices(snapshot.getPrices().getFirst())
                .withDate(snapshot.getSnapshotDate())
                .build()))
        .toList();
  }

  public static List<ItemPrice> mockItemPriceHistory(
      Long itemId,
      LocalDate startDate,
      LocalDate endDate) {
    return ItemSalesTestDataFactory.createMockItemMarketListing(itemId, 1000, "1", startDate,
            endDate)
        .stream()
        .map(snapshot -> ItemPriceBuilder.builder()
            .withItemId(snapshot.getItemId())
            .withPrices(snapshot.getPrices().getFirst())
            .withDate(snapshot.getSnapshotDate())
            .build())
        .toList();
  }

}
