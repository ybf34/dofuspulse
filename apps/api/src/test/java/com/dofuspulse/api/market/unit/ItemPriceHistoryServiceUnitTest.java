package com.dofuspulse.api.market.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.exception.ItemNotFoundException;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.market.fixtures.ItemMarketEntryTestDataFactory;
import com.dofuspulse.api.market.service.ItemPriceHistoryServiceImpl;
import com.dofuspulse.api.projections.ItemPrice;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import com.dofuspulse.api.repository.ItemMarketEntryRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ItemPriceHistoryServiceUnitTest {

  LocalDate startDate = LocalDate.of(2024, 1, 1);
  LocalDate endDate = LocalDate.of(2024, 1, 7);
  Long mockItemId = 1L;
  List<ItemPrice> mockItemPriceHistory;

  @Mock
  ItemDetailsRepository itemDetailsRepository;

  @Mock
  ItemMarketEntryRepository itemMarketEntryRepository;

  @InjectMocks
  ItemPriceHistoryServiceImpl itemPriceHistoryService;

  @BeforeEach
  void setUp() {
    mockItemPriceHistory = ItemMarketEntryTestDataFactory.mockItemPriceHistory(mockItemId,
        100, startDate, endDate);
  }

  @Test
  void shouldReturnItemPriceHistory() {

    when(itemDetailsRepository.findById(eq(mockItemId)))
        .thenReturn(Optional.of(
            ItemTestDataFactory.createMockItemDetails(mockItemId, List.of(), List.of())));

    when(itemMarketEntryRepository.getPriceHistoryInDateRangeForItems(
        List.of(mockItemId), startDate, endDate))
        .thenReturn(mockItemPriceHistory);

    List<ItemPrice> itemPriceHistory = itemPriceHistoryService.getItemPriceHistory(mockItemId,
        startDate, endDate);

    assertThat(itemPriceHistory)
        .hasSize(7)
        .usingRecursiveComparison()
        .isEqualTo(mockItemPriceHistory);

    verify(itemDetailsRepository, times(1)).findById(eq(mockItemId));
    verify(itemMarketEntryRepository, times(1)).getPriceHistoryInDateRangeForItems(
        eq(List.of(mockItemId)), eq(startDate), eq(endDate));
  }

  @Test
  void shouldReturnItemNotFoundException() {

    when(itemDetailsRepository.findById(not(eq(mockItemId))))
        .thenReturn(Optional.empty());

    assertThatThrownBy(() -> itemPriceHistoryService.getItemPriceHistory(
        2L, startDate, endDate))
        .isInstanceOf(ItemNotFoundException.class);

    verify(itemDetailsRepository, times(1)).findById(eq(2L));
    verify(itemMarketEntryRepository, times(0)).getPriceHistoryInDateRangeForItems(
        eq(List.of(2L)), eq(startDate), eq(endDate));
  }
}
