package com.dofuspulse.api.metrics.service.contract;

import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.projections.ItemPerformance;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ItemPerformanceService {

  Optional<ItemPerformance> getItemPerformanceMetrics(
      Long itemId,
      LocalDate startDate,
      LocalDate endDate
  );

  List<ItemPerformance> getItemsPerformanceMetrics(
      ItemDetailsSearchCriteria params,
      LocalDate startDate,
      LocalDate endDate
  );

}
