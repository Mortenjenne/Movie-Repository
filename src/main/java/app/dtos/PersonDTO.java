package app.dtos;

import app.enums.Gender;

public record PersonDTO(
        String name,
        Gender gender
) {
}
