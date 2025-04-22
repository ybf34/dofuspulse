package com.dofuspulse.api.market.unit;

import static org.mockito.AdditionalMatchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.exception.ItemNotFoundException;
import com.dofuspulse.api.market.controller.ItemPriceHistoryController;
import com.dofuspulse.api.market.fixtures.ItemMarketEntryTestDataFactory;
import com.dofuspulse.api.market.fixtures.ItemMarketEntryTestDataFactory.TestItemPriceDto;
import com.dofuspulse.api.market.service.ItemPriceHistoryServiceImpl;
import com.dofuspulse.api.projections.ItemPrice;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(ItemPriceHistoryController.class)
@WithMockUser
@Import({WebSecurityConfig.class, UnauthorizedHandler.class, CustomAccessDeniedHandler.class})
public class ItemPriceHistoryControllerUnitTest {

  LocalDate startDate = LocalDate.of(2024, 1, 1);
  LocalDate endDate = LocalDate.of(2024, 1, 7);
  Long mockItemId = 1L;
  int mockItemPrice = 100;
  List<ItemPrice> mockItemPriceHistory;

  @Autowired
  MockMvcTester mockMvcTester;

  @MockitoBean
  ItemPriceHistoryServiceImpl itemPriceHistoryService;

  @BeforeEach
  void setUp() {

    mockItemPriceHistory = ItemMarketEntryTestDataFactory.mockItemPriceHistory(mockItemId,
        mockItemPrice,
        startDate, endDate);

    when(itemPriceHistoryService.getItemPriceHistory(eq(mockItemId), eq(startDate), eq(endDate)))
        .thenReturn(mockItemPriceHistory);

    when(itemPriceHistoryService.getItemPriceHistory(not(eq(mockItemId)), eq(startDate),
        eq(endDate)))
        .thenThrow(ItemNotFoundException.class);
  }

  @Test
  void shouldReturnItemPriceHistoryWith200Status() {

    mockMvcTester
        .get()
        .uri("/api/v1/items/{id}/price-history", mockItemId)
        .param("startDate", startDate.toString())
        .param("endDate", endDate.toString())
        .assertThat()
        .hasStatus(200)
        .bodyJson()
        .convertTo(TestItemPriceDto[].class)
        .usingRecursiveComparison()
        .isEqualTo(
            ItemMarketEntryTestDataFactory.mockItemPriceHistory(mockItemId, mockItemPrice,
                    startDate, endDate)
                .stream()
                .map(ip -> new TestItemPriceDto(ip.getItemId(), ip.getSnapshotDate(),
                    ip.getPrices()))
                .toArray(TestItemPriceDto[]::new)
        );

    verify(itemPriceHistoryService, times(1)).getItemPriceHistory(eq(mockItemId), eq(startDate),
        eq(endDate));
  }

  @Test
  void shouldReturn404WhenItemNotFound() {

    mockMvcTester
        .get()
        .uri("/api/v1/items/{id}/price-history", 2L)
        .param("startDate", startDate.toString())
        .param("endDate", endDate.toString())
        .assertThat()
        .hasStatus(404);

    verify(itemPriceHistoryService, times(1)).getItemPriceHistory(eq(2L), eq(startDate),
        eq(endDate));
  }
}
