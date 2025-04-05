package com.dofuspulse.api.items.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ItemDetailsRepositoryIntegrationTest extends PostgresIntegrationTestContainer {

  ItemDetails mockItemDetails;

  @Autowired
  ItemDetailsRepository itemDetailsRepository;

  @BeforeEach
  void setUp() {
    itemDetailsRepository.deleteAll();

    mockItemDetails = ItemTestDataFactory.createMockItemDetails();
    itemDetailsRepository.save(mockItemDetails);
  }

  @Test
  @DisplayName("Should find item details by id")
  void shouldFindItemDetailsById() {

    Optional<ItemDetails> itemDetails = itemDetailsRepository.findById(mockItemDetails.getId());

    assertThat(itemDetails)
        .isPresent()
        .get()
        .usingRecursiveComparison()
        .isEqualTo(mockItemDetails);

  }

}
