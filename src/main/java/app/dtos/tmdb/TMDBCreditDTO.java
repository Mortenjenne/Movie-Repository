package app.dtos.tmdb;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TMDBCreditDTO(

        @JsonProperty("cast")
        List<TMDBPersonDTO> actorsDTOs,

        @JsonProperty("crew")
        List<TMDBCrewDTO> TMDBCrewDTOS
) {
}
