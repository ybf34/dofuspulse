package com.dofuspulse.api.repository;

import com.dofuspulse.api.model.GearSet;
import com.dofuspulse.api.model.GearSetSlot;
import com.dofuspulse.api.model.GearSetSlotType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GearSetSlotRepository extends JpaRepository<GearSetSlot, Long> {

  Optional<GearSetSlot> findByIdAndGearSetIdAndGearSetUserPrincipalId(
      Long slotId,
      Long gearSetId,
      Long userId
  );

  Optional<GearSetSlot> findFirstByGearSetAndGearSetSlotType(
      GearSet gearSet,
      GearSetSlotType gearSetSlotType
  );
}
