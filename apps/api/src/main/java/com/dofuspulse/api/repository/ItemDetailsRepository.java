package com.dofuspulse.api.repository;

import com.dofuspulse.api.model.ItemDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ItemDetailsRepository extends JpaRepository<ItemDetails, Long>,
    JpaSpecificationExecutor<ItemDetails> {

}