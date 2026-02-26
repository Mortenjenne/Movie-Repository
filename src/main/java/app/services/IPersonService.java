package app.services;

import app.dtos.PersonDTO;
import app.dtos.tmdb.TMDBMovieDetailDTO;
import app.entities.Person;
import app.enums.Gender;

import java.util.List;
import java.util.Set;

public interface IPersonService
{
    Person findOrCreate(Long id, String name, Gender gender);
    void saveAllPersons(List<TMDBMovieDetailDTO> TMDBMovieDetailDTOS);
    PersonDTO submitPerson(Person person);
    Set<Person> getAllActors(List<TMDBMovieDetailDTO> dtos);
    Set<Person> getAllDirectors(List<TMDBMovieDetailDTO> dtos);
}
