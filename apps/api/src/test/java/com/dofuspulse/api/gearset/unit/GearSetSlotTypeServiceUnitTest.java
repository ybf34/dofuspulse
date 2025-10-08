package com.dofuspulse.api.gearset.unit;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.dofuspulse.api.gearset.dto.GearSetSlotTypeDto;
import com.dofuspulse.api.gearset.dto.GearSetSlotTypeIdentifier;
import com.dofuspulse.api.gearset.fixtures.GearSetTestDataFactory;
import com.dofuspulse.api.gearset.service.GearSetSlotTypeServiceImpl;
import com.dofuspulse.api.model.GearSetSlotType;
import com.dofuspulse.api.model.ItemType;
import com.dofuspulse.api.repository.GearSetSlotTypeRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GearSetSlotTypeServiceUnitTest {

  GearSetSlotType mockSlotType;

  @Mock
  GearSetSlotTypeRepository gearSetSlotTypeRepository;

  @InjectMocks
  GearSetSlotTypeServiceImpl gearSetSlotService;

  @BeforeEach
  void setUp() {
    ItemType itemType = new ItemType(1L, "AMULET");
    mockSlotType = GearSetTestDataFactory.createMockSlotType(GearSetSlotTypeIdentifier.AMULET, List.of(itemType));
  }


  @Test
  void shouldReturnAllSlotTypesWith200Success() {

    when(gearSetSlotTypeRepository.findAll()).thenReturn(List.of(mockSlotType));

    List<GearSetSlotTypeDto> slots = gearSetSlotService.findSlotTypes();

    assertThat(slots)
        .hasSize(1)
        .first()
        .usingRecursiveComparison()
        .isEqualTo(new GearSetSlotTypeDto(mockSlotType));

    verify(gearSetSlotTypeRepository, times(1)).findAll();
  }

}
