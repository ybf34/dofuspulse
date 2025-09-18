package com.dofuspulse.api.items.specification;

import com.dofuspulse.api.model.ItemDetails;
import jakarta.persistence.criteria.Predicate;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class ItemDetailsSpecification {

  public static Specification<ItemDetails> hasName(String name) {
    return (root, query, cb) -> {
      if (name == null || name.isEmpty()) {
        return cb.conjunction();
      }
      return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    };
  }

  public static Specification<ItemDetails> hasTypesIds(List<Long> typeIds) {
    return ((root, query, cb) -> {
      if (typeIds != null && !typeIds.isEmpty()) {
        return root.get("itemTypeId").in(typeIds);
      }
      return cb.conjunction();
    });
  }

  public static Specification<ItemDetails> hasLevelBetween(Long minLevel, Long maxLevel) {
    return (root, query, cb) -> {
      Predicate predicate = cb.conjunction();

      if (minLevel != null) {
        predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("level"), minLevel));
      }

      if (maxLevel != null) {
        predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("level"), maxLevel));
      }

      return predicate;
    };
  }

  public static Specification<ItemDetails> hasIngredient(Long ingredientId) {
    return (root, query, cb) -> {
      if (ingredientId == null) {
        return cb.conjunction();
      }
      return cb.isTrue(cb.function("array_contains", Boolean.class, root.get("ingredientIds"),
          cb.literal(ingredientId)));
    };
  }
}
