package com.dofuspulse.api.repository;

import com.dofuspulse.api.gearset.dto.GearSetSlotTypeIdentifier;
import com.dofuspulse.api.model.GearSetSlotType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;

public interface GearSetSlotTypeRepository extends JpaRepository<GearSetSlotType, Long> {

  @Query("""
      SELECT gst
      FROM GearSetSlotType gst
      LEFT JOIN FETCH gst.itemTypes
      WHERE gst.name = :name
      """)
  Optional<GearSetSlotType> findByName(@Param("name") @NonNull GearSetSlotTypeIdentifier slotTypeName);

}
