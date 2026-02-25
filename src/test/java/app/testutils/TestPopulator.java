package app.testutils;

import app.entities.*;
import app.enums.Gender;
import app.enums.Role;
import app.persistence.MovieDAO;
import app.persistence.daos.*;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class TestPopulator
{
    private final IMovieDAO movieDAO;
    private final IPersonDAO personDAO;
    private final IGenreDAO genreDAO;
    private final Map<String, IEntity> seeded;

    public TestPopulator(EntityManagerFactory emf)
    {
        this.movieDAO = new MovieDAO(emf);
        this.personDAO = new PersonDAO(emf);
        this.genreDAO = new GenreDAO(emf);
        this.seeded = new HashMap<>();
    }

    public Map<String, IEntity> getSeededData()
    {
        return seeded;
    }

    public void populate()
    {
        populatePersons();
        populateGenres();
        populateMovies();
    }

    private void populateGenres()
    {
        Genre drama = new Genre(1L,"Drama");
        Genre romantic = new Genre(2L,"Romantic");
        Genre thriller = new Genre(3L, "Thriller");

        seeded.put("genre_drama", genreDAO.create(drama));
        seeded.put("genre_romantic", genreDAO.create(romantic));
        seeded.put("genre_thriller", genreDAO.create(thriller));
    }

    private void populatePersons()
    {
        Person mads = new Person(1L,"Mads Mikkelsen", Gender.MALE);
        Person thomas = new Person(2L,"Thomas Vinterberg", Gender.MALE);
        Person charlie = new Person(3L, "Charlie Sheen", Gender.MALE);
        Person oliver = new Person(4L,"Oliver Stone", Gender.MALE);
        Person babette = new Person(5L,"Sidse Babette", Gender.FEMALE);

        seeded.put("person_mads", personDAO.create(mads));
        seeded.put("person_thomas", personDAO.create(thomas));
        seeded.put("person_charlie", personDAO.create(charlie));
        seeded.put("person_oliver", personDAO.create(oliver));
        seeded.put("person_sidse", personDAO.create(babette));
    }

    private void populateMovies()
    {
        Person mads = (Person) seeded.get("person_mads");
        Person thomas = (Person) seeded.get("person_thomas");
        Person charlie = (Person) seeded.get("person_charlie");
        Person oliver = (Person) seeded.get("person_oliver");

        Movie platoon = new Movie(
                1L,
                "Platoon",
                "Platoon",
                "As a young and naive recruit in Vietnam, Chris Taylor faces a moral crisis when confronted with the horrors of war and the duality of man.",
                LocalDate.of(1986, 12, 19),
                "en",
                7.7,
                "The first casualty of war is innocence.",
                "Released",
                "Denmark",
                120,
                new HashSet<>()
        );
        platoon.addCast(new Cast("Chris Taylor", Role.ACTOR, charlie));
        platoon.addCast(new Cast(null, Role.DIRECTOR, oliver));

        Movie jagten = new Movie(
                2L,
                "Jagten",
                "Jagten",
                "A teacher lives a lonely life, all the while struggling over his son's custody.",
                LocalDate.of(2012, 10, 25),
                "da",
                8.1,
                "The hunt begins.",
                "Released",
                "Denmark",
                137,
                new HashSet<>()
        );
        jagten.addCast(new Cast("Lucas", Role.ACTOR, mads));
        jagten.addCast(new Cast(null, Role.DIRECTOR, thomas));

        Movie bastarden = new Movie(
                3L,
                "Bastarden",
                "Bastarden",
                "In 1755, the impoverished Captain Ludvig Kahlen sets out to conquer the uninhabitable Danish heath.",
                LocalDate.of(2023, 10, 5),
                "da",
                7.7,
                "A captain's ambition, a ruthless rival, and a land that defies them both.",
                "Released",
                "Denmark",
                105,
                new HashSet<>()
        );
        bastarden.addCast(new Cast("Ludvig Kahlen", Role.ACTOR, mads));

        Movie druk = new Movie(
                4L,
                "Druk",
                "Druk",
                "Four high school teachers launch a drinking experiment.",
                LocalDate.of(2020, 9, 24),
                "da",
                7.7,
                "When you hit rock bottom, there's only one way to go.",
                "Released",
                "Denmark",
                121,
                new HashSet<>()
        );
        druk.addCast(new Cast("Martin", Role.ACTOR, mads));
        druk.addCast(new Cast(null, Role.DIRECTOR, thomas));

        Movie haevnen = new Movie(
                5L,
                "Hævnen",
                "Hævnen",
                "Two Danish families meet tragic circumstances and connect in unexpected ways.",
                LocalDate.of(2010, 8, 26),
                "da",
                7.6,
                "Trust no one. Fear nothing.",
                "Released",
                "Denmark",
                90,
                new HashSet<>()
        );

        seeded.put("movie_platoon", movieDAO.create(platoon));
        seeded.put("movie_jagten", movieDAO.create(jagten));
        seeded.put("movie_bastarden", movieDAO.create(bastarden));
        seeded.put("movie_druk", movieDAO.create(druk));
        seeded.put("movie_haevnen", movieDAO.create(haevnen));
    }
}
