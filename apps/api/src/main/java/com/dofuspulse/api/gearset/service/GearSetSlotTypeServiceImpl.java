package com.dofuspulse.api.gearset.service;

import com.dofuspulse.api.gearset.dto.GearSetSlotTypeDto;
import com.dofuspulse.api.gearset.service.contract.GearSetSlotTypeService;
import com.dofuspulse.api.repository.GearSetSlotTypeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GearSetSlotTypeServiceImpl implements GearSetSlotTypeService {

  private final GearSetSlotTypeRepository gearSetSlotTypeRepository;

  @Override
  @Transactional(readOnly = true)
  public List<GearSetSlotTypeDto> findSlotTypes() {
    return gearSetSlotTypeRepository.findAll()
        .stream()
        .map(GearSetSlotTypeDto::new)
        .toList();
  }
}
