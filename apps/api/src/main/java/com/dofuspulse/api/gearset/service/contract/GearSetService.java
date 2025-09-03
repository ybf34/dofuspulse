package com.dofuspulse.api.gearset.service.contract;

import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.gearset.dto.CreateGearSetRequest;
import com.dofuspulse.api.gearset.dto.GearSetDto;
import java.util.List;
import java.util.Optional;

public interface GearSetService {

  Optional<GearSetDto> findById(Long id);

  List<GearSetDto> findUserGearSets(UserPrincipal user);

  GearSetDto createGearSet(CreateGearSetRequest gearSetRequest, UserPrincipal user);

  void deleteGearSet(Long id, UserPrincipal userPrincipal);

}
