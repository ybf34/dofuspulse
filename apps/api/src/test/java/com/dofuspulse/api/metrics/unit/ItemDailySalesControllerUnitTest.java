package com.dofuspulse.api.metrics.unit;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.metrics.controller.ItemDailySalesController;
import com.dofuspulse.api.metrics.service.contract.ItemDailySalesService;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.projections.DailySales;
import com.dofuspulse.api.projections.DailySalesList;
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

@WebMvcTest(ItemDailySalesController.class)
@WithMockUser
@Import({WebSecurityConfig.class, UnauthorizedHandler.class, CustomAccessDeniedHandler.class})
public class ItemDailySalesControllerUnitTest {

  LocalDate startDate = LocalDate.of(2024, 1, 1);

  LocalDate endDate = LocalDate.of(2024, 1, 14);

  List<DailySales> mockItemDailySales;

  @Autowired
  MockMvcTester mockMvcTester;

  @MockitoBean
  ItemDailySalesService itemDailySalesService;

  @BeforeEach
  void setUp() {
    mockItemDailySales = List.of(
        new DailySales(startDate, 0, 10, 0, 0, 10, 0),
        new DailySales(startDate.plusDays(1), 0, 15, 0, 0, 25, 0)
    );
  }

  @Test
  void invalidDateRangeParamShouldReturn400BadRequest() {

    mockMvcTester.get().uri("/api/v1/items/{id}/sales-history", 1L)
        .param("startDate", "invalidStartDate")
        .param("endDate", "invalidEndDate")
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(400);

    verify(itemDailySalesService, times(0)).getItemDailySalesHistory(1L, startDate, endDate);
  }

  @Test
  void shouldReturnItemDailySales() {

    when(itemDailySalesService.getItemDailySalesHistory(1L, startDate, endDate)).thenReturn(
        mockItemDailySales);

    mockMvcTester.get().uri("/api/v1/items/{id}/sales-history", 1L)
        .param("startDate", startDate.toString())
        .param("endDate", endDate.toString())
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200);

    verify(itemDailySalesService, times(1)).getItemDailySalesHistory(1L, startDate, endDate);

  }

  @Test
  void shouldReturnItemDailySalesWhenGivenValidFiltersWith200Status() {

    ItemDetails mockItemDetails = ItemTestDataFactory.createMockItemDetails(1L, List.of(),
        List.of());

    ItemDetailsSearchCriteria itemFilters = ItemDetailsSearchCriteria
        .builder()
        .build();

    var itemFiltersQueryParams = ItemTestDataFactory.createItemDetailsQueryParams(
        itemFilters);

    List<DailySalesList> mockItemDailySalesList = List.of(
        new DailySalesList(mockItemDetails.getId(), mockItemDailySales)
    );

    when(itemDailySalesService.getItemsDailySalesHistory(itemFilters, startDate,
        endDate)).thenReturn(mockItemDailySalesList);

    mockMvcTester.get().uri("/api/v1/items/sales-history")
        .params(itemFiltersQueryParams)
        .param("startDate", startDate.toString())
        .param("endDate", endDate.toString())
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200);

    verify(itemDailySalesService, times(1)).getItemsDailySalesHistory(itemFilters, startDate,
        endDate);
  }

}
