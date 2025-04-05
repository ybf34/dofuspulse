package com.dofuspulse.api.items.integration;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.items.dto.ItemTypeDto;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.model.ItemType;
import com.dofuspulse.api.repository.ItemTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@Transactional
public class ItemTypeControllerIntegrationTest extends PostgresIntegrationTestContainer {

  ItemType mockItemType;

  @Autowired
  MockMvcTester mockMvcTester;

  @Autowired
  ItemTypeRepository itemTypeRepository;

  @BeforeEach
  void setUp() {
    itemTypeRepository.deleteAll();
    mockItemType = ItemTestDataFactory.createMockItemType();
    itemTypeRepository.save(mockItemType);
  }

  @Test
  void shouldReturnItemTypeByIdWith200Status() {
    mockMvcTester.get()
        .uri("/api/v1/item-types/{id}", mockItemType.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200)
        .bodyJson()
        .convertTo(ItemTypeDto.class)
        .isEqualTo(new ItemTypeDto(mockItemType));
  }

  @Test
  void shouldReturn404WhenItemTypeNotFound() {
    mockMvcTester.get()
        .uri("/api/v1/item-types/{id}", 14)
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(404);
  }

  @Test
  void shouldReturnItemTypesPageWith200Status() {
    mockMvcTester.get()
        .uri("/api/v1/item-types")
        .contentType(MediaType.APPLICATION_JSON)
        .queryParam("size", "20")
        .assertThat()
        .hasStatus(200)
        .bodyJson()
        .hasPath("$.page.totalElements")
        .hasPath("$.page.totalPages")
        .hasPath("$.page.size")
        .hasPath("$.content");
  }


}
