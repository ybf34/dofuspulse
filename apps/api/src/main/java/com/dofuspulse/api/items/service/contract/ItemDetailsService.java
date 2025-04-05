package com.dofuspulse.api.items.service.contract;

import com.dofuspulse.api.items.dto.ItemDetailsDto;
import com.dofuspulse.api.items.dto.ItemDetailsSearchCriteria;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemDetailsService {

  Optional<ItemDetailsDto> findById(Long id);

  Page<ItemDetailsDto> findAll(ItemDetailsSearchCriteria params, Pageable pageable);
}
