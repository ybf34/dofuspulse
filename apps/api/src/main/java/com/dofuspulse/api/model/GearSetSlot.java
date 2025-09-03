package com.dofuspulse.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "gear_set_slot",
    uniqueConstraints = @UniqueConstraint(columnNames = {"gear_set_id", "gear_set_slot_type_id"}))

public class GearSetSlot {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "gear_set_id", nullable = false)
  private GearSet gearSet;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "item_id", nullable = false)
  private ItemDetails itemDetails;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "gear_set_slot_type_id", nullable = false)
  private GearSetSlotType gearSetSlotType;
}
