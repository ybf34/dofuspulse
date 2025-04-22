package com.dofuspulse.api.market.integration;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.market.fixtures.ItemMarketEntryTestDataFactory;
import com.dofuspulse.api.market.fixtures.ItemMarketEntryTestDataFactory.TestItemPriceDto;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
public class ItemPriceHistoryControllerIntegrationTest extends PostgresIntegrationTestContainer {

  LocalDate startDate = LocalDate.of(2024, 1, 1);
  LocalDate endDate = LocalDate.of(2024, 1, 7);
  Long mockItemId = 1L;
  int mockItemPrice = 100;
  List<ItemMarketEntry> mockItemMarketEntries;

  @Autowired
  MockMvcTester mockMvcTester;

  @Autowired
  ItemDetailsRepository itemDetailsRepository;

  @Autowired
  ItemMarketEntryRepository itemMarketEntryRepository;

  @BeforeEach
  void setUp() {
    itemMarketEntryRepository.deleteAll();
    itemDetailsRepository.save(
        ItemTestDataFactory.createMockItemDetails(mockItemId, List.of(), List.of()));

    mockItemMarketEntries = ItemMarketEntryTestDataFactory.createMockItemMarketListing(
        1L, 100, "1", startDate, endDate);

    itemMarketEntryRepository.saveAll(mockItemMarketEntries);
  }

  @Test
  void shouldReturnItemPriceHistoryWith200Status() {

    mockMvcTester
        .get()
        .uri("/api/v1/items/{id}/price-history", mockItemId)
        .param("startDate", startDate.toString())
        .param("endDate", endDate.toString())
        .assertThat()
        .hasStatus(200)
        .bodyJson()
        .convertTo(TestItemPriceDto[].class)
        .usingRecursiveComparison()
        .isEqualTo(
            ItemMarketEntryTestDataFactory.mockItemPriceHistory(mockItemId, mockItemPrice,
                    startDate, endDate)
                .stream()
                .map(ip -> new TestItemPriceDto(ip.getItemId(), ip.getSnapshotDate(),
                    ip.getPrices()))
                .toArray(TestItemPriceDto[]::new)
        );

  }

  @Test
  void shouldReturn404IfItemNotFound() {
    mockMvcTester
        .get()
        .uri("/api/v1/items/{id}/price-history", 2L)
        .param("startDate", startDate.toString())
        .param("endDate", endDate.toString())
        .assertThat()
        .hasStatus(404);
  }
}
