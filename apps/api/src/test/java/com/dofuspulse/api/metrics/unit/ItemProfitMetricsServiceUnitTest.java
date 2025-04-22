package com.dofuspulse.api.metrics.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.exception.ItemNotFoundException;
import com.dofuspulse.api.exception.NotCraftableItemException;
import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.market.fixtures.ItemMarketEntryTestDataFactory;
import com.dofuspulse.api.metrics.MetricRegistry;
import com.dofuspulse.api.metrics.MetricType;
import com.dofuspulse.api.metrics.calculator.params.CraftCostParams;
import com.dofuspulse.api.metrics.calculator.params.ProfitMetricsParams;
import com.dofuspulse.api.metrics.service.ItemProfitMetricsServiceImpl;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.projections.CraftCost;
import com.dofuspulse.api.projections.ItemPrice;
import com.dofuspulse.api.projections.ProfitMetrics;
import com.dofuspulse.api.projections.ProfitMetricsList;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import com.dofuspulse.api.repository.ItemMarketEntryRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
public class ItemProfitMetricsServiceUnitTest {

  LocalDate startDate = LocalDate.of(2024, 1, 1);

  LocalDate endDate = LocalDate.of(2024, 1, 2);

  ItemDetails craftableItemDetails;
  Map<Long, Integer> ingredientQuantityMap;
  ItemDetails notCraftableItemDetails;

  List<CraftCost> mockItemCraftCosts;
  List<ProfitMetrics> mockProfitMetrics;

  @Mock
  ItemDetailsRepository itemDetailsRepository;
  @Mock
  ItemMarketEntryRepository itemMarketEntryRepository;
  @Mock
  MetricRegistry metricRegistry;

  @InjectMocks
  ItemProfitMetricsServiceImpl itemProfitMetricsService;

  @Captor
  ArgumentCaptor<CraftCostParams> craftCostParamsCaptor;

  @Captor
  ArgumentCaptor<ProfitMetricsParams> profitMetricsParamsCaptor;

  @BeforeEach
  void setUp() {
    mockItemCraftCosts = List.of(
        new CraftCost(startDate, 100),
        new CraftCost(endDate, 200)
    );

    mockProfitMetrics = List.of(
        new ProfitMetrics(startDate, 300, 500, 200, 1),
        new ProfitMetrics(endDate, 200, 500, 300, 1.5)
    );

    ingredientQuantityMap = Map.of(
        400L, 10,
        401L, 5,
        402L, 33
    );

    List<Long> ingredientsIds = ingredientQuantityMap.keySet().stream().toList();
    List<Integer> quantities = ingredientQuantityMap.values().stream().toList();

    craftableItemDetails = ItemTestDataFactory.createMockItemDetails(1L,
        quantities,
        ingredientsIds);

    notCraftableItemDetails = ItemTestDataFactory.createMockItemDetails(2L,
        null, //no quantites
        null //no ingredients
    );

  }

  @Test
  void shouldReturnItemProfitMetricsHistory() {

    //given

    ItemDetails mockItemDetails = ItemTestDataFactory.createMockItemDetails(
        1L,
        ingredientQuantityMap.values().stream().toList(),
        ingredientQuantityMap.keySet().stream().toList()
    );

    List<Long> ingredientsIds = ingredientQuantityMap.keySet().stream().toList();
    List<ItemPrice> mockAllIngredientsPrices = ItemMarketEntryTestDataFactory.mockIngredientsPrices(
        ingredientsIds, startDate, endDate);
    List<ItemPrice> mockItemPriceHistory = ItemMarketEntryTestDataFactory.mockItemPriceHistory(
        mockItemDetails.getId(), 100, startDate, endDate);

    when(itemDetailsRepository.findById(mockItemDetails.getId()))
        .thenReturn(Optional.of(mockItemDetails));
    when(itemMarketEntryRepository.getPriceHistoryInDateRangeForItems(eq(ingredientsIds),
        eq(startDate), eq(endDate)))
        .thenReturn(mockAllIngredientsPrices);
    when(itemMarketEntryRepository.getPriceHistoryInDateRangeForItems(
        eq(List.of(mockItemDetails.getId())), eq(startDate), eq(endDate)))
        .thenReturn(mockItemPriceHistory);
    when(metricRegistry.calculate(eq(MetricType.CRAFT_COST), any(CraftCostParams.class)))
        .thenReturn(mockItemCraftCosts);
    when(metricRegistry.calculate(eq(MetricType.PROFIT_METRICS), any(ProfitMetricsParams.class)))
        .thenReturn(mockProfitMetrics);

    //when
    List<ProfitMetrics> itemProfitMetrics = itemProfitMetricsService.getItemProfitMetricsHistory(
        mockItemDetails.getId(),
        startDate,
        endDate);

    //then
    assertThat(itemProfitMetrics)
        .hasSize(2)
        .usingRecursiveFieldByFieldElementComparator()
        .isEqualTo(mockProfitMetrics);

    verify(itemMarketEntryRepository, times(1)).getPriceHistoryInDateRangeForItems(
        eq(ingredientQuantityMap.keySet().stream().toList()), eq(startDate), eq(endDate));
    verify(itemMarketEntryRepository, times(1)).getPriceHistoryInDateRangeForItems(
        eq(List.of(mockItemDetails.getId())), eq(startDate), eq(endDate));
    verify(metricRegistry, times(1)).calculate(eq(MetricType.CRAFT_COST),
        craftCostParamsCaptor.capture());
    verify(metricRegistry, times(1)).calculate(eq(MetricType.PROFIT_METRICS),
        profitMetricsParamsCaptor.capture());
    assertThat(craftCostParamsCaptor.getValue())
        .asInstanceOf(InstanceOfAssertFactories.type(CraftCostParams.class))
        .extracting(CraftCostParams::ingredientsPrices, CraftCostParams::ingredientQuantityMap)
        .containsExactly(mockAllIngredientsPrices, ingredientQuantityMap);

    assertThat(profitMetricsParamsCaptor.getValue())
        .asInstanceOf(InstanceOfAssertFactories.type(ProfitMetricsParams.class))
        .extracting(ProfitMetricsParams::craftCosts, ProfitMetricsParams::itemPrices)
        .containsExactly(mockItemCraftCosts, mockItemPriceHistory);

  }

