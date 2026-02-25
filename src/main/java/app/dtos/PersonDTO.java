package app.dtos;

import app.enums.Gender;

public record PersonDTO(
        Long id,
        String name,
        Gender gender
) {
}
