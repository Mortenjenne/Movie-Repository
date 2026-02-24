package app;

import app.config.HibernateConfig;
import app.dtos.GenreResultDTO;
import app.dtos.MovieDTO;
import app.dtos.MovieDetailDTO;
import app.entities.Cast;
import app.entities.Genre;
import app.entities.Movie;
import app.entities.Person;
import app.enums.Role;
import app.integrations.ITMBDService;
import app.integrations.TMBDService;
import app.persistence.MovieDAO;
import app.persistence.daos.*;
import app.services.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManagerFactory;

import java.net.http.HttpClient;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Main {
    private static final String API_ACCESS_TOKEN = System.getenv("API_ACCESS_TOKEN");

    public static void main(String[] args) {
        Long start = System.currentTimeMillis(); // TODO Remove in production

        HttpClient client = HttpClient.newHttpClient();
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ITMBDService tmdbService = new TMBDService(client, objectMapper, API_ACCESS_TOKEN);
        MovieService movieService = new MovieService(tmdbService);
        IMovieDAO movieDAO = new MovieDAO(emf);
        IPersonDAO personDAO = new PersonDAO(emf);
        IGenreDAO genreDAO = new GenreDAO(emf);

        GenreResultDTO genreResultDTO = tmdbService.getAllGenres();

        List<MovieDTO> danishMovieDTOS = movieService.getAllDaMovies(100);
        //System.out.println(danishMovieDTOS.size());

        List<Long> movieIds = danishMovieDTOS.stream()
                .map(MovieDTO::movieId)
                .toList();

        List<MovieDetailDTO> movieDetailDTOS = movieService.getAllMovieDetails(movieIds);
        System.out.println(movieDetailDTOS.size());

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

        System.out.println("\nACTORS-------------------------------------------------------------------");
        System.out.println("ACTORS SIZE: " + actors.size() + "\n");
        // actors.forEach(System.out::println);

        System.out.println("\nDIRECTORS----------------------------------------------------------------");
        System.out.println("DIRECTORS SIZE: " + directors.size() + "\n");
        // directors.forEach(System.out::println);

        System.out.println("\nGENRES-------------------------------------------------------------------");
        System.out.println("GENRES SIZE: " + genreResultDTO.genres().size() + "\n");
        // genreResultDTO.genres().forEach(System.out::println);

        casts.forEach(person ->
        {
            personDAO.create(person);
        });


        genreResultDTO.genres().forEach(genre ->
        {
            genreDAO.create(genre);
        });

        System.out.println("So the fun begins.....");

        AtomicInteger counter = new AtomicInteger();

        movieDetailDTOS.forEach(m -> {
            System.out.println(counter.get());

            Set<Genre> genres = m.genres().stream()
                    .map(g -> new Genre(g.movieId(), g.name()))
                    .collect(Collectors.toSet());

            Movie movie = new Movie(
                    m.id(),
                    m.title(),
                    m.originalTitle(),
                    m.overview(),
                    m.releaseDate(),
                    m.originalLanguage(),
                    m.voteAverage(),
                    m.tagline(),
                    m.status(),
                    genres
            );

            Set<Cast> castActors = m.creditDTO().actorsDTOs()
                    .stream()
                    .filter(a -> a.role().equals("Acting"))
                    .map(a -> new Cast(a.characterName(), Role.ACTOR, personDAO.getByID(a.personId())))
                    .collect(Collectors.toSet());

            Set<Cast> castDirectors = m.creditDTO().crewDTOs()
                    .stream()
                    .filter(a -> a.job().equals("Director"))
                    .map(a -> new Cast(null, Role.DIRECTOR, personDAO.getByID(a.personId())))
                    .collect(Collectors.toSet());

            // Set<Cast> cast = new HashSet<>();
            // cast.addAll(castActors);
            // cast.addAll(castDirectors);

            castActors.forEach(cm -> {
                movie.addCast(cm);
            });

            castDirectors.forEach(cm -> {
                movie.addCast(cm);
            });

            movieDAO.create(movie);
            counter.getAndIncrement();
        });


        // Persistorder:

        // 1. Genre

        // 2. Person

        // 3. Movie

        // TODO Remove in production
        Long finish = System.currentTimeMillis();
        Long elapsedTime = finish - start;
        long minutes = (elapsedTime / 1000) / 60;
        long seconds = (elapsedTime / 1000) % 60;
        long milliseconds = elapsedTime % 1000;

        // Only 2 decimals on milliseconds
        long hundredths = milliseconds / 10;

        System.out.printf("Total runtime: %02d:%02d.%02d%n",
                minutes,
                seconds,
                hundredths);
    }
}