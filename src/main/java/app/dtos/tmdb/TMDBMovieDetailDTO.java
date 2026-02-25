package app.dtos.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TMDBMovieDetailDTO(
        @JsonProperty("id")
        Long id,

        @JsonProperty("genres")
        List<TMDBGenreDTO> genres,

        @JsonProperty("origin_country")
        List<String> originCountries,

        @JsonProperty("original_language")
        String originalLanguage,

        @JsonProperty("original_title")
        String originalTitle,

        @JsonProperty("overview")
        String overview,

        @JsonProperty("release_date")
        LocalDate releaseDate,

        @JsonProperty("runtime")
        int runtime,

        @JsonProperty("status")
        String status,

        @JsonProperty("tagline")
        String tagline,

        @JsonProperty("title")
        String title,

        @JsonProperty("vote_average")
        double voteAverage,

        @JsonProperty("credits")
        TMDBCreditDTO TMDBCreditDTO
        )
{
}
