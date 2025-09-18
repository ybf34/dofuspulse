package com.dofuspulse.api.items.specification;

import static com.dofuspulse.api.items.specification.ItemDetailsSpecification.hasIngredient;
import static com.dofuspulse.api.items.specification.ItemDetailsSpecification.hasLevelBetween;
import static com.dofuspulse.api.items.specification.ItemDetailsSpecification.hasName;
import static com.dofuspulse.api.items.specification.ItemDetailsSpecification.hasTypesIds;

import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.model.ItemDetails;
import org.springframework.data.jpa.domain.Specification;

public class ItemDetailsSpecificationBuilder {

  public static Specification<ItemDetails> buildSpecification(ItemDetailsSearchCriteria criteria) {
    return hasName(criteria.name())
        .and(hasTypesIds(criteria.typesIds()))
        .and(hasIngredient(criteria.ingredient()))
        .and(hasLevelBetween(criteria.minLevel(), criteria.maxLevel()));
  }
}
