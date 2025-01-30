package com.dofuspulse.api.metrics.fixtures.builders;

import com.dofuspulse.api.projections.PriceHistory;
import java.time.LocalDate;
import java.util.List;

public class PriceHistoryBuilder {

  private LocalDate snapshotDate = LocalDate.now();
  private List<Integer> prices = List.of(100);

  public static PriceHistoryBuilder builder() {
    return new PriceHistoryBuilder();
  }

  public PriceHistoryBuilder withDate(LocalDate date) {
    this.snapshotDate = date;
    return this;
  }

  public PriceHistoryBuilder withPrices(Integer... prices) {
    this.prices = List.of(prices);
    return this;
  }

  public PriceHistory build() {
    return new PriceHistory() {
      @Override
      public LocalDate getSnapshotDate() {return snapshotDate;}

      @Override
      public List<Integer> getPrices() {return prices;}
    };
  }

}
