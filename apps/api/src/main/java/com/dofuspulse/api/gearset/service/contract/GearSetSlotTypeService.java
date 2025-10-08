package com.dofuspulse.api.gearset.service.contract;

import com.dofuspulse.api.gearset.dto.GearSetSlotTypeDto;
import java.util.List;

public interface GearSetSlotTypeService {

  List<GearSetSlotTypeDto> findSlotTypes();
}
