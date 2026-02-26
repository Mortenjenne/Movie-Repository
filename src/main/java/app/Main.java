package app;

import app.config.HibernateConfig;
import app.dtos.MovieDTO;
import app.dtos.MovieFullDetailDTO;
import app.dtos.tmdb.TMDBMovieDTO;
import app.dtos.tmdb.TMDBMovieDetailDTO;
import app.entities.Genre;
import app.integrations.ITMBDClient;
import app.integrations.TMBDClient;
import app.persistence.IGenreDAO;
import app.persistence.IMovieDAO;
import app.persistence.daos.MovieDAO;
import app.persistence.daos.*;
import app.persistence.IPersonDAO;
import app.services.*;
import app.utils.ExecutionTimer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManagerFactory;

import java.net.http.HttpClient;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    private static final String API_ACCESS_TOKEN = System.getenv("API_ACCESS_TOKEN");

    public static void main(String[] args) {

        ExecutionTimer.start();

        HttpClient client = HttpClient.newHttpClient();
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ITMBDClient tmbdClient = new TMBDClient(client, objectMapper, API_ACCESS_TOKEN);
        
        IPersonDAO personDAO = new PersonDAO(emf);
        IMovieDAO movieDAO = new MovieDAO(emf);
        IGenreDAO genreDAO = new GenreDAO(emf);

        MovieFetchingService movieFetchingService = new MovieFetchingService(tmbdClient);
        PersonService personService = new PersonService(personDAO);
        GenreService genreService = new GenreService(genreDAO);
        MovieService movieService = new MovieService(movieDAO, personDAO);
        MovieSearchService movieSearchService = new MovieSearchService(movieDAO);

        fetchAndPersistApiData(movieFetchingService, genreService, personService, movieService);

        Set<MovieDTO> allMovies = movieService.getAllMovies();
        System.out.println("\nNumber of unique danish movies fetched from database: " + allMovies.size());

        MovieFullDetailDTO movieFullDetailDTO = movieService.getFullMovieDetail(1583668L);
        System.out.println("\n=============== FULL MOVIE DETAILS (BY ID) ===============");
        System.out.println(movieFullDetailDTO);

        System.out.println("\n=============== SEARCH BY TITLE ===============");
        List<MovieDTO> result = movieSearchService.searchByTitle("mine");
        result.forEach(System.out::println);

        System.out.println("\n=============== SEARCH BY ACTOR ===============");
        List<MovieDTO> actorMovies = movieSearchService.searchByActor("Mads Mikkelsen");
        actorMovies.forEach(System.out::println);

        System.out.println("\n=============== SEARCH BY DIRECTOR ===============");
        List<MovieDTO> directorMovies = movieSearchService.searchByDirector("Anders Thomas Jensen");
        directorMovies.forEach(System.out::println);

        System.out.println("\n=============== SEARCH BY GENRE ===============");
        List<MovieDTO> moviesByGenre = movieSearchService.getMoviesByGenre("Western");
        moviesByGenre.forEach(System.out::println);

        System.out.println("\n=============== LOWEST RATED ===============");
        List<MovieDTO> lowestRated = movieSearchService.getLowestRated(10);
        lowestRated.forEach(m -> System.out.println(m.originalTitle() + " | Rating: " + m.voteAverage()));

        System.out.println("\n=============== HIGHEST RATED ===============");
        List<MovieDTO> highestRated = movieSearchService.getTopRated(10);
        highestRated.forEach(m -> System.out.println(m.originalTitle() + " | Rating: " + m.voteAverage()));

        System.out.println("\n=============== AVERAGE RATING OF ALL MOVIES ===============");
        Double totalAverage = movieSearchService.getAverageRating();
        System.out.println(String.format("Rating: %.2f\n", totalAverage));

        System.out.println(ExecutionTimer.finish());
    }

    private static void fetchAndPersistApiData(MovieFetchingService movieFetchingService, GenreService genreService, PersonService personService, MovieService movieService)
    {
        System.out.print("\nFetching movies (movie id's) from TMDB API... ");
        List<Genre> genres = movieFetchingService.getAllGenres();
        List<TMDBMovieDTO> danishTMDBMovieDTOS = movieFetchingService.getAllDaMovies(100);

        Set<Long> movieIds = danishTMDBMovieDTOS.stream()
                .map(TMDBMovieDTO::movieId)
                .collect(Collectors.toSet());

        System.out.print(ExecutionTimer.split());
        System.out.println(
                "\nNumber of unique Danish movies fetched from TMDB API: " +
                movieIds.stream().distinct().count() + "\n"
        );

        System.out.print("Fetching movie details from TMDB API... ");
        List<TMDBMovieDetailDTO> TMDBMovieDetailDTOS = movieFetchingService.getAllMovieDetails(movieIds);
        System.out.println(ExecutionTimer.split());

        System.out.println("Persisting...");

        System.out.print("Persisting genres... ");
        genreService.saveAllGenres(genres);
        System.out.print(ExecutionTimer.split());

        System.out.print("Persisting persons... ");
        personService.saveAllPersons(TMDBMovieDetailDTOS);
        System.out.print(ExecutionTimer.split());

        try {
            System.out.print("Persisting movies... ");
            movieService.saveAllMovies(TMDBMovieDetailDTOS);
            System.out.print(ExecutionTimer.split());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
