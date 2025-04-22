package com.dofuspulse.api.market.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.exception.ItemNotFoundException;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.market.fixtures.ItemMarketEntryTestDataFactory;
import com.dofuspulse.api.market.service.ItemPriceHistoryServiceImpl;
import com.dofuspulse.api.market.service.contract.ItemPriceHistoryService;
import com.dofuspulse.api.model.ItemMarketEntry;
import com.dofuspulse.api.projections.ItemPrice;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import com.dofuspulse.api.repository.ItemMarketEntryRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ItemPriceHistoryServiceIntegrationTest extends PostgresIntegrationTestContainer {

  LocalDate startDate = LocalDate.of(2024, 1, 1);
  LocalDate endDate = LocalDate.of(2024, 1, 7);

  Long mockItemId = 1L;
  List<ItemMarketEntry> mockItemMarketEntries;

  @Autowired
  ItemDetailsRepository itemDetailsRepository;

  @Autowired
  ItemMarketEntryRepository itemMarketEntryRepository;

  ItemPriceHistoryService itemPriceHistoryService;

  @BeforeEach
  void setUp() {
    itemMarketEntryRepository.deleteAll();
    itemDetailsRepository.save(
        ItemTestDataFactory.createMockItemDetails(mockItemId, List.of(), List.of()));
    mockItemMarketEntries = ItemMarketEntryTestDataFactory.createMockItemMarketListing(1L, 100, "1",
        startDate, endDate);

    itemMarketEntryRepository.saveAll(mockItemMarketEntries);

    itemPriceHistoryService = new ItemPriceHistoryServiceImpl(itemDetailsRepository,
        itemMarketEntryRepository);
  }

  @Test
  void shouldReturnItemPriceHistory() {
    List<ItemPrice> itemPriceHistory = itemPriceHistoryService.getItemPriceHistory(
        mockItemId, startDate, endDate);

    assertThat(itemPriceHistory)
        .hasSize(7)
        .extracting(ItemPrice::getPrices)
        .extracting(List::getFirst)
        .containsExactly(
            mockItemMarketEntries.stream()
                .map(ItemMarketEntry::getPrices)
                .map(List::getFirst)
                .toArray(Integer[]::new)
        );
  }

  @Test
  void shouldReturnItemNotFoundException() {

    assertThatThrownBy(() -> itemPriceHistoryService.getItemPriceHistory(
        2L, startDate, endDate))
        .isInstanceOf(ItemNotFoundException.class);
  }
}
