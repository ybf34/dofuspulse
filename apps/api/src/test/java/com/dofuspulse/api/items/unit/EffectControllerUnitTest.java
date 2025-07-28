package com.dofuspulse.api.items.unit;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.items.controller.EffectController;
import com.dofuspulse.api.items.dto.EffectDto;
import com.dofuspulse.api.items.service.EffectServiceImpl;
import com.dofuspulse.api.security.CustomAccessDeniedHandler;
import com.dofuspulse.api.security.UnauthorizedHandler;
import com.dofuspulse.api.security.WebSecurityConfig;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(EffectController.class)
@WithMockUser
@Import({WebSecurityConfig.class, UnauthorizedHandler.class, CustomAccessDeniedHandler.class})
public class EffectControllerUnitTest {

  EffectDto mockEffectDto;

  @Autowired
  MockMvcTester mockMvcTester;

  @MockitoBean
  EffectServiceImpl effectService;

  @BeforeEach
  void setUp() {
    mockEffectDto = new EffectDto(1L, "effect A");
  }

  @Test
  void shouldReturnEffectByIdWith200Status() {

    when(effectService.findById(mockEffectDto.id())).thenReturn(Optional.of(mockEffectDto));

    mockMvcTester.get()
        .uri("/api/v1/effects/{id}", mockEffectDto.id())
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200)
        .bodyJson()
        .convertTo(EffectDto.class)
        .isEqualTo(mockEffectDto);

    verify(effectService, times(1)).findById(mockEffectDto.id());
  }

  @Test
  void shouldReturnAllEffectsWith200Status() {

    when(effectService.findAll()).thenReturn(List.of(mockEffectDto));

    mockMvcTester.get()
        .uri("/api/v1/effects")
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200);

    verify(effectService, times(1)).findAll();
  }

}
