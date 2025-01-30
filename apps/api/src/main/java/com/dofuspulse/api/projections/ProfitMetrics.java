package com.dofuspulse.api.projections;

import java.time.LocalDate;

public record ProfitMetrics(LocalDate snapshotDate,
                            int craftCost,
                            int sellPrice,
                            int profitMargin,
                            double roi) {}
