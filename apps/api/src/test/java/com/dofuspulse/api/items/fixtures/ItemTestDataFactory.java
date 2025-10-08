package com.dofuspulse.api.items.fixtures;

import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.model.ItemType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class ItemTestDataFactory {

  public static ItemType createMockItemType() {
    ItemType mockItemType = new ItemType();
    mockItemType.setId(1L);
    mockItemType.setName("Amulette");
    return mockItemType;
  }

  public static ItemDetails createMockItemDetails(
      Long id,
      List<Integer> quantities,
      List<Long> ingredientIds) {
    ItemDetails mockItemDetails = new ItemDetails();

    mockItemDetails.setId(id);
    mockItemDetails.setName("Amulette X");
    mockItemDetails.setIconId(12000L);
    mockItemDetails.setLevel(200L);
    mockItemDetails.setItemTypeId(1L);
    mockItemDetails.setIngredientIds(ingredientIds);
    mockItemDetails.setQuantities(quantities);

    mockItemDetails.setPossibleEffects(List.of());

    return mockItemDetails;
  }

  public static ItemDetailsSearchCriteria createValidTestItemSearchCriteria() {
    return ItemDetailsSearchCriteria.builder().name("X").minLevel(1L).maxLevel(200L)
        .typesIds(List.of(1L)).ingredient(14L).effectsIds(List.of()).build();
  }

  public static ItemDetailsSearchCriteria createInvalidTestItemSearchCriteria() {
    return ItemDetailsSearchCriteria
        .builder()
        .name("X")
        .minLevel(0L)
        .maxLevel(300L)
        .typesIds(List.of(1L))
        .ingredient(14L)
        .effectsIds(List.of(111L))
        .build();
  }

  public static MultiValueMap<String, String> createItemDetailsQueryParams(ItemDetailsSearchCriteria filters) {
    MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();

    if (filters.name() != null) {
      queryParams.add("name", filters.name());
    }
    if (filters.typesIds() != null) {
      queryParams.add("typesIds",
          filters.typesIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
    }
    if (filters.effectsIds() != null) {
      queryParams.add("effectsIds",
          filters.effectsIds().stream().map(String::valueOf).collect(Collectors.joining(",")));
    }
    if (filters.ingredient() != null) {
      queryParams.add("ingredient", String.valueOf(filters.ingredient()));
    }
    if (filters.minLevel() != null) {
      queryParams.add("minLevel", String.valueOf(filters.minLevel()));
    }
    if (filters.maxLevel() != null) {
      queryParams.add("maxLevel", String.valueOf(filters.maxLevel()));
    }
    return queryParams;
  }

  public static Stream<Arguments> itemDetailsQueryParamsScenarios() {
    return Stream.of(

        Arguments.of("Out of range minLevel/maxLevel",
            ItemTestDataFactory.createItemDetailsQueryParams(
                ItemDetailsSearchCriteria.builder().minLevel(0L).maxLevel(300L).typesIds(List.of(1L))
                    .build())),

        Arguments.of("Name exceed max characters", ItemTestDataFactory.createItemDetailsQueryParams(
            ItemDetailsSearchCriteria.builder().name("A".repeat(101)).typesIds(List.of(1L)).build())),

        Arguments.of("typesIds exceeds max size (46 elements)", ItemTestDataFactory.createItemDetailsQueryParams(
            ItemDetailsSearchCriteria.builder()
                .typesIds(Stream.iterate(1L, i -> i + 1).limit(46).collect(Collectors.toList()))
                .build())),

        Arguments.of("effectsIds exceeds max size (21 elements)", ItemTestDataFactory.createItemDetailsQueryParams(
            ItemDetailsSearchCriteria.builder()
                .effectsIds(Stream.iterate(100L, i -> i + 1).limit(21).collect(Collectors.toList()))
                .typesIds(List.of(1L))
                .build()))
    );
  }

}