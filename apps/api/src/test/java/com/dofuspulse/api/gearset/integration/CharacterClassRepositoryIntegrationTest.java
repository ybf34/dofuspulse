package com.dofuspulse.api.gearset.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.model.CharacterClass;
import com.dofuspulse.api.repository.CharacterClassRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class CharacterClassRepositoryIntegrationTest extends PostgresIntegrationTestContainer {

  CharacterClass mockCharacterClass;

  @Autowired
  CharacterClassRepository characterClassRepository;

  @BeforeEach
  void setUp() {
    characterClassRepository.deleteAll();
    mockCharacterClass = new CharacterClass(1L, "cra");
    characterClassRepository.save(mockCharacterClass);
  }

  @Test
  void shouldReturnCharacterClassByName() {

    Optional<CharacterClass> characterClass = characterClassRepository.findByName(
        mockCharacterClass.getName());

    assertThat(characterClass)
        .isPresent()
        .get()
        .usingRecursiveComparison()
        .isEqualTo(mockCharacterClass);
  }
}
