package com.dofuspulse.api.market.service.contract;

import com.dofuspulse.api.projections.ItemPrice;
import java.time.LocalDate;
import java.util.List;

public interface ItemPriceHistoryService {

  List<ItemPrice> getItemPriceHistory(Long id, LocalDate startDate, LocalDate endDate);
}
