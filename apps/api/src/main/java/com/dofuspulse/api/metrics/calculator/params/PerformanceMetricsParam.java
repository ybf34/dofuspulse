package com.dofuspulse.api.metrics.calculator.params;

import com.dofuspulse.api.projections.DailySales;
import com.dofuspulse.api.projections.ProfitMetrics;
import java.util.List;

public record PerformanceMetricsParam(Long itemId, List<DailySales> dailySales,
                                      List<ProfitMetrics> profitMetrics) implements BaseParams {
}
