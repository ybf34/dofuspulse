package com.dofuspulse.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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

  @Column(name = "quantities", columnDefinition = "integer[]")
  private List<Integer> quantities;

  @Column(name = "ingredient_ids")
  private List<Long> ingredientIds;

  @Column(name = "possibleeffects", columnDefinition = "integer[]")
  private List<Integer> possibleEffects;

}
