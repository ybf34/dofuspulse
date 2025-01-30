package com.dofuspulse.api.repository;

import com.dofuspulse.api.model.ItemDetails;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemDetailsRepository extends JpaRepository<ItemDetails, Long> {

  <T> List<T> findAllByItemTypeId(Long itemTypeId, Class<T> type);

}