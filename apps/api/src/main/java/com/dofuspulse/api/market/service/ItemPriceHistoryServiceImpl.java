package com.dofuspulse.api.market.service;

import com.dofuspulse.api.exception.ItemNotFoundException;
import com.dofuspulse.api.market.service.contract.ItemPriceHistoryService;
import com.dofuspulse.api.projections.ItemPrice;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import com.dofuspulse.api.repository.ItemMarketEntryRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemPriceHistoryServiceImpl implements ItemPriceHistoryService {

  private final ItemDetailsRepository itemDetailsRepository;
  private final ItemMarketEntryRepository itemMarketEntryRepository;

  @Override
  public List<ItemPrice> getItemPriceHistory(Long id, LocalDate startDate, LocalDate endDate) {

    itemDetailsRepository.findById(id)
        .orElseThrow(() -> new ItemNotFoundException("Item with id " + id + " not found"));

    return itemMarketEntryRepository.getPriceHistoryInDateRangeForItems(
        List.of(id),
        startDate,
        endDate);
  }
}
