package com.dofuspulse.api.items.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.items.dto.EffectDto;
import com.dofuspulse.api.items.service.EffectServiceImpl;
import com.dofuspulse.api.model.Effect;
import com.dofuspulse.api.repository.EffectRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EffectServiceUnitTest {

  Effect mockEffect;

  @Mock
  EffectRepository effectRepository;

  @InjectMocks
  EffectServiceImpl effectService;

  @BeforeEach
  void setUp() {
    mockEffect = new Effect(1L, "effect A");
  }

  @Test
  void shouldReturnEffectById() {

    when(effectRepository.findById(mockEffect.getId())).thenReturn(Optional.of(mockEffect));

    Optional<EffectDto> effect = effectService.findById(mockEffect.getId());

    assertThat(effect).isPresent().get()
        .extracting(EffectDto::id, EffectDto::descriptionTemplate)
        .containsExactly(mockEffect.getId(), mockEffect.getDescriptionTemplate());

    verify(effectRepository, times(1)).findById(mockEffect.getId());
  }

  @Test
  void shouldReturnAllEffects() {

    when(effectRepository.findAll()).thenReturn(List.of(mockEffect));

    List<EffectDto> effects = effectService.findAll();

    assertThat(effects)
        .hasSize(1)
        .first()
        .usingRecursiveComparison()
        .isEqualTo(new EffectDto(mockEffect));

    verify(effectRepository, times(1)).findAll();
  }
}
