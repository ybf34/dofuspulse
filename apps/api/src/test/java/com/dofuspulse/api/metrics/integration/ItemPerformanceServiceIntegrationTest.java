package com.dofuspulse.api.metrics.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.metrics.MetricRegistry;
import com.dofuspulse.api.metrics.calculator.CraftCostCalculator;
import com.dofuspulse.api.metrics.calculator.DailySalesCalculator;
import com.dofuspulse.api.metrics.calculator.PerformanceCalculator;
import com.dofuspulse.api.metrics.calculator.ProfitMetricsCalculator;
import com.dofuspulse.api.metrics.fixtures.ItemSalesTestDataFactory;
import com.dofuspulse.api.metrics.service.ItemDailySalesServiceImpl;
import com.dofuspulse.api.metrics.service.ItemPerformanceServiceImpl;
import com.dofuspulse.api.metrics.service.ItemProfitMetricsServiceImpl;
import com.dofuspulse.api.metrics.service.contract.ItemDailySalesService;
import com.dofuspulse.api.metrics.service.contract.ItemPerformanceService;
import com.dofuspulse.api.metrics.service.contract.ItemProfitMetricsService;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.model.ItemSalesSnapshot;
import com.dofuspulse.api.projections.ItemPerformance;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import com.dofuspulse.api.repository.ItemSalesSnapshotRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({
    MetricRegistry.class,
    ProfitMetricsCalculator.class,
    PerformanceCalculator.class,
    DailySalesCalculator.class,
    CraftCostCalculator.class,

})
public class ItemPerformanceServiceIntegrationTest extends PostgresIntegrationTestContainer {

  ItemDetails mockItemDetails;
  LocalDate startDate = LocalDate.of(2024, 1, 1);
  LocalDate endDate = LocalDate.of(2024, 1, 14);

  @Autowired
  ItemDetailsRepository itemDetailsRepository;

  @Autowired
  ItemSalesSnapshotRepository itemSalesSnapshotRepository;

  ItemPerformanceService itemPerformanceService;

  @Autowired
  MetricRegistry metricRegistry;

  @BeforeEach
  void setUp() {

    itemDetailsRepository.deleteAll();
    itemSalesSnapshotRepository.deleteAll();

    Map<Long, Integer> ingredientQuantityMap = Map.of(
        400L, 10,
        401L, 5,
        402L, 33
    );

    mockItemDetails = ItemTestDataFactory.createMockItemDetails(
        1L,
        ingredientQuantityMap.values().stream().toList(),
        ingredientQuantityMap.keySet().stream().toList()
    );

    itemDetailsRepository.save(mockItemDetails);

    for (Long id : ingredientQuantityMap.keySet()) {
      List<ItemSalesSnapshot> ingredientMarketListing = ItemSalesTestDataFactory
          .createMockItemMarketListing(
              id, 100, "i",
              startDate, endDate);

      itemSalesSnapshotRepository.saveAll(ingredientMarketListing);
    }

    List<ItemSalesSnapshot> itemMarketListings = ItemSalesTestDataFactory
        .createMockItemMarketListing(
            mockItemDetails.getId(), 100000, "1",
            startDate, endDate);

    itemSalesSnapshotRepository.saveAll(itemMarketListings);

    ItemDailySalesService itemDailySalesService = new ItemDailySalesServiceImpl(
        itemDetailsRepository,
        itemSalesSnapshotRepository,
        metricRegistry

    );

    ItemProfitMetricsService itemProfitMetricsService = new ItemProfitMetricsServiceImpl(
        itemDetailsRepository,
        itemSalesSnapshotRepository,
        metricRegistry
    );

    itemPerformanceService = new ItemPerformanceServiceImpl(itemDetailsRepository,
        itemDailySalesService, itemProfitMetricsService, metricRegistry);

  }

  @Test
  void shouldReturnItemPerformanceMetrics() {

    Optional<ItemPerformance> itemPerformanceOpt = itemPerformanceService
        .getItemPerformanceMetrics(mockItemDetails.getId(), startDate, endDate);

    assertThat(itemPerformanceOpt)
        .isPresent();

  }

  @Test
  void shouldReturnPerformanceMetricsWhenItemsMatchesFiltersAndDates() {

    ItemDetailsSearchCriteria itemFilters = ItemDetailsSearchCriteria
        .builder()
        .types(List.of(mockItemDetails.getItemTypeId()))
        .build();

    List<ItemPerformance> itemsPerformance = itemPerformanceService.getItemsPerformanceMetrics(
        itemFilters, startDate, endDate);

    assertThat(itemsPerformance)
        .hasSize(1)
        .first()
        .extracting(ItemPerformance::itemId)
        .isEqualTo(mockItemDetails.getId());

  }

}
