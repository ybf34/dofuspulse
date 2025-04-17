package com.dofuspulse.api.metrics.unit;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.metrics.controller.ItemProfitMetricsController;
import com.dofuspulse.api.metrics.service.contract.ItemProfitMetricsService;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.projections.ProfitMetrics;
import com.dofuspulse.api.projections.ProfitMetricsList;
import com.dofuspulse.api.security.CustomAccessDeniedHandler;
import com.dofuspulse.api.security.UnauthorizedHandler;
import com.dofuspulse.api.security.WebSecurityConfig;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(ItemProfitMetricsController.class)
@WithMockUser
@Import({WebSecurityConfig.class, UnauthorizedHandler.class, CustomAccessDeniedHandler.class})
public class ItemProfitMetricsControllerUnitTest {

  LocalDate startDate = LocalDate.of(2024, 1, 1);

  LocalDate endDate = LocalDate.of(2024, 1, 2);

  List<ProfitMetrics> mockItemProfitMetrics;

  @Autowired
  MockMvcTester mockMvcTester;

  @MockitoBean
  ItemProfitMetricsService itemProfitMetricsService;

  @BeforeEach
  void setUp() {
    mockItemProfitMetrics = List.of(
        new ProfitMetrics(startDate, 300, 500, 200, 1),
        new ProfitMetrics(endDate, 200, 500, 300, 1.5)
    );
  }

  @Test
  void shouldReturnItemProfitMetrics() {

    when(itemProfitMetricsService.getItemProfitMetricsHistory(1L, startDate, endDate)).thenReturn(
        mockItemProfitMetrics);

    mockMvcTester.get().uri("/api/v1/items/{id}/profit-metrics", 1L)
        .param("startDate", startDate.toString())
        .param("endDate", endDate.toString())
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200);

    verify(itemProfitMetricsService, times(1)).getItemProfitMetricsHistory(1L, startDate, endDate);

  }

  @Test
  void shouldReturnItemsProfitMetricsWhenGivenValidFiltersWith200Status() {

    ItemDetails mockItemDetails = ItemTestDataFactory.createMockItemDetails(1L, List.of(),
        List.of());

    ItemDetailsSearchCriteria itemFilters = ItemDetailsSearchCriteria
        .builder()
        .types(List.of(mockItemDetails.getItemTypeId()))
        .build();

    var itemFiltersQueryParams = ItemTestDataFactory.createItemDetailsQueryParams(
        itemFilters);

    List<ProfitMetricsList> mockItemProfitMetricsList = List.of(
        new ProfitMetricsList(mockItemDetails.getId(), mockItemProfitMetrics)
    );

    when(itemProfitMetricsService.getItemsProfitMetricsHistory(itemFilters, startDate,
        endDate)).thenReturn(mockItemProfitMetricsList);

    mockMvcTester.get().uri("/api/v1/items/profit-metrics")
        .params(itemFiltersQueryParams)
        .param("startDate", startDate.toString())
        .param("endDate", endDate.toString())
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200);

    verify(itemProfitMetricsService, times(1)).getItemsProfitMetricsHistory(itemFilters, startDate,
        endDate);
  }
}
