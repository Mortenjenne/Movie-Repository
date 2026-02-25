package app.persistence.daos;

import app.config.HibernateTestConfig;
import app.entities.Genre;
import app.entities.IEntity;
import app.entities.Person;
import app.enums.Gender;

import app.exceptions.DatabaseException;
import app.testutils.TestCleanDB;
import app.testutils.TestPopulator;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class PersonDAOTest
{
    private final EntityManagerFactory emf = HibernateTestConfig.getEntityManagerFactory();
    private Map<String, IEntity> seeded;
    private IPersonDAO personDAO;

    @BeforeEach
    void setUp()
    {
        TestCleanDB.truncateTables(emf);
        TestPopulator populator = new TestPopulator(emf);
        populator.populate();
        seeded = populator.getSeededData();
        personDAO = new PersonDAO(emf);
    }

    @Test
    @DisplayName("Create - Test creating Person")
    void create()
    {
        Person person = new Person(6L, "Viggo Mortensen", Gender.MALE);

        Person created = personDAO.create(person);

        assertThat(created.getId(), is(person.getId()));
        assertThat(created.getName(), is(person.getName()));

        Set<Person> allPersons = personDAO.getAll();
        assertThat(allPersons, hasSize(6));
    }

    @Test
    @DisplayName("Retrieve - should return all seeded persons")
    void getAll()
    {
        Set<Person> persons = personDAO.getAll();
        assertThat(persons, hasSize(5));
    }

    @Test
    @DisplayName("Update - should change name of existing Person")
    void update() {
        Person seed = (Person) seeded.get("person_charlie");
        seed.setName("Sidse Babbette Knudsen");
        seed.setGender(Gender.FEMALE);

        Person updated = personDAO.update(seed);

        assertThat(updated.getName(), is("Sidse Babbette Knudsen"));
        assertThat(updated.getGender(), is(Gender.FEMALE));

        Person fetched = personDAO.getByID(seed.getId());
        assertThat(fetched.getName(), is("Sidse Babbette Knudsen"));
        assertThat(fetched.getGender(), is(Gender.FEMALE));
    }

    @Test
    @DisplayName("Delete - should remove person and return true")
    void delete()
    {
        Person seed = (Person) seeded.get("person_sidse");
        Long id = seed.getId();

        boolean isDeleted = personDAO.delete(id);

        assertTrue(isDeleted);
        assertThrows(DatabaseException.class, () -> personDAO.getByID(id));
    }
    @Test
    @DisplayName("Delete - should throw EntityNotFoundException when deleting non-existing ID")
    void deleteNotFoundThrowsException()
    {
        assertThrows(EntityNotFoundException.class, () -> personDAO.delete(9999L));
    }

    @Test
    @DisplayName("Get by ID - should return correct Person")
    void getByID()
    {
        Person seed = (Person) seeded.get("person_mads");

        Person fetched = personDAO.getByID(seed.getId());

        assertThat(fetched.getName(), is("Mads Mikkelsen"));
        assertThat(fetched.getGender(), is(Gender.MALE));
    }
}