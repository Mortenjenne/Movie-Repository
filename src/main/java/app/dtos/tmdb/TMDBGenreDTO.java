package app.dtos.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TMDBGenreDTO(
        @JsonProperty("id")
        Long movieId,

        @JsonProperty("name")
        String name
)
{
}
