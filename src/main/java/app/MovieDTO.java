package app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties(ignoreUnknown = true)
public record MovieDTO(
        @JsonProperty("id")
        int movieId,

        @JsonProperty("original_title")
        String title,

        @JsonProperty("overview")
        String overview,

        @JsonProperty("vote_average")
        double averageRating,

        @JsonProperty("release_date")
        LocalDate releaseDate
)
{
    public String getReleaseYear()
    {
        return releaseDate.format(DateTimeFormatter.ofPattern("yyyy"));
    }
}
