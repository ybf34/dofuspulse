package com.dofuspulse.api.items.unit;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.items.dto.ItemTypeDto;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.items.service.ItemTypeServiceImpl;
import com.dofuspulse.api.model.ItemType;
import com.dofuspulse.api.repository.ItemTypeRepository;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@DisplayName("Item Type Service Unit Test")
@ExtendWith(MockitoExtension.class)
public class ItemTypeServiceUnitTest {

  @Mock
  ItemTypeRepository itemTypeRepository;

  @InjectMocks
  ItemTypeServiceImpl itemTypeService;

  @Test
  void shouldReturnItemTypeById() {
    //given
    ItemType mockItemType = ItemTestDataFactory.createMockItemType();
    when(itemTypeRepository.findById(1L)).thenReturn(Optional.of(mockItemType));

    //when
    Optional<ItemTypeDto> itemType = itemTypeService.getItemTypeById(1L);

    //then
    assertThat(itemType)
        .isPresent()
        .get()
        .extracting(ItemTypeDto::id, ItemTypeDto::name)
        .containsExactly(mockItemType.getId(), mockItemType.getName());

    verify(itemTypeRepository, times(1)).findById(1L);
  }

  @Test
  void shouldReturnEmptyOptionalWhenItemTypeNotFound() {

    when(itemTypeRepository.findById(1L)).thenReturn(Optional.empty());
    Optional<ItemTypeDto> itemType = itemTypeService.getItemTypeById(1L);

    assertThat(itemType).isEmpty();
    verify(itemTypeRepository, times(1)).findById(1L);

  }

  @Test
  void shouldReturnPageOfAllItemTypes() {

    //given
    Pageable pageable = Pageable.ofSize(20);
    ItemType itemType = ItemTestDataFactory.createMockItemType();
    Page<ItemType> mockItemTypePage = new PageImpl<>(List.of(itemType), pageable, 1);
    when(itemTypeRepository.findAll(any(Pageable.class))).thenReturn(mockItemTypePage);

    //when
    Page<ItemTypeDto> itemTypes = itemTypeService.getItemTypes(pageable);

    //then
    assertThat(itemTypes)
        .asInstanceOf(InstanceOfAssertFactories.type(Page.class))
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
            List.of(new ItemTypeDto(itemType))
        );

    verify(itemTypeRepository, times(1)).findAll(any(Pageable.class));
  }

  @Test
  void shouldReturnNoContentPageWhenItemTypesNotFound() {

    //given
    Pageable pageable = Pageable.ofSize(20);
    Page<ItemType> mockItemTypePage = new PageImpl<>(List.of(), pageable, 0);

    when(itemTypeRepository.findAll(any(Pageable.class))).thenReturn(mockItemTypePage);

    //when
    Page<ItemTypeDto> itemTypes = itemTypeService.getItemTypes(pageable);

    //then
    assertThat(itemTypes)
        .isEmpty();

    assertThat(itemTypes.getTotalElements()).isZero();
    assertThat(itemTypes.getTotalPages()).isZero();
    assertThat(itemTypes.getSize()).isEqualTo(20);

    verify(itemTypeRepository, times(1)).findAll(any(Pageable.class));
  }
}
