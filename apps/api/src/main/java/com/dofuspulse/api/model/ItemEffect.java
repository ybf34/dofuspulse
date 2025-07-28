package com.dofuspulse.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "item_effect")
public class ItemEffect {

  @Id
  private Long id;

  @ManyToOne
  @JoinColumn(name = "item_id")
  private ItemDetails itemDetails;

  @ManyToOne
  @JoinColumn(name = "effect_id")
  private Effect effect;

  @Column(name = "min_value")
  private Integer minValue;

  @Column(name = "max_value")
  private Integer maxValue;
}
