package com.dofuspulse.api.metrics.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.metrics.MetricRegistry;
import com.dofuspulse.api.metrics.calculator.CraftCostCalculator;
import com.dofuspulse.api.metrics.calculator.ProfitMetricsCalculator;
import com.dofuspulse.api.metrics.fixtures.ItemMarketEntryTestDataFactory;
import com.dofuspulse.api.metrics.service.ItemProfitMetricsServiceImpl;
import com.dofuspulse.api.metrics.service.contract.ItemProfitMetricsService;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.model.ItemMarketEntry;
import com.dofuspulse.api.projections.ProfitMetrics;
import com.dofuspulse.api.projections.ProfitMetricsList;
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
    CraftCostCalculator.class,
    ProfitMetricsCalculator.class,
})
public class ItemProfitMetricsServiceIntegrationTest extends PostgresIntegrationTestContainer {

  ItemDetails mockItemDetails;

  LocalDate startDate = LocalDate.of(2024, 1, 1);

  LocalDate endDate = LocalDate.of(2024, 1, 14);

  @Autowired
  ItemDetailsRepository itemDetailsRepository;

  @Autowired
  ItemMarketEntryRepository itemMarketEntryRepository;

  @Autowired
  MetricRegistry metricRegistry;

  ItemProfitMetricsService itemProfitMetricsService;

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

    itemProfitMetricsService = new ItemProfitMetricsServiceImpl(
        itemDetailsRepository,
        itemMarketEntryRepository,
        metricRegistry);
  }


  @Test
  void shouldReturnItemProfitMetricsHistory() {

    //when
    List<ProfitMetrics> itemDailySales = itemProfitMetricsService.getItemProfitMetricsHistory(
        mockItemDetails.getId(), startDate, endDate);

    //then
    assertThat(itemDailySales)
        .hasSize(14)
        .extracting(ProfitMetrics::craftCost, ProfitMetrics::sellPrice)
        .containsOnly(tuple(4800, 100000));

  }

  @Test
  void shouldReturnProfitMetricsForItemsMatchingFilterAndDateRange() {

    //given
    ItemDetailsSearchCriteria itemFilters = ItemDetailsSearchCriteria
        .builder()
        .types(List.of(mockItemDetails.getItemTypeId()))
        .build();

    //when
    List<ProfitMetricsList> itemsDailySales = itemProfitMetricsService.getItemsProfitMetricsHistory(
        itemFilters, startDate, endDate);

    //then
    assertThat(itemsDailySales)
        .hasSize(1)
        .first()
        .satisfies(dailySalesList -> {
          assertThat(dailySalesList.itemId()).isEqualTo(mockItemDetails.getId());
          assertThat(dailySalesList.profitMetrics())
              .hasSize(14)
              .extracting(ProfitMetrics::craftCost, ProfitMetrics::sellPrice)
              .containsOnly(tuple(4800, 100000));
        });
  }
}
