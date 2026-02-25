package app.services;

import app.dtos.tmdb.TMDBMovieDetailDTO;
import app.entities.Person;
import app.enums.Gender;

import java.util.List;

public interface IPersonService {
    Person findOrCreate(Long id, String name, Gender gender);

    void saveAllPersons(List<TMDBMovieDetailDTO> TMDBMovieDetailDTOS);
}
