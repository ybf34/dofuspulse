package com.dofuspulse.api.items.specification;

import static com.dofuspulse.api.items.specification.ItemDetailsSpecification.hasEffect;
import static com.dofuspulse.api.items.specification.ItemDetailsSpecification.hasIngredient;
import static com.dofuspulse.api.items.specification.ItemDetailsSpecification.hasLevelBetween;
import static com.dofuspulse.api.items.specification.ItemDetailsSpecification.hasName;
import static com.dofuspulse.api.items.specification.ItemDetailsSpecification.hasTypeIdIn;
import static com.dofuspulse.api.items.specification.ItemDetailsSpecification.hasTypeNameIn;

import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.model.ItemDetails;
import org.springframework.data.jpa.domain.Specification;

public class ItemDetailsSpecificationBuilder {

  public static Specification<ItemDetails> buildSpecification(ItemDetailsSearchCriteria criteria) {
    return hasName(criteria.name())
        .and(hasTypeIdIn(criteria.types()))
        .and(hasEffect(criteria.effect()))
        .and(hasIngredient(criteria.ingredient()))
        .and(hasTypeNameIn(criteria.typesNames()))
        .and(hasLevelBetween(criteria.minLevel(), criteria.maxLevel()));
  }
}
