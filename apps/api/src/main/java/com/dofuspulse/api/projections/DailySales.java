package com.dofuspulse.api.projections;

import java.time.LocalDate;

public record DailySales(LocalDate date,
                         int sold,
                         int added,
                         int expired,
                         double avgSoldDuration,
                         int listingCount,
                         int revenue) {}
