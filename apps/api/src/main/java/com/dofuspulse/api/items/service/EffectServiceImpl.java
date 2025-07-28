package com.dofuspulse.api.items.service;

import com.dofuspulse.api.items.dto.EffectDto;
import com.dofuspulse.api.items.service.contract.EffectService;
import com.dofuspulse.api.repository.EffectRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EffectServiceImpl implements EffectService {

  private final EffectRepository effectRepository;

  @Override
  @Transactional(readOnly = true)
  public Optional<EffectDto> findById(Long id) {
    return effectRepository.findById(id).map(EffectDto::new);
  }

  @Override
  public List<EffectDto> findAll() {
    return effectRepository.findAll().stream()
        .map(EffectDto::new)
        .toList();
  }
}
