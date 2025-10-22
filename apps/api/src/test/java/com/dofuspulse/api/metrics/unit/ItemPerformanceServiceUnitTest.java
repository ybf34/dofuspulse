package com.dofuspulse.api.metrics.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.metrics.MetricRegistry;
import com.dofuspulse.api.metrics.MetricType;
import com.dofuspulse.api.metrics.calculator.params.PerformanceMetricsParam;
import com.dofuspulse.api.metrics.service.ItemPerformanceServiceImpl;
import com.dofuspulse.api.metrics.service.contract.ItemDailySalesService;
import com.dofuspulse.api.metrics.service.contract.ItemProfitMetricsService;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.projections.DailySales;
import com.dofuspulse.api.projections.DailySalesList;
import com.dofuspulse.api.projections.ItemPerformance;
import com.dofuspulse.api.projections.ProfitMetrics;
import com.dofuspulse.api.projections.ProfitMetricsList;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class ItemPerformanceServiceUnitTest {

  ItemDetails mockItemDetailsFirst;
  ItemDetails mockItemDetailsSecond;

  List<DailySales> mockItemDailySales;
  List<ProfitMetrics> mockItemProfitMetrics;
  List<ItemPerformance> mockItemsPerformance;

  LocalDate startDate = LocalDate.of(2024, 1, 1);

  LocalDate endDate = LocalDate.of(2024, 1, 2);

  @Mock
  ItemDailySalesService itemDailySalesService;
  @Mock
  ItemProfitMetricsService itemProfitMetricsService;
  @Mock
  ItemDetailsRepository itemDetailsRepository;
  @Mock
  MetricRegistry metricRegistry;

  @InjectMocks
  ItemPerformanceServiceImpl itemPerformanceService;

  @Captor
  ArgumentCaptor<PerformanceMetricsParam> performanceParamsCaptor;

  @BeforeEach
  void setUp() {
    mockItemDetailsFirst = ItemTestDataFactory.createMockItemDetails(1L, List.of(), List.of());
    mockItemDetailsSecond = ItemTestDataFactory.createMockItemDetails(2L, List.of(), List.of());

    mockItemsPerformance = List.of(
        new ItemPerformance(mockItemDetailsFirst.getId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        new ItemPerformance(mockItemDetailsSecond.getId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    );

    mockItemDailySales = List.of(
        new DailySales(startDate, 0, 10, 0, 0, 10, 0),
        new DailySales(startDate.plusDays(1), 0, 15, 0, 0, 25, 0)
    );

    mockItemProfitMetrics = List.of(
        new ProfitMetrics(startDate, 300, 500, 200, 1),
        new ProfitMetrics(endDate, 200, 500, 300, 1.5)
    );
  }

  @Test
  void shouldReturnItemPerformanceMetrics() {

    when(itemDailySalesService.getItemDailySalesHistory(eq(mockItemDetailsFirst.getId()),
        eq(startDate), eq(endDate)))
        .thenReturn(mockItemDailySales);

    when(itemProfitMetricsService.getItemProfitMetricsHistory(eq(mockItemDetailsFirst.getId()),
        eq(startDate), eq(endDate)))
        .thenReturn(mockItemProfitMetrics);

    when(metricRegistry.calculate(eq(MetricType.PERFORMANCE), any(PerformanceMetricsParam.class)))
        .thenReturn(Optional.of(mockItemsPerformance.getFirst()));

    ItemPerformance itemPerformanceOpt = itemPerformanceService.getItemPerformanceMetrics(
        mockItemDetailsFirst.getId(), startDate, endDate);

    assertThat(itemPerformanceOpt)
        .isNotNull()
        .extracting(ItemPerformance::itemId)
        .isEqualTo(mockItemDetailsFirst.getId());

    verify(itemDailySalesService, times(1)).getItemDailySalesHistory(
        eq(mockItemDetailsFirst.getId()), eq(startDate), eq(endDate));
    verify(itemProfitMetricsService, times(1)).getItemProfitMetricsHistory(
        eq(mockItemDetailsFirst.getId()), eq(startDate), eq(endDate));
    verify(metricRegistry, times(1)).calculate(eq(MetricType.PERFORMANCE),
        performanceParamsCaptor.capture());

    assertThat(performanceParamsCaptor.getValue())
        .asInstanceOf(InstanceOfAssertFactories.type(PerformanceMetricsParam.class))
        .extracting(PerformanceMetricsParam::itemId, PerformanceMetricsParam::dailySales,
            PerformanceMetricsParam::profitMetrics)
        .containsExactly(mockItemDetailsFirst.getId(), mockItemDailySales, mockItemProfitMetrics);
  }

  @Test
  void shouldReturnPerformanceMetricsWhenItemsMatchesFiltersAndDates() {
    ItemDetailsSearchCriteria itemFilters = ItemDetailsSearchCriteria
        .builder()
        .typesIds(List.of(mockItemDetailsFirst.getItemTypeId()))
        .build();

    when(itemDetailsRepository.findAll(any(Specification.class)))
        .thenReturn(List.of(mockItemDetailsFirst, mockItemDetailsSecond));

    when(itemDailySalesService.getItemsDailySalesHistory(itemFilters, startDate, endDate))
        .thenReturn(List.of(
            new DailySalesList(mockItemDetailsFirst.getId(), mockItemDailySales),
            new DailySalesList(mockItemDetailsSecond.getId(), mockItemDailySales))
        );

    when(itemProfitMetricsService.getItemsProfitMetricsHistory(itemFilters, startDate, endDate))
        .thenReturn(List.of(
                new ProfitMetricsList(mockItemDetailsFirst.getId(), mockItemProfitMetrics),
                new ProfitMetricsList(mockItemDetailsSecond.getId(), mockItemProfitMetrics)
            )
        );

    when(metricRegistry.calculate(eq(MetricType.PERFORMANCE), any(PerformanceMetricsParam.class)))
        .thenAnswer(invocation -> {

          PerformanceMetricsParam performanceMetricsParams = invocation.getArgument(1);
          Long itemId = performanceMetricsParams.itemId();

          return mockItemsPerformance.stream()
              .filter(itemPerformance -> Objects.equals(itemPerformance.itemId(), itemId))
              .findFirst();
        });

    List<ItemPerformance> itemsPerformance = itemPerformanceService.getItemsPerformanceMetrics(
        itemFilters, startDate, endDate);

    assertThat(itemsPerformance)
        .hasSize(2)
        .extracting(ItemPerformance::itemId)
        .containsExactlyInAnyOrder(
            mockItemDetailsFirst.getId(), mockItemDetailsSecond.getId()
        );

    verify(itemDetailsRepository, times(1)).findAll(any(Specification.class));
    verify(itemDailySalesService, times(1)).getItemsDailySalesHistory(eq(itemFilters),
        eq(startDate), eq(endDate));
    verify(itemProfitMetricsService, times(1)).getItemsProfitMetricsHistory(eq(itemFilters),
        eq(startDate), eq(endDate));
    verify(metricRegistry, times(2)).calculate(eq(MetricType.PERFORMANCE),
        performanceParamsCaptor.capture());

  }

}
