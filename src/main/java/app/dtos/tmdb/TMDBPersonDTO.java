package app.dtos.tmdb;

import app.enums.Gender;
import app.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TMDBPersonDTO(
        @JsonProperty("id")
        Long personId,

        @JsonProperty("gender")
        int gender,

        @JsonProperty("known_for_department")
        String role,

        @JsonProperty("name")
        String name,

        @JsonProperty("original_name")
        String originalName,

        @JsonProperty("cast_id")
        int castId,

        @JsonProperty("character")
        String characterName
)
{
    public Gender getGenderEnum() // TODO Rename
    {
        return Gender.fromValue(gender);
    }

    public Role getRoleEnum() // TODO Rename
    {
        return Role.fromValue(role);
    }
}
