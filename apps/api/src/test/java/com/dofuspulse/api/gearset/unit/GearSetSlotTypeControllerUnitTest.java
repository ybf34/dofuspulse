package com.dofuspulse.api.gearset.unit;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.gearset.controller.GearSetSlotTypeController;
import com.dofuspulse.api.gearset.dto.GearSetSlotTypeDto;
import com.dofuspulse.api.gearset.dto.GearSetSlotTypeIdentifier;
import com.dofuspulse.api.gearset.fixtures.GearSetTestDataFactory;
import com.dofuspulse.api.gearset.service.contract.GearSetSlotTypeService;
import com.dofuspulse.api.model.GearSetSlotType;
import com.dofuspulse.api.model.ItemType;
import com.dofuspulse.api.security.CustomAccessDeniedHandler;
import com.dofuspulse.api.security.UnauthorizedHandler;
import com.dofuspulse.api.security.WebSecurityConfig;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(GearSetSlotTypeController.class)
@Import({WebSecurityConfig.class, UnauthorizedHandler.class, CustomAccessDeniedHandler.class})
public class GearSetSlotTypeControllerUnitTest {

  GearSetSlotTypeDto mockSlotType;

  @Autowired
  MockMvcTester mvcTester;

  @MockitoBean
  GearSetSlotTypeService gearSetSlotTypeService;

  @BeforeEach
  void setUp() {
    ItemType itemType = new ItemType(1L, "AMULET");
    GearSetSlotType slotType = GearSetTestDataFactory.createMockSlotType(GearSetSlotTypeIdentifier.AMULET,
        List.of(itemType));
    mockSlotType = new GearSetSlotTypeDto(slotType);
  }

  @Test
  @WithMockUser
  void shouldReturnAllSlotTypesWith200Success() {

    when(gearSetSlotTypeService.findSlotTypes()).thenReturn(List.of(mockSlotType));

    mvcTester.get()
        .uri("/api/v1/gearset-slot-types")
        .contentType(MediaType.APPLICATION_JSON)
        .assertThat()
        .hasStatus(200)
        .bodyJson()
        .hasPath("$[0].id")
        .hasPath("$[0].name")
        .hasPath("$[0].itemTypes");

    verify(gearSetSlotTypeService, times(1)).findSlotTypes();
  }

}
