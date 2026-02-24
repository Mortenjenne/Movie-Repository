package app.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CreditDTO(

        @JsonProperty("cast")
        List<PersonDTO> actorsDTOs,

        @JsonProperty("crew")
        List<PersonDTO> crewDTOs
) {
}
