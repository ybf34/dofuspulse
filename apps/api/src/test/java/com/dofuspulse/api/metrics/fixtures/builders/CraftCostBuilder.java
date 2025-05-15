package com.dofuspulse.api.metrics.fixtures.builders;

import com.dofuspulse.api.projections.CraftCost;
import java.time.LocalDate;

public class CraftCostBuilder {

  private Long itemId = 1L;
  private LocalDate snapshotDate = LocalDate.now();
  private int craftCost = 0;

  public static CraftCostBuilder builder() {
    return new CraftCostBuilder();
  }

  public CraftCostBuilder withItemId(Long itemId) {
    this.itemId = itemId;
    return this;
  }

  public CraftCostBuilder withDate(LocalDate date) {
    this.snapshotDate = date;
    return this;
  }

  public CraftCostBuilder withCraftCost(int craftCost) {
    this.craftCost = craftCost;
    return this;
  }

  public CraftCost build() {
    return new CraftCost() {
      @Override
      public Long getItemId() {
        return itemId;
      }

      @Override
      public LocalDate getSnapshotDate() {
        return snapshotDate;
      }

      @Override
      public int getCraftCost() {
        return craftCost;
      }
    };
  }
}