  @Test
  void shouldThrowItemNotFoundExceptionWhenItemNotFound() {

    when(itemDetailsRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ItemNotFoundException.class,
        () -> itemProfitMetricsService.getItemProfitMetricsHistory(1L, startDate, endDate));

  }

  @Test
  void shouldThrowNotCraftableItemException() {

    when(itemDetailsRepository.findById(1L)).thenReturn(Optional.of(notCraftableItemDetails));

    assertThrows(NotCraftableItemException.class,
        () -> itemProfitMetricsService.getItemProfitMetricsHistory(1L, startDate, endDate));
  }

  @Test
  void shouldReturnProfitMetricsListPerItemAndFilterEmptyOnes() {

    List<Long> ingredientsIds = ingredientQuantityMap.keySet().stream().toList();

    List<ItemPrice> mockAllIngredientsPrices = ItemMarketEntryTestDataFactory.mockIngredientsPrices(
        ingredientsIds, startDate, endDate);

    List<ItemPrice> mockItemPriceHistory = ItemMarketEntryTestDataFactory.mockItemPriceHistory(
        craftableItemDetails.getId(), 100, startDate, endDate);

    when(itemDetailsRepository.findAll(any(Specification.class)))
        .thenReturn(List.of(craftableItemDetails, notCraftableItemDetails));
    when(itemMarketEntryRepository.getPriceHistoryInDateRangeForItems(eq(ingredientsIds),
        eq(startDate), eq(endDate)))
        .thenReturn(mockAllIngredientsPrices);
    when(itemMarketEntryRepository.getPriceHistoryInDateRangeForItems(
        eq(List.of(craftableItemDetails.getId())), eq(startDate), eq(endDate)))
        .thenReturn(mockItemPriceHistory);
    when(metricRegistry.calculate(eq(MetricType.CRAFT_COST), any(CraftCostParams.class)))
        .thenReturn(mockItemCraftCosts);
    when(metricRegistry.calculate(eq(MetricType.PROFIT_METRICS), any(ProfitMetricsParams.class)))
        .thenReturn(mockProfitMetrics);

    ItemDetailsSearchCriteria itemFilters = ItemDetailsSearchCriteria.builder()
        .types(List.of(1L)).build();

    var itemsProfitMetrics = itemProfitMetricsService.getItemsProfitMetricsHistory(
        itemFilters, startDate, endDate);

    assertThat(itemsProfitMetrics)
        .hasSize(1)
        .first()
        .extracting(ProfitMetricsList::itemId, ProfitMetricsList::profitMetrics)
        .containsExactly(
            craftableItemDetails.getId(),
            mockProfitMetrics
        );

    verify(itemDetailsRepository, times(1)).findAll(any(Specification.class));

    verify(itemMarketEntryRepository, times(1)).getPriceHistoryInDateRangeForItems(
        eq(ingredientsIds),
        eq(startDate),
        eq(endDate));

    verify(itemMarketEntryRepository, times(1)).getPriceHistoryInDateRangeForItems(
        eq(List.of(craftableItemDetails.getId())),
        eq(startDate),
        eq(endDate));

    verify(metricRegistry, times(1)).calculate(eq(MetricType.CRAFT_COST),
        craftCostParamsCaptor.capture());
    verify(metricRegistry, times(1)).calculate(eq(MetricType.PROFIT_METRICS),
        profitMetricsParamsCaptor.capture());

    assertThat(craftCostParamsCaptor.getValue())
        .asInstanceOf(InstanceOfAssertFactories.type(CraftCostParams.class))
        .extracting(CraftCostParams::ingredientsPrices, CraftCostParams::ingredientQuantityMap)
        .containsExactly(mockAllIngredientsPrices, ingredientQuantityMap);

    assertThat(profitMetricsParamsCaptor.getValue())
        .asInstanceOf(InstanceOfAssertFactories.type(ProfitMetricsParams.class))
        .extracting(ProfitMetricsParams::craftCosts, ProfitMetricsParams::itemPrices)
        .containsExactly(mockItemCraftCosts, mockItemPriceHistory);
  }

}
