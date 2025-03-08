package com.dofuspulse.api.projections;

public record ItemPerformance(
    Long itemId,
    int salesVolume,
    double salesVelocity,
    double avgCraftCost,
    double avgListingPrice,
    double avgDailyProfitMargin,
    double profitMarginTrend,
    double craftCostTrend,
    double priceTrend,
    double salesTrend,
    double listingsTrend,
    double roiTrend,
    double avgSoldDuration,
    double craftCostPctChangeFromAvg
) {}
