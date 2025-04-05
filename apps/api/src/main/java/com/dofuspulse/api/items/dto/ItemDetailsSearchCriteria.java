package com.dofuspulse.api.items.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;

@Builder
public record ItemDetailsSearchCriteria(
    @Size(max = 100) String name,
    @NotNull @Size(min = 1) List<Long> types,
    List<String> typesNames,
    Integer effect,
    Long ingredient,
    @Min(value = 1) @Max(value = 200) Long minLevel,
    @Min(value = 1) @Max(value = 200) Long maxLevel
) {}
