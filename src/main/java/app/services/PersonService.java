package app.services;

import app.dtos.tmdb.TMDBMovieDetailDTO;
import app.entities.Person;
import app.enums.Gender;
import app.persistence.IPersonDAO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PersonService implements IPersonService {
    private final IPersonDAO personDAO;

    public PersonService(IPersonDAO personDAO)
    {
        this.personDAO = personDAO;
    }

    @Override
    public Person findOrCreate(Long id, String name, Gender gender) {

        Person existing = personDAO.findById(id);

        if (existing != null) {
            return existing;
        }

        Person person = new Person(id, name, gender);
        return personDAO.create(person);
    }

    @Override
    public void saveAllPersons(List<TMDBMovieDetailDTO> TMDBMovieDetailDTOS)
    {
        Set<Person> actors = TMDBMovieDetailDTOS.stream()
                .flatMap(m -> m.TMDBCreditDTO().actorsDTOs().stream())
                .filter(a -> a.role().equals("Acting"))
                .map(a -> new Person(
                        a.personId(),
                        a.name(),
                        a.getGenderEnum()))
                .collect(Collectors.toSet());

        Set<Person> directors = TMDBMovieDetailDTOS.stream()
                .flatMap(m -> m.TMDBCreditDTO().TMDBCrewDTOS().stream())
                .filter(d -> d.job().equals("Director") || d.department().equals("Directing"))
                .map(d -> new Person(
                        d.personId(),
                        d.name(),
                        d.getGenderEnum()))
                .collect(Collectors.toSet());

        Set<Person> casts = new HashSet<>();
        casts.addAll(actors);
        casts.addAll(directors);

        casts.forEach(person ->
        {
            personDAO.create(person);
        });
    }
}
