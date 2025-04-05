package com.dofuspulse.api.items.service;

import com.dofuspulse.api.items.dto.ItemDetailsDto;
import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import com.dofuspulse.api.items.service.contract.ItemDetailsService;
import com.dofuspulse.api.items.specification.ItemDetailsSpecificationBuilder;
import com.dofuspulse.api.repository.ItemDetailsRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ItemDetailsServiceImpl implements ItemDetailsService {

  private final ItemDetailsRepository itemDetailsRepository;

  @Override
  @Transactional(readOnly = true)
  public Optional<ItemDetailsDto> findById(Long id) {
    return itemDetailsRepository.findById(id)
        .map(ItemDetailsDto::new);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<ItemDetailsDto> findAll(ItemDetailsSearchCriteria params, Pageable pageable) {
    return itemDetailsRepository.findAll(
            ItemDetailsSpecificationBuilder.buildSpecification(params), pageable)
        .map(ItemDetailsDto::new);
  }
}
