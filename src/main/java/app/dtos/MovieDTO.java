package app.dtos;

import app.enums.Status;

import java.time.LocalDate;

public record MovieDTO(
        Long movieId,

        String originalTitle,

        String title,

        String originCountry,

        String originalLanguage,

        String overview,

        LocalDate releaseDate,

        int runtime,

        String status,

        String tagline,

        double voteAverage
) {
}
