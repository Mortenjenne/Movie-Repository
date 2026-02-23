package app.persistence;

import app.config.HibernateTestConfig;
import app.entities.Genre;
import app.entities.IEntity;
import app.entities.Movie;
import app.persistence.daos.IMovieDAO;
import app.testutils.TestCleanDB;
import app.testutils.TestPopulator;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

class MovieDAOTest
{

    private final EntityManagerFactory emf = HibernateTestConfig.getEntityManagerFactory();
    private  Map<String, IEntity> seeded;
    private IMovieDAO movieDAO;

    @BeforeEach
    void setUp()
    {
        TestCleanDB.truncateTables(emf);
        TestPopulator populator = new TestPopulator(emf);
        populator.populate();
        seeded = populator.getSeededData();
        movieDAO = new MovieDAO(emf);
    }

    @Test
    @DisplayName("Create - Test creating movie")
    void create()
    {
        Genre drama = (Genre) seeded.get("genre_drama");
        Genre romantic = (Genre) seeded.get("genre_drama");
        Set<Genre> genres = new HashSet<>();
        genres.add(drama);
        genres.add(romantic);

        Movie movie = new Movie(
                "Festen",
                "The Celebration",
                "En familie samles for at fejre patriarkens 60 års fødselsdag, hvor sønnen Christian afslører en ødelæggende hemmelighed.",
                LocalDate.of(1998, 6, 19),
                "da",
                8.0,
                "Alle familier har hemmeligheder.",
                "Released",
                genres
        );

        Movie created = movieDAO.create(movie);


        assertThat(created.getId(), notNullValue());
        assertEquals("Festen", created.getTitle());
        assertTrue(created.getGenres().contains(drama));
    }

    @Test
    @DisplayName("Create - should throw exception when movie is null")
    void createNullThrowsException()
    {
        assertThrows(IllegalArgumentException.class, () -> movieDAO.create(null));
    }

    @Test
    void getAll() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void getByID() {
    }
}