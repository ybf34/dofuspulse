package com.dofuspulse.api.market.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.metrics.fixtures.ItemMarketEntryTestDataFactory;
import com.dofuspulse.api.model.ItemMarketEntry;
import com.dofuspulse.api.projections.ItemPrice;
import com.dofuspulse.api.repository.ItemMarketEntryRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ItemMarketEntryRepositoryIntegrationTest extends PostgresIntegrationTestContainer {

  LocalDate startDate = LocalDate.of(2024, 1, 1);
  LocalDate endDate = LocalDate.of(2024, 1, 7);

  Long mockItemId = 1L;
  List<ItemMarketEntry> mockItemMarketEntries;

  @Autowired
  ItemMarketEntryRepository itemMarketEntryRepository;

  @BeforeEach
  void setUp() {
    itemMarketEntryRepository.deleteAll();
    mockItemMarketEntries = ItemMarketEntryTestDataFactory.createMockItemMarketListing(1L, 100, "1",
        startDate, endDate);

    itemMarketEntryRepository.saveAll(mockItemMarketEntries);
  }

  @Test
  void shouldReturnItemMarketEntries() {

  }

  @Test
  void shouldReturnItemsPriceHistoryGivenDateRange() {

    List<ItemPrice> marketEntries = itemMarketEntryRepository.getPriceHistoryInDateRangeForItems(
        List.of(mockItemId), startDate, endDate);

    assertThat(marketEntries)
        .hasSize(7);
  }
}
