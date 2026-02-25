package app.dtos;

import java.util.List;

public record MovieFullDetailDTO(
        MovieDTO movieDTO,
        List<CastDTO> castDTOS,
        List<GenreDTO> genreDTOS
) {
}
