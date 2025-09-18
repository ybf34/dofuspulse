package com.dofuspulse.api.items.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.items.dto.EffectDto;
import com.dofuspulse.api.items.service.EffectServiceImpl;
import com.dofuspulse.api.items.service.contract.EffectService;
import com.dofuspulse.api.model.Effect;
import com.dofuspulse.api.repository.EffectRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class EffectServiceIntegrationTest extends PostgresIntegrationTestContainer {

  Effect mockEffect;

  @Autowired
  EffectRepository effectRepository;

  EffectService effectService;

  @BeforeEach
  void setUp() {
    effectRepository.deleteAll();
    mockEffect = new Effect(1L, "effect A");
    effectRepository.save(mockEffect);

    effectService = new EffectServiceImpl(effectRepository);
  }

  @Test
  void shouldReturnEffectById() {

    Optional<EffectDto> effect = effectService.findById(mockEffect.getId());

    assertThat(effect).isPresent().get()
        .extracting(EffectDto::id, EffectDto::descriptionTemplate)
        .containsExactly(mockEffect.getId(), mockEffect.getDescriptionTemplate());
  }

  @Test
  void shouldReturnAllEffects() {
    List<EffectDto> effects = effectService.findAll();

    assertThat(effects)
        .hasSize(1)
        .first()
        .usingRecursiveComparison()
        .isEqualTo(new EffectDto(mockEffect));
  }
}
