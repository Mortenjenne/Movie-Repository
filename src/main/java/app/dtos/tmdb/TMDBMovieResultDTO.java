package app.dtos.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TMDBMovieResultDTO(
        @JsonProperty("page")
        int page,

        @JsonProperty("results")
        List<TMDBMovieDTO> TMDBMovieDTOS,

        @JsonProperty("total_pages")
        int totalPages,

        @JsonProperty("total_results")
        int totalResults

)
{
}
