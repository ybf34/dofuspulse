package com.dofuspulse.api.items.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.model.Effect;
import com.dofuspulse.api.repository.EffectRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class EffectRepositoryIntegrationTest extends PostgresIntegrationTestContainer {

  Effect mockEffect;

  @Autowired
  EffectRepository effectRepository;

  @BeforeEach
  void setUp() {
    effectRepository.deleteAll();
    mockEffect = new Effect(1L, "effect A");
    effectRepository.save(mockEffect);
  }

  @Test
  void shouldReturnEffectById() {

    Optional<Effect> effect = effectRepository.findById(mockEffect.getId());

    assertThat(effect).isPresent().get()
        .extracting(Effect::getId, Effect::getDescriptionTemplate)
        .containsExactly(mockEffect.getId(), mockEffect.getDescriptionTemplate());
  }

  @Test
  void shouldReturnAllEffects() {
    List<Effect> effects = effectRepository.findAll();

    assertThat(effects)
        .hasSize(1)
        .first()
        .usingRecursiveComparison()
        .isEqualTo(mockEffect);
  }
}
