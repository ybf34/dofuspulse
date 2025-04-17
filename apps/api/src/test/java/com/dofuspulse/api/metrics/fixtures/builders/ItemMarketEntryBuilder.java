package com.dofuspulse.api.metrics.fixtures.builders;

import com.dofuspulse.api.model.ItemMarketEntry;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ItemMarketEntryBuilder {

  private Long snapshotId = 1L;
  private Long itemId = 1L;
  private LocalDate snapshotDate = LocalDate.now();
  private Map<String, String> effects = Map.of();
  private List<Integer> prices = List.of(100);

  public static ItemMarketEntryBuilder builder() {
    return new ItemMarketEntryBuilder();
  }

  public ItemMarketEntryBuilder withItemId(Long itemId) {
    this.itemId = itemId;
    return this;
  }

  public ItemMarketEntryBuilder withDate(LocalDate date) {
    this.snapshotDate = date;
    return this;
  }

  public ItemMarketEntryBuilder withEffects(Map<String, String> effects) {
    this.effects = effects;
    return this;
  }

  public ItemMarketEntryBuilder withPrices(Integer... prices) {
    this.prices = List.of(prices);
    return this;
  }

  public ItemMarketEntry build() {
    return new ItemMarketEntry(snapshotId, itemId, snapshotDate, effects, prices);
  }
}
