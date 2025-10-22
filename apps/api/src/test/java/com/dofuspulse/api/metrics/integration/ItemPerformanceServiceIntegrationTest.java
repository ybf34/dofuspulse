package com.dofuspulse.api.metrics.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.market.fixtures.ItemMarketEntryTestDataFactory;
import com.dofuspulse.api.metrics.MetricRegistry;
import com.dofuspulse.api.metrics.calculator.DailySalesCalculator;
import com.dofuspulse.api.metrics.calculator.PerformanceCalculator;
import com.dofuspulse.api.metrics.calculator.ProfitMetricsCalculator;
import com.dofuspulse.api.metrics.service.ItemDailySalesServiceImpl;
import com.dofuspulse.api.metrics.service.ItemPerformanceServiceImpl;
import com.dofuspulse.api.metrics.service.ItemProfitMetricsServiceImpl;
import com.dofuspulse.api.metrics.service.contract.ItemDailySalesService;
import com.dofuspulse.api.metrics.service.contract.ItemPerformanceService;
import com.dofuspulse.api.metrics.service.contract.ItemProfitMetricsService;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.model.ItemMarketEntry;
import com.dofuspulse.api.projections.ItemPerformance;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import com.dofuspulse.api.repository.ItemMarketEntryRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
})
public class ItemPerformanceServiceIntegrationTest extends PostgresIntegrationTestContainer {

  ItemDetails mockItemDetails;
  LocalDate startDate = LocalDate.of(2024, 1, 1);
  LocalDate endDate = LocalDate.of(2024, 1, 14);

  @Autowired
  ItemDetailsRepository itemDetailsRepository;

  @Autowired
  ItemMarketEntryRepository itemMarketEntryRepository;

  ItemPerformanceService itemPerformanceService;

  @Autowired
  MetricRegistry metricRegistry;

  @BeforeEach
  void setUp() {

    itemDetailsRepository.deleteAll();
    itemMarketEntryRepository.deleteAll();

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
      List<ItemMarketEntry> ingredientMarketListing = ItemMarketEntryTestDataFactory
          .createMockItemMarketListing(
              id, 100, "i",
              startDate, endDate);

      itemMarketEntryRepository.saveAll(ingredientMarketListing);
    }

    List<ItemMarketEntry> itemMarketListings = ItemMarketEntryTestDataFactory
        .createMockItemMarketListing(
            mockItemDetails.getId(), 100000, "1",
            startDate, endDate);

    itemMarketEntryRepository.saveAll(itemMarketListings);

    ItemDailySalesService itemDailySalesService = new ItemDailySalesServiceImpl(
        itemDetailsRepository,
        itemMarketEntryRepository,
        metricRegistry

    );

    ItemProfitMetricsService itemProfitMetricsService = new ItemProfitMetricsServiceImpl(
        itemDetailsRepository,
        itemMarketEntryRepository,
        metricRegistry
    );

    itemPerformanceService = new ItemPerformanceServiceImpl(itemDetailsRepository,
        itemDailySalesService, itemProfitMetricsService, metricRegistry);

  }

  @Test
  void shouldReturnItemPerformanceMetrics() {

    ItemPerformance itemPerformance = itemPerformanceService
        .getItemPerformanceMetrics(mockItemDetails.getId(), startDate, endDate);

    assertThat(itemPerformance).isNotNull();

  }

  @Test
  void shouldReturnPerformanceMetricsWhenItemsMatchesFiltersAndDates() {

    ItemDetailsSearchCriteria itemFilters = ItemDetailsSearchCriteria
        .builder()
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
