package com.dofuspulse.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "character_class")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CharacterClass {

  @Id
  private Long id;

  private String name;

}
