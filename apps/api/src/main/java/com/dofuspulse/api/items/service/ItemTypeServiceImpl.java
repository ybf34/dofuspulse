package com.dofuspulse.api.items.service;

import com.dofuspulse.api.items.dto.ItemTypeDto;
import com.dofuspulse.api.items.service.contract.ItemTypeService;
import com.dofuspulse.api.repository.ItemTypeRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemTypeServiceImpl implements ItemTypeService {

  private final ItemTypeRepository itemTypeRepository;

  @Override
  @Transactional(readOnly = true)
  public Optional<ItemTypeDto> getItemTypeById(Long id) {
    return itemTypeRepository.findById(id)
        .map(ItemTypeDto::new);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ItemTypeDto> getItemTypes(Pageable pageable) {
    return itemTypeRepository.findAll(pageable)
        .map(ItemTypeDto::new);
  }
}
