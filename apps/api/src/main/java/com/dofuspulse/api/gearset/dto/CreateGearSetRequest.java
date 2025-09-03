package com.dofuspulse.api.gearset.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;


public record CreateGearSetRequest(
    @NotBlank String title,

    @NotBlank String characterClass,

    @NotBlank
    @Pattern(regexp = "^[mf]$", message = "Gender must be 'm' or 'f'")
    String characterGender,

    @Size(max = 10, message = "Cannot have more than 10 tags")
    List<
        @NotBlank
        @Size(min = 1, max = 20)
        @Pattern(regexp = "^[a-z0-9-]+$")
            String> tags
) {}