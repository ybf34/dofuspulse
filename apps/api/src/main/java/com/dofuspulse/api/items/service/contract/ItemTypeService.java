package com.dofuspulse.api.items.service.contract;

import com.dofuspulse.api.items.dto.ItemTypeDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemTypeService {

  Optional<ItemTypeDto> getItemTypeById(Long id);

  Page<ItemTypeDto> getItemTypes(Pageable pageable);
}
