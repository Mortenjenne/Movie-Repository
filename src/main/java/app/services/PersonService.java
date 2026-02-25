package app.services;

import app.dtos.MovieDetailDTO;
import app.entities.Person;
import app.persistence.daos.IPersonDAO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PersonService
{
    private final IPersonDAO personDAO;

    public PersonService(IPersonDAO personDAO)
    {
        this.personDAO = personDAO;
    }

    public void saveAllPersons(List<MovieDetailDTO> movieDetailDTOS)
    {
        Set<Person> actors = movieDetailDTOS.stream()
                .flatMap(m -> m.creditDTO().actorsDTOs().stream())
                .filter(a -> a.role().equals("Acting"))
                .map(a -> new Person(
                        a.personId(),
                        a.name(),
                        a.getGenderEnum()))
                .collect(Collectors.toSet());

        Set<Person> directors = movieDetailDTOS.stream()
                .flatMap(m -> m.creditDTO().crewDTOs().stream())
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
