package com.dofuspulse.api.model;

import io.hypersistence.utils.hibernate.type.array.ListArrayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "item_details")
public class ItemDetails {

  @Id
  @Column(name = "item_id")
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "icon_id")
  private Long iconId;

  private Long level;

  @Column(name = "item_type_id")
  private Long itemTypeId;

  @Type(ListArrayType.class)
  @Column(name = "quantities", columnDefinition = "integer[]")
  private List<Integer> quantities;

  @Type(ListArrayType.class)
  @Column(name = "ingredient_ids", columnDefinition = "bigint[]")
  private List<Long> ingredientIds;

  @Type(ListArrayType.class)
  @Column(name = "possibleeffects", columnDefinition = "integer[]")
  private List<Integer> possibleEffects;

}
