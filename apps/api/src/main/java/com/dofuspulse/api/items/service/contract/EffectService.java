package com.dofuspulse.api.items.service.contract;

import com.dofuspulse.api.items.dto.EffectDto;
import java.util.List;
import java.util.Optional;

public interface EffectService {

  Optional<EffectDto> findById(Long id);

  List<EffectDto> findAll();
}
