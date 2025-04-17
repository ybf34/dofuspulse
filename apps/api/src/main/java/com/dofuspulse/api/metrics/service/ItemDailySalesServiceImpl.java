package com.dofuspulse.api.metrics.service;

import com.dofuspulse.api.exception.ItemNotFoundException;
import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.items.specification.ItemDetailsSpecificationBuilder;
import com.dofuspulse.api.metrics.MetricRegistry;
import com.dofuspulse.api.metrics.MetricType;
import com.dofuspulse.api.metrics.calculator.params.DailySalesParam;
import com.dofuspulse.api.metrics.service.contract.ItemDailySalesService;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.model.ItemSalesSnapshot;
import com.dofuspulse.api.projections.DailySales;
import com.dofuspulse.api.projections.DailySalesList;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import com.dofuspulse.api.repository.ItemSalesSnapshotRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemDailySalesServiceImpl implements ItemDailySalesService {

  private final ItemDetailsRepository idr;
  private final ItemSalesSnapshotRepository isr;
  private final MetricRegistry metricRegistry;

  @Override
  public List<DailySales> getItemDailySalesHistory(
      Long itemId,
      LocalDate startDate,
      LocalDate endDate) {

    idr.findById(itemId)
        .orElseThrow(() -> new ItemNotFoundException("Item with id " + itemId + " not found"));

    List<ItemSalesSnapshot> salesData = isr.findAllByItemIdInAndSnapshotDateIsBetween(
        List.of(itemId), startDate, endDate);

    return metricRegistry.calculate(MetricType.DAILY_SALES,
        new DailySalesParam(salesData));
  }

  @Override
  public List<DailySalesList> getItemsDailySalesHistory(
      ItemDetailsSearchCriteria params,
      LocalDate startDate,
      LocalDate endDate) {

    List<ItemDetails> items = idr.findAll(
        ItemDetailsSpecificationBuilder.buildSpecification(params));

    if (items.isEmpty()) {
      return List.of();
    }

    var itemsSalesData = isr.findAllByItemIdInAndSnapshotDateIsBetween(
            items.stream().map(ItemDetails::getId).toList(),
            startDate, endDate)
        .stream()
        .collect(Collectors.groupingBy(ItemSalesSnapshot::getItemId));

    return items.parallelStream()
        .filter(item -> itemsSalesData.containsKey(item.getId()))
        .map(item -> {
          List<DailySales> itemDailySales = metricRegistry.calculate(MetricType.DAILY_SALES,
              new DailySalesParam(itemsSalesData.get(item.getId())));

          return new DailySalesList(item.getId(), itemDailySales);
        })
        .toList();
  }

}
