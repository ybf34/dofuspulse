package com.dofuspulse.api.metrics.unit;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.metrics.controller.ItemPerformanceController;
import com.dofuspulse.api.metrics.service.contract.ItemPerformanceService;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.projections.ItemPerformance;
import com.dofuspulse.api.security.CustomAccessDeniedHandler;
import com.dofuspulse.api.security.UnauthorizedHandler;
import com.dofuspulse.api.security.WebSecurityConfig;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(ItemPerformanceController.class)
@WithMockUser
@Import({WebSecurityConfig.class, UnauthorizedHandler.class, CustomAccessDeniedHandler.class})
public class ItemPerformanceControllerUnitTest {

  ItemDetails mockItemDetails;

  LocalDate startDate = LocalDate.of(2024, 1, 1);

  LocalDate endDate = LocalDate.of(2024, 1, 2);

  List<ItemPerformance> mockItemsPerformance;

  @Autowired
  MockMvcTester mockMvcTester;

  @MockitoBean
  ItemPerformanceService itemPerformanceService;

  @BeforeEach
  void setUp() {
    mockItemDetails = ItemTestDataFactory.createMockItemDetails(1L, List.of(),
        List.of());

    mockItemsPerformance = List.of(
        new ItemPerformance(mockItemDetails.getId(), 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
        new ItemPerformance(mockItemDetails.getId() + 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
    );
  }

  @Test
  void shouldReturnItemPerformanceMetrics() {

    when(itemPerformanceService.getItemPerformanceMetrics(1L, startDate, endDate))
        .thenReturn(Optional.of(mockItemsPerformance.getFirst()));

    mockMvcTester.get().uri("/api/v1/items/{id}/performance", mockItemDetails.getId())
        .param("startDate", startDate.toString())
        .param("endDate", endDate.toString())
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200);

    verify(itemPerformanceService, times(1)).getItemPerformanceMetrics(1L, startDate, endDate);

  }

  @Test
  void shouldReturnItemsPerformanceMetricsWhenGivenValidFiltersWith200Status() {

    ItemDetailsSearchCriteria itemFilters = ItemDetailsSearchCriteria
        .builder()
        .build();

    var itemFiltersQueryParams = ItemTestDataFactory.createItemDetailsQueryParams(
        itemFilters);

    when(itemPerformanceService.getItemsPerformanceMetrics(itemFilters, startDate, endDate))
        .thenReturn(mockItemsPerformance);

    mockMvcTester.get().uri("/api/v1/items/performance")
        .params(itemFiltersQueryParams)
        .param("startDate", startDate.toString())
        .param("endDate", endDate.toString())
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200);

    verify(itemPerformanceService, times(1)).getItemsPerformanceMetrics(itemFilters, startDate,
        endDate);

  }
}
