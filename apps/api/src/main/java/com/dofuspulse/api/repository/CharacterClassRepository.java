package com.dofuspulse.api.repository;

import com.dofuspulse.api.gearset.dto.CharacterClassName;
import com.dofuspulse.api.model.CharacterClass;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterClassRepository extends JpaRepository<CharacterClass, Long> {

  Optional<CharacterClass> findByName(CharacterClassName name);
}
