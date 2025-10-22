package com.dofuspulse.api.items.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.items.dto.ItemDetailsDto;
import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.items.service.ItemDetailsServiceImpl;
import com.dofuspulse.api.items.service.contract.ItemDetailsService;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.repository.ItemDetailsRepository;
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
public class ItemDetailsServiceIntegrationTest extends PostgresIntegrationTestContainer {

  ItemDetails mockItemDetails;

  @Autowired
  ItemDetailsRepository itemDetailsRepository;

  ItemDetailsService itemDetailsService;

  @BeforeEach
  void setUp() {
    itemDetailsRepository.deleteAll();
    mockItemDetails = ItemTestDataFactory.createMockItemDetails(1L, List.of(), List.of(14L));
    itemDetailsRepository.save(mockItemDetails);

    itemDetailsService = new ItemDetailsServiceImpl(itemDetailsRepository);
  }

  @Test
  @DisplayName("should return item details by ID")
  void shouldFindItemById() {

    Optional<ItemDetailsDto> itemDetails = itemDetailsService.findById(mockItemDetails.getId());

    assertThat(itemDetails)
        .isPresent()
        .contains(new ItemDetailsDto(mockItemDetails));
  }

  @Test
  @DisplayName("should return empty optional when item details not found")
  void shouldReturnEmptyOptionalWhenItemNotFound() {

    Optional<ItemDetailsDto> itemDetails = itemDetailsService.findById(2L);

    assertThat(itemDetails)
        .isEmpty();
  }

  @Test
  @DisplayName("should return item details by filters params")
  void shouldFindAllItemsWithValidFilters() {

    var itemDetailsFiltersParams = ItemTestDataFactory.createValidTestItemSearchCriteria();

    Page<ItemDetailsDto> itemsDetailsPage = itemDetailsService.findAll(itemDetailsFiltersParams,
        Pageable.ofSize(20));

    assertThat(itemsDetailsPage)
        .asInstanceOf(InstanceOfAssertFactories.type(Page.class))
        .isNotNull()
        .extracting(
            Page::getTotalElements,
            Page::getTotalPages,
            Page::getSize,
            Page::getNumber,
            Page::getContent
        )
        .containsExactly(
            1L,
            1,
            20,
            0,
            List.of(new ItemDetailsDto(mockItemDetails))
        );
  }

  @Test
  void shouldReturnEmptyPageWhenNoItemsFound() {

    ItemDetailsSearchCriteria itemDetailsFiltersParams = ItemDetailsSearchCriteria
        .builder()
        .typesIds(List.of(1L))
        .ingredient(15L)
        .build();

    Page<ItemDetailsDto> itemsDetailsPage = itemDetailsService.findAll(itemDetailsFiltersParams,
        Pageable.ofSize(20));

    assertThat(itemsDetailsPage)
        .asInstanceOf(InstanceOfAssertFactories.type(Page.class))
        .isNotNull()
        .extracting(
            Page::getTotalElements,
            Page::getTotalPages,
            Page::getSize,
            Page::getNumber,
            Page::getContent
        )
        .containsExactly(
            0L,
            0,
            20,
            0,
            List.of()
        );
  }

}

