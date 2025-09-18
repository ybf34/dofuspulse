package com.dofuspulse.api.model;

import com.dofuspulse.api.gearset.dto.CharacterClassName;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

  @Enumerated(value = EnumType.STRING)
  private CharacterClassName name;

}
