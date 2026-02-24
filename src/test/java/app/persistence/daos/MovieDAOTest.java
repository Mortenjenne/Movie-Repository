package app.persistence.daos;

import app.config.HibernateTestConfig;
import app.entities.*;
import app.enums.Role;
import app.persistence.MovieDAO;
import app.testutils.TestCleanDB;
import app.testutils.TestPopulator;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
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
    @DisplayName("Create - Should persist a movie")
    void create()
    {
        Genre drama = (Genre) seeded.get("genre_drama");
        Genre romantic = (Genre) seeded.get("genre_drama");
        Set<Genre> genres = new HashSet<>();
        genres.add(drama);
        genres.add(romantic);

        Movie movie = new Movie(
                6L,
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
    @DisplayName("Retrieve - should return all seeded movies")
    void getAll()
    {
        Set<Movie> movies = movieDAO.getAll();
        assertThat(movies, hasSize(5));
    }

    @Test
    @DisplayName("Update - should change title of existing Movie")
    void update()
    {
        Movie seed = (Movie) seeded.get("movie_platoon");
        seed.setTitle("Rambo");

        Movie updated = movieDAO.update(seed);

        assertThat(updated.getTitle(), is("Rambo"));

        Movie fetched = movieDAO.getByID(seed.getId());
        assertThat(fetched.getTitle(), is("Rambo"));
    }

    @Test
    @DisplayName("Update - should persist newly added cast member")
    void addCastToMovie()
    {
        Movie movie = (Movie) seeded.get("movie_druk");
        Person charlie = (Person) seeded.get("person_charlie");

        movie.addCast(new Cast("New Role", Role.ACTOR, charlie));

        movieDAO.update(movie);

        Movie updated = movieDAO.getByID(movie.getId());

        assertThat(updated.getCast(), hasSize(3));

        Movie fetched = movieDAO.getByID(movie.getId());
        assertThat(fetched.getCast(), hasSize(3));
        assertThat(
                fetched.getCast()
                        .stream()
                        .map(c -> c.getPerson().getName())
                        .toList(),
                containsInAnyOrder("Mads Mikkelsen", "Thomas Vinterberg", "Charlie Sheen")
        );
    }

    @Test
    @DisplayName("Update - should remove cast member from database")
    void removeCastFromMovie()
    {
        Movie movie = (Movie) seeded.get("movie_platoon");

        Cast castMember = movie.getCast().iterator().next();
        movie.removeCast(castMember);

        movieDAO.update(movie);

        Movie updated = movieDAO.getByID(movie.getId());

        assertThat(updated.getCast(), hasSize(1));
    }

    @Test
    @DisplayName("Delete - should remove Movie and return true")
    void delete()
    {
        Movie seed = (Movie) seeded.get("movie_platoon");
        Long id = seed.getId();

        boolean isDeleted = movieDAO.delete(id);

        assertTrue(isDeleted);
        assertThrows(EntityNotFoundException.class, () -> movieDAO.getByID(id));
    }

    @Test
    @DisplayName("Delete - should throw EntityNotFoundException when deleting non-existing ID")
    void deleteNotFoundThrowsException()
    {
        assertThrows(EntityNotFoundException.class, () -> movieDAO.delete(9999L));
    }

    @Test
    @DisplayName("Get by ID - should return correct Movie")
    void getByID()
    {
        Movie seed = (Movie) seeded.get("movie_platoon");
        Movie fetched = movieDAO.getByID(seed.getId());

        assertThat(fetched.getId(), is(seed.getId()));
        assertThat(fetched.getTitle(), is(seed.getTitle()));
        assertThat(fetched.getLanguage(), is(seed.getLanguage()));
        assertThat(fetched.getRating(), is(seed.getRating()));
        assertThat(fetched.getOriginalTitle(), is(seed.getOriginalTitle()));
        assertThat(fetched.getCast(), hasSize(2));
    }

    @Test
    @DisplayName("Get by ID - should throw EntityNotFoundException for non-existing ID")
    void getByIDNotFoundThrowsException()
    {
        assertThrows(EntityNotFoundException.class, () -> movieDAO.getByID(999999L));
    }
}