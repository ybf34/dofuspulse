package com.dofuspulse.api.items.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Builder;

@Builder
public record ItemDetailsSearchCriteria(
    @Size(max = 100) String name,
    List<Long> typesIds,
    Integer effect,
    Long ingredient,
    @Min(value = 1) @Max(value = 200) Long minLevel,
    @Min(value = 1) @Max(value = 200) Long maxLevel
) {}
