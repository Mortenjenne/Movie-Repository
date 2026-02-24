package app.persistence.daos;

import app.config.HibernateTestConfig;
import app.entities.Genre;
import app.entities.IEntity;
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

class GenreDAOTest
{
    private final EntityManagerFactory emf = HibernateTestConfig.getEntityManagerFactory();
    private Map<String, IEntity> seeded;
    private IGenreDAO genreDAO;

    @BeforeEach
    void setUp()
    {
        TestCleanDB.truncateTables(emf);
        TestPopulator populator = new TestPopulator(emf);
        populator.populate();
        seeded = populator.getSeededData();
        genreDAO = new GenreDAO(emf);
    }

    @Test
    @DisplayName("Create - Should persist a genre")
    void create()
    {
        Genre genre = new Genre(24L, "Action");

        Genre created = genreDAO.create(genre);

        assertThat(created.getId(), is(genre.getId()));
        assertThat(created.getName(), is(genre.getName()));

        Set<Genre> allGenres = genreDAO.getAll();
        assertThat(allGenres, hasSize(4));
    }

    @Test
    @DisplayName("Retrieve - should return all seeded movies")
    void getAll()
    {
        Set<Genre> genres = genreDAO.getAll();
        assertThat(genres, hasSize(3));
    }

    @Test
    @DisplayName("Update - should change name of existing Genre")
    void update()
    {
        Genre seed = (Genre) seeded.get("genre_drama");
        seed.setName("drama queen");

        Genre updated = genreDAO.update(seed);

        assertThat(updated.getName(), is("drama queen"));

        Genre fetched = genreDAO.getByID(seed.getId());
        assertThat(fetched.getName(), is("drama queen"));
    }

    @Test
    @DisplayName("Delete - should remove genre and return true")
    void delete()
    {
        Genre seed = (Genre) seeded.get("genre_thriller");
        Long id = seed.getId();

        boolean isDeleted = genreDAO.delete(id);

        assertTrue(isDeleted);
        assertThrows(DatabaseException.class, () -> genreDAO.getByID(id));
    }

    @Test
    @DisplayName("Delete - should throw EntityNotFoundException when deleting non-existing ID")
    void deleteNotFoundThrowsException()
    {
        assertThrows(EntityNotFoundException.class, () -> genreDAO.delete(9999L));
    }

    @Test
    @DisplayName("Get by ID - should return correct Genre")
    void getByID()
    {
        Genre seed = (Genre) seeded.get("genre_drama");
        Genre fetched = genreDAO.getByID(seed.getId());

        assertThat(fetched.getId(), is(seed.getId()));
        assertThat(fetched.getName(), is(seed.getName()));
    }
}