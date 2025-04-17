package com.dofuspulse.api.metrics.service.contract;

import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.projections.DailySales;
import com.dofuspulse.api.projections.DailySalesList;
import java.time.LocalDate;
import java.util.List;

public interface ItemDailySalesService {

  List<DailySales> getItemDailySalesHistory(
      Long itemId,
      LocalDate startDate,
      LocalDate endDate
  );

  List<DailySalesList> getItemsDailySalesHistory(
      ItemDetailsSearchCriteria params,
      LocalDate startDate,
      LocalDate endDate
  );

}
