package com.dofuspulse.api.model;

import com.dofuspulse.api.gearset.dto.GearSetSlotTypeIdentifier;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "gear_set_slot_type")
public class GearSetSlotType {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(value = EnumType.STRING)
  private GearSetSlotTypeIdentifier name;

  @ManyToMany
  @JoinTable(name = "slot_type_item_types", joinColumns = @JoinColumn(name = "slot_type_id"),
      inverseJoinColumns = @JoinColumn(name = "item_type_id")
  )
  private List<ItemType> itemTypes = new ArrayList<>();
}