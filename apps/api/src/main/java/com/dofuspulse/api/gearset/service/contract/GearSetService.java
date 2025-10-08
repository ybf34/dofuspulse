package com.dofuspulse.api.gearset.service.contract;

import com.dofuspulse.api.auth.UserPrincipal;
import com.dofuspulse.api.gearset.dto.CreateGearSetRequest;
import com.dofuspulse.api.gearset.dto.GearSetDto;
import com.dofuspulse.api.gearset.dto.UpdateGearSetRequest;
import java.util.List;

public interface GearSetService {

  GearSetDto findById(Long id, UserPrincipal user);

  List<GearSetDto> findUserGearSets(UserPrincipal user);

  GearSetDto createGearSet(CreateGearSetRequest gearSetRequest, UserPrincipal user);

  GearSetDto updateGearSet(Long id, UpdateGearSetRequest updateGearSetRequest, UserPrincipal user);

  void deleteGearSet(Long id, UserPrincipal userPrincipal);

}
