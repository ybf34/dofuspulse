package com.dofuspulse.api.metrics.fixtures.builders;

import com.dofuspulse.api.model.ItemSalesSnapshot;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class ItemSalesSnapshotBuilder {

  private Long snapshotId = 1L;
  private Long itemId = 1L;
  private LocalDate snapshotDate = LocalDate.now();
  private Map<String, String> effects = Map.of();
  private List<Integer> prices = List.of(100);

  public static ItemSalesSnapshotBuilder builder() {
    return new ItemSalesSnapshotBuilder();
  }

  public ItemSalesSnapshotBuilder withItemId(Long itemId) {
    this.itemId = itemId;
    return this;
  }

  public ItemSalesSnapshotBuilder withDate(LocalDate date) {
    this.snapshotDate = date;
    return this;
  }

  public ItemSalesSnapshotBuilder withEffects(Map<String, String> effects) {
    this.effects = effects;
    return this;
  }

  public ItemSalesSnapshotBuilder withSnapshotId(Long snapshotId) {
    this.snapshotId = snapshotId;
    return this;
  }

  public ItemSalesSnapshotBuilder withPrices(Integer... prices) {
    this.prices = List.of(prices);
    return this;
  }

  public ItemSalesSnapshot build() {
    return new ItemSalesSnapshot(snapshotId, itemId, snapshotDate, effects, prices);
  }
}
