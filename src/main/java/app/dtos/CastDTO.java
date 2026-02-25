package app.dtos;

import app.enums.Role;

public record CastDTO(
        Long castId,
        String characterName,
        Role movieRole,
        PersonDTO personDTO
) {
}
