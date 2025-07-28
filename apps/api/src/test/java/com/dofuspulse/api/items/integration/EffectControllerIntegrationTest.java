package com.dofuspulse.api.items.integration;

import com.dofuspulse.api.PostgresIntegrationTestContainer;
import com.dofuspulse.api.items.dto.EffectDto;
import com.dofuspulse.api.model.Effect;
import com.dofuspulse.api.repository.EffectRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
public class EffectControllerIntegrationTest extends PostgresIntegrationTestContainer {

  Effect mockEffect;

  @Autowired
  MockMvcTester mockMvcTester;

  @Autowired
  EffectRepository effectRepository;

  @BeforeEach
  void setUp() {
    effectRepository.deleteAll();
    mockEffect = new Effect(1L, "effect A");
    effectRepository.save(mockEffect);
  }

  @Test
  void shouldReturnEffectByIdWith200Status() {

    mockMvcTester.get()
        .uri("/api/v1/effects/{id}", mockEffect.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200)
        .bodyJson()
        .convertTo(EffectDto.class)
        .isEqualTo(new EffectDto(mockEffect));

  }

  @Test
  void shouldReturnAllEffectsWith200Status() {

    mockMvcTester.get()
        .uri("/api/v1/effects")
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200);
  }

  @Test
  void shouldReturn404WhenEffectNotFound() {

    mockMvcTester.get()
        .uri("/api/v1/effects/{id}", 32L)
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(404);

  }

}
