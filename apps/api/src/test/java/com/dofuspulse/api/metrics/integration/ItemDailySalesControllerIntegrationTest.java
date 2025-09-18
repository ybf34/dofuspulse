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
public class ItemDailySalesControllerIntegrationTest extends PostgresIntegrationTestContainer {

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

    mockItemDetails = ItemTestDataFactory.createMockItemDetails(1L, List.of(), List.of());
    itemDetailsRepository.save(mockItemDetails);

    List<ItemMarketEntry> itemMarketListings = ItemMarketEntryTestDataFactory
        .createMockItemMarketListing(
            mockItemDetails.getId(), 100000, "1",
            startDate, endDate);

    itemMarketEntryRepository.saveAll(itemMarketListings);
  }

  @Test
  void shouldReturnItemDailySalesWith200Status() {

    mockMvcTester.get().uri("/api/v1/items/{id}/sales-history", mockItemDetails.getId())
        .param("startDate", startDate.toString())
        .param("endDate", endDate.toString())
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200);
  }

  @Test
  void shouldReturn404WhenNoSnapshotsFoundMatchingDateRange() {

    mockMvcTester.get().uri("/api/v1/items/{id}/sales-history", 33)
        .param("startDate", startDate.toString())
        .param("endDate", endDate.toString())
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(404);
  }

  @Test
  void shouldReturnSalesHistoriesWhenItemsMatchesFiltersAndDates() {

    ItemDetailsSearchCriteria itemFilters = ItemDetailsSearchCriteria
        .builder()
        .build();

    var itemFiltersQueryParams = ItemTestDataFactory.createItemDetailsQueryParams(
        itemFilters);

    mockMvcTester.get().uri("/api/v1/items/sales-history")
        .params(itemFiltersQueryParams)
        .param("startDate", startDate.toString())
        .param("endDate", endDate.toString())
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200);
  }

}
