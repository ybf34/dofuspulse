package com.dofuspulse.api.metrics.integration;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.market.fixtures.ItemMarketEntryTestDataFactory;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.model.ItemMarketEntry;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import com.dofuspulse.api.repository.ItemMarketEntryRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
public class ItemProfitMetricsControllerIntegrationTest extends PostgresIntegrationTestContainer {

  ItemDetails mockItemDetails;

  LocalDate startDate = LocalDate.of(2024, 1, 1);

  LocalDate endDate = LocalDate.of(2024, 1, 14);

  @Autowired
  MockMvcTester mockMvcTester;

  @Autowired
  ItemDetailsRepository itemDetailsRepository;

  @Autowired
  ItemMarketEntryRepository itemMarketEntryRepository;

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

  }

  @Test
  void shouldReturnItemProfitMetricsHistoryWith200Status() {

    mockMvcTester.get().uri("/api/v1/items/{id}/profit-metrics", mockItemDetails.getId())
        .param("startDate", startDate.toString())
        .param("endDate", endDate.toString())
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200);
  }

  @Test
  void shouldReturnProfitMetricsHistoriesWhenItemsMatchesFiltersAndDates() {

    ItemDetailsSearchCriteria itemFilters = ItemDetailsSearchCriteria
        .builder()
        .build();

    var itemFiltersQueryParams = ItemTestDataFactory.createItemDetailsQueryParams(
        itemFilters);

    mockMvcTester.get().uri("/api/v1/items/profit-metrics")
        .params(itemFiltersQueryParams)
        .param("startDate", startDate.toString())
        .param("endDate", endDate.toString())
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200);
  }

}
