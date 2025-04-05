package com.dofuspulse.api.items.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.model.ItemType;
import com.dofuspulse.api.repository.ItemTypeRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ItemTypeRepositoryIntegrationTest extends PostgresIntegrationTestContainer {

  ItemType mockItemType;

  @Autowired
  ItemTypeRepository itemTypeRepository;

  @BeforeEach
  void setUp() {
    itemTypeRepository.deleteAll();
    mockItemType = ItemTestDataFactory.createMockItemType();
    itemTypeRepository.save(mockItemType);
  }

  @Test
  @DisplayName("Should find the item type by id")
  void shouldFindItemTypeById() {
    Optional<ItemType> itemType = itemTypeRepository.findById(mockItemType.getId());

    assertThat(itemType)
        .isPresent()
        .get()
        .usingRecursiveComparison()
        .isEqualTo(mockItemType);
  }

  @Test
  @DisplayName("Should find all item types")
  void shouldFindAllItemTypes() {
    List<ItemType> types = itemTypeRepository.findAll();

    assertThat(types)
        .hasSize(1)
        .first()
        .usingRecursiveComparison()
        .isEqualTo(mockItemType);
  }
}
