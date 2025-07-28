package com.dofuspulse.api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Effect {

  @Id
  @Column(name = "effect_id")
  private Long id;

  @Column(name = "description_template")
  private String descriptionTemplate;
}
