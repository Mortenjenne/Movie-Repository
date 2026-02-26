package app.services;

import app.dtos.PersonDTO;
import app.dtos.tmdb.TMDBMovieDetailDTO;
import app.entities.Genre;
import app.entities.Person;
import app.enums.Gender;
import app.persistence.IPersonDAO;
import app.utils.DTOMapper;

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
    public PersonDTO submitPerson(Person person)
    {
        validateNotNull(person);
        validateId(person.getId());

        boolean exists = personDAO.existsById(person.getId());

        Person result;
        if(exists)
        {
            result = personDAO.update(person);
        }
        else
        {
            result = personDAO.create(person);
        }

        return DTOMapper.mapPersonToDTO(result);
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
        Set<Person> casts = new HashSet<>();
        casts.addAll(getAllActors(TMDBMovieDetailDTOS));
        casts.addAll(getAllDirectors(TMDBMovieDetailDTOS));

        casts.forEach(person ->
        {
            boolean exists = personDAO.existsById(person.getId());

            if(exists)
            {
                personDAO.update(person);
            }
            else
            {
                personDAO.create(person);
            }
        });
    }

    @Override
    public Set<Person> getAllActors(List<TMDBMovieDetailDTO> dtos)
    {
        return dtos.stream()
                .flatMap(m -> m.TMDBCreditDTO().actorsDTOs().stream())
                .filter(a -> a.role().equals("Acting"))
                .map(a -> new Person(
                        a.personId(),
                        a.name(),
                        a.getGenderEnum()))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Person> getAllDirectors(List<TMDBMovieDetailDTO> dtos)
    {
        return dtos.stream()
                .flatMap(m -> m.TMDBCreditDTO().TMDBCrewDTOS().stream())
                .filter(d -> d.job().equals("Director") || d.department().equals("Directing"))
                .map(d -> new Person(
                        d.personId(),
                        d.name(),
                        d.getGenderEnum()))
                .collect(Collectors.toSet());
    }

    private void validateNotNull(Object exists)
    {
        if(exists == null)
        {
            throw new IllegalArgumentException("Movie cant be null");
        }
    }

    private void validateId(Long id)
    {
        if (id == null || id <= 0)
        {
            throw new IllegalArgumentException("Invalid ID: Must be provided and greater than 0.");
        }
    }
}
