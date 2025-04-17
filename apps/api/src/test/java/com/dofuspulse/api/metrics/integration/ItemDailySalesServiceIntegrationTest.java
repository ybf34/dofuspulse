package com.dofuspulse.api.metrics.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.metrics.MetricRegistry;
import com.dofuspulse.api.metrics.calculator.DailySalesCalculator;
import com.dofuspulse.api.metrics.fixtures.ItemSalesTestDataFactory;
import com.dofuspulse.api.metrics.service.ItemDailySalesServiceImpl;
import com.dofuspulse.api.metrics.service.contract.ItemDailySalesService;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.model.ItemSalesSnapshot;
import com.dofuspulse.api.projections.DailySales;
import com.dofuspulse.api.projections.DailySalesList;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import com.dofuspulse.api.repository.ItemSalesSnapshotRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({
    MetricRegistry.class,
    DailySalesCalculator.class,
})
public class ItemDailySalesServiceIntegrationTest extends PostgresIntegrationTestContainer {

  ItemDetails mockItemDetails;

  LocalDate startDate = LocalDate.of(2024, 1, 1);

  LocalDate endDate = LocalDate.of(2024, 1, 14);

  @Autowired
  ItemDetailsRepository itemDetailsRepository;

  @Autowired
  ItemSalesSnapshotRepository itemSalesSnapshotRepository;

  @Autowired
  MetricRegistry metricRegistry;

  ItemDailySalesService itemDailySalesService;

  @BeforeEach
  void setUp() {

    itemDetailsRepository.deleteAll();
    itemSalesSnapshotRepository.deleteAll();

    mockItemDetails = ItemTestDataFactory.createMockItemDetails(1L, List.of(), List.of());
    itemDetailsRepository.save(mockItemDetails);

    List<ItemSalesSnapshot> itemMarketListings = ItemSalesTestDataFactory
        .createMockItemMarketListing(
            mockItemDetails.getId(), 100000, "1",
            startDate, endDate);

    itemSalesSnapshotRepository.saveAll(itemMarketListings);

    itemDailySalesService = new ItemDailySalesServiceImpl(itemDetailsRepository,
        itemSalesSnapshotRepository, metricRegistry);
  }

  @Test
  void shouldReturnItemDailySalesHistory() {

    //when
    List<DailySales> itemDailySales = itemDailySalesService.getItemDailySalesHistory(
        mockItemDetails.getId(), startDate, endDate);

    //then
    assertThat(itemDailySales)
        .hasSize(14)
        .extracting(DailySales::listingCount)
        .containsOnly(1);
  }

  @Test
  void shouldReturnDailySalesHistoryForItemsMatchingFilterAndDateRange() {

    ItemDetailsSearchCriteria itemFilters = ItemDetailsSearchCriteria
        .builder()
        .types(List.of(mockItemDetails.getItemTypeId()))
        .build();

    //when
    List<DailySalesList> itemsDailySales = itemDailySalesService.getItemsDailySalesHistory(
        itemFilters, startDate, endDate);

    //then
    assertThat(itemsDailySales)
        .hasSize(1)
        .first()
        .satisfies(dailySalesList -> {
          assertThat(dailySalesList.itemId()).isEqualTo(mockItemDetails.getId());
          assertThat(dailySalesList.dailySales())
              .hasSize(14)
              .extracting(DailySales::listingCount)
              .containsOnly(1);
        });
  }
}
