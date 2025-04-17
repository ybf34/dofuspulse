package com.dofuspulse.api.metrics.service.contract;

import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.projections.ProfitMetrics;
import com.dofuspulse.api.projections.ProfitMetricsList;
import java.time.LocalDate;
import java.util.List;

public interface ItemProfitMetricsService {

  List<ProfitMetrics> getItemProfitMetricsHistory(
      Long itemId,
      LocalDate startDate,
      LocalDate endDate);

  List<ProfitMetricsList> getItemsProfitMetricsHistory(
      ItemDetailsSearchCriteria params,
      LocalDate startDate,
      LocalDate endDate);

}
