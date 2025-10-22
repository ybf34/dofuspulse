package com.dofuspulse.api.repository;


import com.dofuspulse.api.model.GearSet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GearSetRepository extends JpaRepository<GearSet, Long> {

  List<GearSet> findByUserPrincipalIdOrderByUpdatedAtDesc(Long userId);
}
