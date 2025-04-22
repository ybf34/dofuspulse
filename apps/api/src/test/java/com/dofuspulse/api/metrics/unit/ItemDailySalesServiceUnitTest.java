package com.dofuspulse.api.metrics.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.exception.ItemNotFoundException;
import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.market.fixtures.ItemMarketEntryTestDataFactory;
import com.dofuspulse.api.metrics.MetricRegistry;
import com.dofuspulse.api.metrics.MetricType;
import com.dofuspulse.api.metrics.calculator.params.DailySalesParam;
import com.dofuspulse.api.metrics.service.ItemDailySalesServiceImpl;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.model.ItemMarketEntry;
import com.dofuspulse.api.projections.DailySales;
import com.dofuspulse.api.projections.DailySalesList;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import com.dofuspulse.api.repository.ItemMarketEntryRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class ItemDailySalesServiceUnitTest {

  LocalDate startDate = LocalDate.of(2024, 1, 1);

  LocalDate endDate = LocalDate.of(2024, 1, 2);

  @Mock
  ItemDetailsRepository itemDetailsRepository;
  @Mock
  ItemMarketEntryRepository itemMarketEntryRepository;
  @Mock
  MetricRegistry metricRegistry;

  @InjectMocks
  ItemDailySalesServiceImpl itemDailySalesService;

  @Test
  void shouldReturnItemDailySalesHistory() {

    //given
    ItemDetails mockItemDetails = ItemTestDataFactory.createMockItemDetails(1L, List.of(),
        List.of());

    var mockItemMarketEntries = ItemMarketEntryTestDataFactory.createMockItemMarketListing(
        mockItemDetails.getId(), 100, "1", startDate, endDate);

    List<DailySales> mockExpectedComputedSalesHistory = List.of(
        new DailySales(startDate, 0, 10, 0, 0, 10, 0),
        new DailySales(endDate, 0, 15, 0, 0, 25, 0)
    );

    when(itemDetailsRepository.findById(
        mockItemDetails.getId())).thenReturn(Optional.of(mockItemDetails));

    when(itemMarketEntryRepository.findAllByItemIdInAndEntryDateIsBetween(
        List.of(mockItemDetails.getId()), startDate, endDate)).thenReturn(mockItemMarketEntries);

    when(metricRegistry.calculate(MetricType.DAILY_SALES,
        new DailySalesParam(mockItemMarketEntries)))
        .thenReturn(mockExpectedComputedSalesHistory);

    //when
    List<DailySales> itemDailySales = itemDailySalesService.getItemDailySalesHistory(1L, startDate,
        endDate);

    assertThat(itemDailySales)
        .hasSize(2)
        .usingRecursiveFieldByFieldElementComparator()
        .isEqualTo(mockExpectedComputedSalesHistory);

    verify(itemMarketEntryRepository, times(1)).findAllByItemIdInAndEntryDateIsBetween(
        eq(List.of(mockItemDetails.getId())), eq(startDate), eq(endDate));

    verify(metricRegistry, times(1)).calculate(eq(MetricType.DAILY_SALES),
        eq(new DailySalesParam(mockItemMarketEntries)));

  }

  @Test
  void shouldReturnSalesListPerItemAndFilterEmptyOnes() {

    //given
    List<ItemDetails> mockItemsDetails = List.of(
        ItemTestDataFactory.createMockItemDetails(1L, List.of(), List.of()),
        ItemTestDataFactory.createMockItemDetails(2L, List.of(), List.of())
    );

    when(itemDetailsRepository.findAll(any(Specification.class))).thenReturn(mockItemsDetails);

    List<ItemMarketEntry> itemMarketEntries =
        ItemMarketEntryTestDataFactory.createMockItemMarketListing(
            mockItemsDetails.getFirst().getId(), 100, "1", startDate, endDate);

    List<Long> itemIds = mockItemsDetails.stream().map(ItemDetails::getId).toList();

    when(itemMarketEntryRepository.findAllByItemIdInAndEntryDateIsBetween(itemIds, startDate,
        endDate))
        .thenReturn(itemMarketEntries);

    List<DailySalesList> mockResutDailySalesList = List.of(
        new DailySalesList(mockItemsDetails.get(0).getId(),
            List.of(
                new DailySales(startDate, 0, 10, 0, 0, 10, 0),
                new DailySales(endDate, 0, 15, 0, 0, 25, 0)
            )
        ),
        new DailySalesList(mockItemsDetails.get(1).getId(),
            List.of() //no sales data for second item
        ));

    when(metricRegistry.calculate(eq(MetricType.DAILY_SALES), any(DailySalesParam.class)))
        .thenAnswer(invocation -> {

          DailySalesParam dailySalesParam = invocation.getArgument(1);

          if (dailySalesParam == null || dailySalesParam.itemMarketEntries() == null) {
            return List.of();
          }

          Long itemId = dailySalesParam.itemMarketEntries().getFirst().getItemId();

          return mockResutDailySalesList.stream()
              .filter(salesList -> Objects.equals(salesList.itemId(), itemId))
              .map(DailySalesList::dailySales)
              .flatMap(List::stream)
              .toList();

        });

    //when
    long itemCommonCategoryTypeFilter = 1;
    ItemDetailsSearchCriteria itemFilters = ItemDetailsSearchCriteria.builder()
        .types(List.of(itemCommonCategoryTypeFilter)).build();

    List<DailySalesList> itemDailySalesLists = itemDailySalesService.getItemsDailySalesHistory(
        itemFilters, startDate, endDate);

    assertThat(itemDailySalesLists)
        .hasSize(1)
        .first()
        .extracting(DailySalesList::itemId, DailySalesList::dailySales)
        .contains(
            mockResutDailySalesList.getFirst().itemId(),
            mockResutDailySalesList.getFirst().dailySales()
        );

    verify(itemDetailsRepository, times(1)).findAll(any(Specification.class));
    verify(itemMarketEntryRepository, times(1)).findAllByItemIdInAndEntryDateIsBetween(
        eq(itemIds),
        eq(startDate),
        eq(endDate));
    verify(metricRegistry, times(1)).calculate(eq(MetricType.DAILY_SALES),
        any(DailySalesParam.class));

  }

  @Test
  void shouldReturnEmptyDailySalesListWhenNoItemMatchesFilters() {

    //given
    ItemDetailsSearchCriteria itemFilters = ItemDetailsSearchCriteria.builder()
        .types(List.of(1L)).build();

    when(itemDetailsRepository.findAll(any(Specification.class))).thenReturn(List.of());

    //when
    List<DailySalesList> itemDailySalesLists = itemDailySalesService.getItemsDailySalesHistory(
        itemFilters, startDate, endDate);

    //then
    assertThat(itemDailySalesLists)
        .isEmpty();

    verify(itemDetailsRepository, times(1)).findAll(any(Specification.class));
    verify(itemMarketEntryRepository, times(0)).findAllByItemIdInAndEntryDateIsBetween(any(),
        any(), any());
    verify(metricRegistry, times(0)).calculate(any(), any());
  }

  @Test
  void shouldReturnNotFoundItemExceptionWhenItemNotFoundById() {

    when(itemDetailsRepository.findById(2L)).thenThrow(ItemNotFoundException.class);

    assertThatThrownBy(() -> itemDailySalesService.getItemDailySalesHistory(2L, startDate, endDate))
        .isInstanceOf(ItemNotFoundException.class);

  }
}
