package com.dofuspulse.api.metrics.fixtures.builders;

import com.dofuspulse.api.projections.ItemPrice;
import java.time.LocalDate;
import java.util.List;


public class ItemPriceBuilder {

  private Long itemId = 1L;
  private LocalDate snapshotDate = LocalDate.now();
  private List<Integer> prices = List.of(100);

  public static ItemPriceBuilder builder() {
    return new ItemPriceBuilder();
  }

  public ItemPriceBuilder withItemId(Long itemId) {
    this.itemId = itemId;
    return this;
  }

  public ItemPriceBuilder withDate(LocalDate date) {
    this.snapshotDate = date;
    return this;
  }

  public ItemPriceBuilder withPrices(Integer... prices) {
    this.prices = List.of(prices);
    return this;
  }

  public ItemPrice build() {
    return new ItemPrice() {
      @Override
      public Long getItemId() {
        return itemId;
      }

      @Override
      public LocalDate getSnapshotDate() {
        return snapshotDate;
      }

      @Override
      public List<Integer> getPrices() {
        return prices;
      }
    };
  }
}
