package com.dofuspulse.api.gearset.dto;

import com.dofuspulse.api.constraint.ValidCharacterClassName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;


public record CreateGearSetRequest(

    @NotBlank String title,

    @NotBlank
    @ValidCharacterClassName
    String characterClass,

    @NotBlank
    @Pattern(regexp = "^[mf]$", message = "Gender must be 'm' or 'f'")
    String characterGender,

    @Size(max = 10, message = "Cannot have more than 10 tags")
    List<
        @NotBlank
        @Size(min = 1, max = 15, message = "Each tag must be between 1 and 15 characters long.")
        @Pattern(regexp = "^[a-z0-9- ]+$", message = "Tags must be alphanumeric.")
            String> tags
) {}