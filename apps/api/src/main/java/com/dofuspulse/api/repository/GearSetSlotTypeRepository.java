package com.dofuspulse.api.repository;

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
      WHERE gst.id = :id
      """)
  Optional<GearSetSlotType> findById(@Param("id") @NonNull Long id);
}
