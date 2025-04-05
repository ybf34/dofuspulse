package com.dofuspulse.api.projections;

import java.util.List;

public record ProfitMetricsList(Long itemId, List<ProfitMetrics> profitMetrics) {
}