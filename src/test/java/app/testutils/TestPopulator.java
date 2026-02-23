package app.testutils;

import app.entities.Cast;
import app.entities.IEntity;
import app.entities.Movie;
import app.entities.Person;
import app.enums.Gender;
import app.enums.Role;
import app.persistence.MovieDAO;
import app.persistence.daos.IMovieDAO;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class TestPopulator
{
    private final IMovieDAO movieDAO;
    private final Map<String, IEntity> seeded;

    public TestPopulator(EntityManagerFactory emf)
    {
        this.movieDAO = new MovieDAO(emf);
        this.seeded = new HashMap<>();
    }

    public Map<String, IEntity> getSeededData()
    {
        return seeded;
    }

    public void populate()
    {
        populateMovies();
    }

    private void populatePersons()
    {
        Person mads = new Person("Mads Mikkelsen", Gender.MALE);
        Person thomas = new Person("Thomas Vinterberg", Gender.MALE);
        Person charlie = new Person("Charlie Sheen", Gender.MALE);
        Person oliver = new Person("Oliver Stone", Gender.MALE);

        //seeded.put("person_mads", personDAO.create(mads));
        //seeded.put("person_thomas", personDAO.create(thomas));
        //seeded.put("person_charlie", personDAO.create(charlie));
        //seeded.put("person_oliver", personDAO.create(oliver));
    }

    private void populateMovies()
    {
        Person mads = (Person) seeded.get("person_mads");
        Person thomas = (Person) seeded.get("person_thomas");
        Person charlie = (Person) seeded.get("person_charlie");
        Person oliver = (Person) seeded.get("person_oliver");

        Movie platoon = new Movie(
                "Platoon",
                "Platoon",
                "As a young and naive recruit in Vietnam, Chris Taylor faces a moral crisis when confronted with the horrors of war and the duality of man.",
                LocalDate.of(1986, 12, 19),
                "en",
                7.7,
                "The first casualty of war is innocence.",
                "Released",
                new HashSet<>()
        );
        platoon.addCast(new Cast("Chris Taylor", Role.ACTOR, charlie));
        platoon.addCast(new Cast(null, Role.DIRECTOR, oliver));

        Movie jagten = new Movie(
                "Jagten",
                "Jagten",
                "A teacher lives a lonely life, all the while struggling over his son's custody.",
                LocalDate.of(2012, 10, 25),
                "da",
                8.1,
                "The hunt begins.",
                "Released",
                new HashSet<>()
        );
        jagten.addCast(new Cast("Lucas", Role.ACTOR, mads));
        jagten.addCast(new Cast(null, Role.DIRECTOR, thomas));

        Movie bastarden = new Movie(
                "Bastarden",
                "Bastarden",
                "In 1755, the impoverished Captain Ludvig Kahlen sets out to conquer the uninhabitable Danish heath.",
                LocalDate.of(2023, 10, 5),
                "da",
                7.7,
                "A captain's ambition, a ruthless rival, and a land that defies them both.",
                "Released",
                new HashSet<>()
        );
        bastarden.addCast(new Cast("Ludvig Kahlen", Role.ACTOR, mads));

        Movie druk = new Movie(
                "Druk",
                "Druk",
                "Four high school teachers launch a drinking experiment.",
                LocalDate.of(2020, 9, 24),
                "da",
                7.7,
                "When you hit rock bottom, there's only one way to go.",
                "Released",
                new HashSet<>()
        );
        druk.addCast(new Cast("Martin", Role.ACTOR, mads));
        druk.addCast(new Cast(null, Role.DIRECTOR, thomas));

        Movie haevnen = new Movie(
                "Hævnen",
                "Hævnen",
                "Two Danish families meet tragic circumstances and connect in unexpected ways.",
                LocalDate.of(2010, 8, 26),
                "da",
                7.6,
                "Trust no one. Fear nothing.",
                "Released",
                new HashSet<>()
        );

        seeded.put("platoon", movieDAO.create(platoon));
        seeded.put("jagten", movieDAO.create(jagten));
        seeded.put("bastarden", movieDAO.create(bastarden));
        seeded.put("druk", movieDAO.create(druk));
        seeded.put("haevnen", movieDAO.create(haevnen));
    }
}
