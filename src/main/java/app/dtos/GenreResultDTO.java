package app.dtos;

import app.entities.Genre;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GenreResultDTO(
        @JsonProperty("genres")
        List<Genre> genres
)
{
}
