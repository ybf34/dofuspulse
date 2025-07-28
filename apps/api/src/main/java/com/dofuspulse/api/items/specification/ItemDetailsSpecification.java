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

  public static Specification<ItemDetails> hasTypeFilters(
      List<Long> typeIds,
      List<String> typeNames) {
    return ((root, query, cb) -> {
      Predicate typeIdPredicate = null;
      Predicate typeNamePredicate = null;

      if (typeIds != null && !typeIds.isEmpty()) {
        typeIdPredicate = root.get("itemTypeId").in(typeIds);
      }

      if (typeNames != null && !typeNames.isEmpty()) {
        assert query != null;
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<ItemType> itemTypeRoot = subquery.from(ItemType.class);
        subquery.select(itemTypeRoot.get("id"))
            .where(itemTypeRoot.get("name").in(typeNames));
        typeNamePredicate = root.get("itemTypeId").in(subquery);
      }

      if (typeIdPredicate != null && typeNamePredicate != null) {
        return cb.or(typeIdPredicate, typeNamePredicate);
      } else if (typeIdPredicate != null) {
        return typeIdPredicate;
      } else if (typeNamePredicate != null) {
        return typeNamePredicate;
      } else {
        return cb.conjunction();
      }
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
