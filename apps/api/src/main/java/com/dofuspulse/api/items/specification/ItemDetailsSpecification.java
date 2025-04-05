package com.dofuspulse.api.items.specification;

import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.model.ItemType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
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

  public static Specification<ItemDetails> hasTypeIdIn(List<Long> typeIds) {
    return (root, query, cb) -> {
      if (typeIds == null || typeIds.isEmpty()) {
        return cb.conjunction();
      }
      return root.get("itemTypeId").in(typeIds);
    };
  }

  public static Specification<ItemDetails> hasTypeNameIn(List<String> typeNames) {
    return (root, query, cb) -> {
      if (typeNames == null || typeNames.isEmpty()) {
        return cb.conjunction();
      }
      assert query != null;
      Subquery<Long> subquery = query.subquery(Long.class);
      Root<ItemType> itemTypeRoot = subquery.from(ItemType.class);
      subquery.select(itemTypeRoot
              .get("id"))
          .where(itemTypeRoot.get("name").in(typeNames));

      return root.get("itemTypeId").in(subquery);
    };
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

  public static Specification<ItemDetails> hasEffect(Integer effectId) {
    return (root, query, cb) -> {
      if (effectId == null) {
        return cb.conjunction();
      }
      return cb.isTrue(cb.function("array_contains", Boolean.class, root.get("possibleEffects"),
          cb.literal(effectId)));
    };
  }
}
