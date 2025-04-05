package com.dofuspulse.api.items.unit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.items.controller.ItemDetailsController;
import com.dofuspulse.api.items.dto.ItemDetailsDto;
import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.items.service.ItemDetailsServiceImpl;
import com.dofuspulse.api.security.CustomAccessDeniedHandler;
import com.dofuspulse.api.security.UnauthorizedHandler;
import com.dofuspulse.api.security.WebSecurityConfig;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.util.MultiValueMap;

@WebMvcTest(ItemDetailsController.class)
@WithMockUser
@Import({WebSecurityConfig.class, UnauthorizedHandler.class, CustomAccessDeniedHandler.class})
public class ItemDetailsControllerUnitTest {

  @Autowired
  MockMvcTester mockMvcTester;

  @MockitoBean
  ItemDetailsServiceImpl itemDetailsService;

  @Test
  void shouldReturnItemDetailsByIdWith200Status() {

    ItemDetailsDto mockItemDetails = new ItemDetailsDto(
        ItemTestDataFactory.createMockItemDetails());

    when(itemDetailsService.findById(1L)).thenReturn(Optional.of(mockItemDetails));

    mockMvcTester.get()
        .uri("/api/v1/items/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200);

    verify(itemDetailsService, times(1)).findById(1L);
  }

  @Test
  void shouldReturn404WhenItemDetailsByIdNotFound() {

    when(itemDetailsService.findById(1L)).thenReturn(Optional.empty());

    mockMvcTester.get()
        .uri("/api/v1/items/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(404);

    verify(itemDetailsService, times(1)).findById(1L);
  }

  @Test
  void shouldReturnItemDetailsPageWhenGivenValidFiltersWith200Status() {

    ItemDetailsDto mockItemDetails = new ItemDetailsDto(
        ItemTestDataFactory.createMockItemDetails());

    Page<ItemDetailsDto> mockItemDetailsPage = new PageImpl<>(
        List.of(mockItemDetails),
        Pageable.ofSize(20),
        1);

    ItemDetailsSearchCriteria filters = ItemTestDataFactory.createValidTestItemSearchCriteria();

    MultiValueMap<String, String> itemDetailsQueryParams =
        ItemTestDataFactory.createItemDetailsQueryParams(
            filters);

    when(itemDetailsService.findAll(any(ItemDetailsSearchCriteria.class), any(Pageable.class)))
        .thenReturn(mockItemDetailsPage);

    mockMvcTester.get()
        .uri("/api/v1/items")
        .queryParams(itemDetailsQueryParams)
        .queryParam("size", "20")
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200)
        .bodyJson()
        .hasPath("$.page.totalElements")
        .hasPath("$.page.totalPages")
        .hasPath("$.page.size")
        .hasPath("$.content");

    verify(itemDetailsService, times(1)).findAll(
        any(ItemDetailsSearchCriteria.class), any(Pageable.class));
  }

  @ParameterizedTest(name = "{index} => Item Details Scenario: {0}")
  @MethodSource("com.dofuspulse.api.items.fixtures.ItemTestDataFactory#itemDetailsQueryParamsScenarios")
  @DisplayName("Should return 400 when invalid item details query params provided")
  void shouldReturn400WhenInvalidRegistrationDataProvided(
      String displayName,
      MultiValueMap<String, String> queryParamsScenario) {

    mockMvcTester.get()
        .uri("/api/v1/items")
        .queryParams(queryParamsScenario)
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(400);

    verify(itemDetailsService, times(0)).findAll(any(), any());
  }
}
