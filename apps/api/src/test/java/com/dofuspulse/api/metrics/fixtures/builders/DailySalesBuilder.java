package com.dofuspulse.api.metrics.fixtures.builders;

import com.dofuspulse.api.projections.DailySales;
import java.time.LocalDate;

public class DailySalesBuilder {

  private LocalDate date;
  private int sold;
  private int added;
  private int expired;
  private double avgSoldDuration;
  private int listingCount;
  private int revenue;

  public static DailySalesBuilder builder() {
    return new DailySalesBuilder();
  }

  public DailySalesBuilder withDate(LocalDate date) {
    this.date = date;
    return this;
  }

  public DailySalesBuilder withSold(int sold) {
    this.sold = sold;
    return this;
  }

  public DailySalesBuilder withAdded(int added) {
    this.added = added;
    return this;
  }

  public DailySalesBuilder withExpired(int expired) {
    this.expired = expired;
    return this;
  }

  public DailySalesBuilder withAvgSoldDuration(double avgSoldDuration) {
    this.avgSoldDuration = avgSoldDuration;
    return this;
  }

  public DailySalesBuilder withListingCount(int listingCount) {
    this.listingCount = listingCount;
    return this;
  }

  public DailySalesBuilder withRevenue(int revenue) {
    this.revenue = revenue;
    return this;
  }

  public DailySales build() {
    return new DailySales(date, sold, added, expired, avgSoldDuration, listingCount, revenue);
  }
}
