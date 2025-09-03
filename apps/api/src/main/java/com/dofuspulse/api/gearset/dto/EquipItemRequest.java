package com.dofuspulse.api.gearset.dto;

import jakarta.validation.constraints.NotNull;

public record EquipItemRequest(
    @NotNull(message = "slotTypeId must not be null") Long slotTypeId,
    @NotNull(message = "itemId must not be null") Long itemId) {}