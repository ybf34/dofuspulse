package com.dofuspulse.api.items.integration;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.items.dto.ItemDetailsDto;
import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
public class ItemDetailsControllerIntegrationTest extends PostgresIntegrationTestContainer {

  ItemDetails mockItemDetails;

  @Autowired
  MockMvcTester mockMvcTester;

  @Autowired
  ItemDetailsRepository itemDetailsRepository;

  @BeforeEach
  void setUp() {
    itemDetailsRepository.deleteAll();
    mockItemDetails = ItemTestDataFactory.createMockItemDetails();
    itemDetailsRepository.save(mockItemDetails);
  }

  @Test
  void shouldReturnItemDetailsByIdWith200Status() {
    mockMvcTester.get()
        .uri("/api/v1/items/{id}", mockItemDetails.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200)
        .bodyJson()
        .convertTo(ItemDetailsDto.class)
        .isEqualTo(new ItemDetailsDto(mockItemDetails));

  }

  @Test
  void shouldReturn404WhenItemNotFound() {
    mockMvcTester.get()
        .uri("/api/v1/items/{id}", 2L)
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(404);
  }

  @Test
  void shouldReturnItemDetailsPageWhenGivenValidFiltersWith200Status() {

    ItemDetailsSearchCriteria filters = ItemTestDataFactory.createValidTestItemSearchCriteria();
    MultiValueMap<String, String> queryParams = ItemTestDataFactory.createItemDetailsQueryParams(
        filters);

    mockMvcTester.get()
        .uri("/api/v1/items")
        .queryParams(queryParams)
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200)
        .bodyJson()
        .hasPath("$.page.totalElements")
        .hasPath("$.page.totalPages")
        .hasPath("$.page.size")
        .hasPath("$.content");
  }

  @Test
  void shouldReturn400WhenGivenInvalidQueryParamsFilters() {

    ItemDetailsSearchCriteria filters = ItemTestDataFactory.createInvalidTestItemSearchCriteria();
    MultiValueMap<String, String> queryParams = ItemTestDataFactory.createItemDetailsQueryParams(
        filters);

    mockMvcTester.get()
        .uri("/api/v1/items")
        .queryParams(queryParams)
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(400);
  }

}
