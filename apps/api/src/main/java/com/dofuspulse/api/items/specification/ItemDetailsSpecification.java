package com.dofuspulse.api.items.specification;

import com.dofuspulse.api.model.ItemDetails;
import com.dofuspulse.api.model.ItemEffect;
import jakarta.persistence.criteria.Join;
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

  public static Specification<ItemDetails> hasTypesIds(List<Long> typeIds) {
    return ((root, query, cb) -> {
      if (typeIds != null && !typeIds.isEmpty()) {
        return root.get("itemTypeId").in(typeIds);
      }
      return cb.conjunction();
    });
  }

  public static Specification<ItemDetails> hasEffects(List<Long> effectIds) {
    return (root, query, cb) -> {
      if (effectIds == null || effectIds.isEmpty()) {
        return cb.conjunction();
      }

      query.distinct(true);

      Subquery<Long> subquery = query.subquery(Long.class);
      Root<ItemDetails> subRoot = subquery.from(ItemDetails.class);

      Join<ItemDetails, ItemEffect> effectsJoin = subRoot.join("possibleEffects");

      subquery.select(subRoot.get("id"))
          .where(effectsJoin.get("effect").get("id").in(effectIds))
          .groupBy(subRoot.get("id"))
          .having(cb.equal(cb.countDistinct(effectsJoin.get("effect")), effectIds.size()));

      return cb.in(root.get("id")).value(subquery);
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
}
