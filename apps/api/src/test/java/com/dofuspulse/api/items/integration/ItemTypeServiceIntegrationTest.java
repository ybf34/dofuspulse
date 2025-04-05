package com.dofuspulse.api.items.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.items.dto.ItemTypeDto;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.items.service.ItemTypeServiceImpl;
import com.dofuspulse.api.items.service.contract.ItemTypeService;
import com.dofuspulse.api.model.ItemType;
import com.dofuspulse.api.repository.ItemTypeRepository;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@DataJpaTest
public class ItemTypeServiceIntegrationTest extends PostgresIntegrationTestContainer {

  ItemType mockItemType;

  @Autowired
  ItemTypeRepository itemTypeRepository;

  ItemTypeService itemTypeService;

  @BeforeEach
  void setUp() {
    itemTypeRepository.deleteAll();

    mockItemType = ItemTestDataFactory.createMockItemType();

    itemTypeRepository.save(mockItemType);
    itemTypeService = new ItemTypeServiceImpl(itemTypeRepository);
  }

  @Test
  @DisplayName("Should return item type by id")
  void shouldFindTypeById() {
    Optional<ItemTypeDto> itemType = itemTypeService.getItemTypeById(mockItemType.getId());

    assertThat(itemType)
        .isPresent()
        .contains(new ItemTypeDto(mockItemType));

  }

  @Test
  @DisplayName("Should return all item types")
  void shouldGetAllItemTypesPage() {
    Page<ItemTypeDto> itemTypes = itemTypeService.getItemTypes(Pageable.ofSize(20));

    assertThat(itemTypes)
        .asInstanceOf(InstanceOfAssertFactories.type(Page.class))
        .isNotNull()
        .extracting(
            Page::getTotalElements,
            Page::getTotalPages,
            Page::getSize,
            Page::getNumber,
            Page::getContent)
        .containsExactly(
            1L,
            1,
            20,
            0,
            List.of(new ItemTypeDto(mockItemType))
        );
  }

}
