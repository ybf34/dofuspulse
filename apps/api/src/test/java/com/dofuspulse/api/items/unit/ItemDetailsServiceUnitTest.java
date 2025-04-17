package com.dofuspulse.api.items.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.items.dto.ItemDetailsDto;
import com.dofuspulse.api.items.fixtures.ItemTestDataFactory;
import com.dofuspulse.api.items.service.ItemDetailsServiceImpl;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.InstanceOfAssertFactories;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class ItemDetailsServiceUnitTest {

  @Mock
  ItemDetailsRepository itemDetailsRepository;

  @InjectMocks
  ItemDetailsServiceImpl itemDetailsService;

  @Test
  void shouldReturnItemDetailsById() {
    //given
    ItemDetails mockItemDetails = ItemTestDataFactory.createMockItemDetails(1L, List.of(),
        List.of());
    when(itemDetailsRepository.findById(1L)).thenReturn(Optional.of(mockItemDetails));

    //when
    Optional<ItemDetailsDto> itemDetailsOpt = itemDetailsService.findById(1L);

    //then
    assertThat(itemDetailsOpt)
        .isPresent()
        .get()
        .usingRecursiveComparison()
        .isEqualTo(new ItemDetailsDto(mockItemDetails));
  }

  @Test
  void shouldReturnEmptyOptionalWhenItemDetailsNotFound() {
    //given
    when(itemDetailsRepository.findById(1L)).thenReturn(Optional.empty());

    //when
    Optional<ItemDetailsDto> itemDetailsOpt = itemDetailsService.findById(1L);

    //then
    assertThat(itemDetailsOpt)
        .isEmpty();

  }

  @Test
  void shouldReturnItemDetailsPageWhenFiltered() {
    //given
    Pageable pageable = Pageable.ofSize(20);
    ItemDetails mockItemDetails = ItemTestDataFactory.createMockItemDetails(1L, List.of(),
        List.of());
    Page<ItemDetails> mockItemDetailsPage = new PageImpl<>(List.of(mockItemDetails), pageable, 1);

    when(itemDetailsRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(
        mockItemDetailsPage);

    //when
    Page<ItemDetailsDto> itemDetailsPage = itemDetailsService.findAll(
        ItemTestDataFactory.createValidTestItemSearchCriteria(), pageable);

    //then
    assertThat(itemDetailsPage)
        .asInstanceOf(InstanceOfAssertFactories.type(Page.class))
        .extracting(
            Page::getTotalElements,
            Page::getTotalPages,
            Page::getSize,
            Page::getNumber,
            Page::getContent
        ).containsExactly(
            1L,
            1,
            20,
            0,
            List.of(new ItemDetailsDto(mockItemDetails))
        );

    verify(itemDetailsRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
  }

  @Test
  void shouldReturnEmptyPageWhenItemDetailsNotFound() {
    //given
    Pageable pageable = Pageable.ofSize(20);
    Page<ItemDetails> mockItemDetailsPage = new PageImpl<>(List.of(), pageable, 0);

    when(itemDetailsRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(
        mockItemDetailsPage);

    //when
    Page<ItemDetailsDto> itemDetailsPage = itemDetailsService.findAll(
        ItemTestDataFactory.createValidTestItemSearchCriteria(), pageable);

    //then
    assertThat(itemDetailsPage)
        .asInstanceOf(InstanceOfAssertFactories.type(Page.class))
        .extracting(
            Page::getTotalElements,
            Page::getTotalPages,
            Page::getSize,
            Page::getNumber,
            Page::getContent
        ).containsExactly(
            0L,
            0,
            20,
            0,
            List.of()
        );

    verify(itemDetailsRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
  }

}
