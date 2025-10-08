package com.dofuspulse.api.gearset.dto;


import com.dofuspulse.api.constraint.ValidGearSetSlotTypeIdentifier;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EquipItemRequest(

    @NotBlank
    @ValidGearSetSlotTypeIdentifier
    String slotIdentifier,
    @NotNull(message = "itemId must not be null") Long itemId) {}