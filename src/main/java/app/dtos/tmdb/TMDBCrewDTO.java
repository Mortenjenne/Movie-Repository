package app.dtos.tmdb;

import app.enums.Gender;
import app.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TMDBCrewDTO(
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

        @JsonProperty("department")
        String department,

        @JsonProperty("job")
        String job
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
