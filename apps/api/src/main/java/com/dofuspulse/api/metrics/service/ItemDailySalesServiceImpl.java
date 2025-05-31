package com.dofuspulse.api.metrics.service;

import com.dofuspulse.api.exception.ItemNotFoundException;
import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.items.specification.ItemDetailsSpecificationBuilder;
import com.dofuspulse.api.metrics.MetricRegistry;
import com.dofuspulse.api.metrics.MetricType;
import com.dofuspulse.api.metrics.calculator.params.DailySalesParam;
import com.dofuspulse.api.metrics.service.contract.ItemDailySalesService;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.projections.DailySales;
import com.dofuspulse.api.projections.DailySalesList;
import com.dofuspulse.api.projections.ItemMarketEntryProjection;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import com.dofuspulse.api.repository.ItemMarketEntryRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemDailySalesServiceImpl implements ItemDailySalesService {

  private final ItemDetailsRepository idr;
  private final ItemMarketEntryRepository imr;
  private final MetricRegistry metricRegistry;

  @Override
  @Transactional(readOnly = true)
  public List<DailySales> getItemDailySalesHistory(
      Long itemId,
      LocalDate startDate,
      LocalDate endDate) {

    idr.findById(itemId)
        .orElseThrow(() -> new ItemNotFoundException("Item with id " + itemId + " not found"));

    List<ItemMarketEntryProjection> itemMarketEntries = imr.findAllByItemIdInAndEntryDateIsBetween(
        List.of(itemId), startDate, endDate);

    return metricRegistry.calculate(MetricType.DAILY_SALES,
        new DailySalesParam(itemMarketEntries));
  }

  @Override
  @Transactional(readOnly = true)
  public List<DailySalesList> getItemsDailySalesHistory(
      ItemDetailsSearchCriteria params,
      LocalDate startDate,
      LocalDate endDate) {

    List<Long> itemsIds = idr.findAll(
            ItemDetailsSpecificationBuilder.buildSpecification(params))
        .stream()
        .map(ItemDetails::getId)
        .toList();

    if (itemsIds.isEmpty()) {
      return List.of();
    }

    List<DailySalesList> itemDailySalesLists = new ArrayList<>();
    for (Long itemId : itemsIds) {

      List<ItemMarketEntryProjection> itemMarketEntries = imr.findAllByItemIdInAndEntryDateIsBetween(
          List.of(itemId), startDate, endDate);

      if (itemMarketEntries.isEmpty()) {
        continue;
      }

      List<DailySales> itemDailySales = metricRegistry.calculate(
          MetricType.DAILY_SALES,
          new DailySalesParam(itemMarketEntries));

      itemDailySalesLists.add(new DailySalesList(itemId, itemDailySales));
    }
    return itemDailySalesLists;
  }

}
