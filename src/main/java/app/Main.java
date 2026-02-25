package app;

import app.config.HibernateConfig;
import app.dtos.GenreDTO;
import app.dtos.MovieDTO;
import app.dtos.MovieFullDetailDTO;
import app.dtos.tmdb.TMDBGenreResultDTO;
import app.dtos.tmdb.TMDBMovieDTO;
import app.dtos.tmdb.TMDBMovieDetailDTO;
import app.entities.Genre;
import app.integrations.ITMBDClient;
import app.integrations.TMBDClient;
import app.persistence.MovieDAO;
import app.persistence.daos.*;
import app.persistence.daos.IPersonDAO;
import app.services.*;
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


        List<Genre> genres = movieFetchingService.getAllGenres();
        List<TMDBMovieDTO> danishTMDBMovieDTOS = movieFetchingService.getAllDaMovies(100);

        Set<Long> movieIds = danishTMDBMovieDTOS.stream()
                .map(TMDBMovieDTO::movieId)
                .collect(Collectors.toSet());

        System.out.println(
                movieIds.stream().distinct().count()
        );


        List<TMDBMovieDetailDTO> TMDBMovieDetailDTOS = movieFetchingService.getAllMovieDetails(movieIds);

        genreService.saveAllGenres(genres);
        System.out.println("persisted genres");
        personService.saveAllPersons(TMDBMovieDetailDTOS);
        System.out.println("persisted persons");
        try {
            movieService.saveAllMovies(TMDBMovieDetailDTOS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Set<MovieDTO> allMovies = movieService.getAllMovies();
        System.out.println(allMovies.size());

        MovieFullDetailDTO movieFullDetailDTO = movieService.getFullMovieDetail(1583668L);
        System.out.println(movieFullDetailDTO);

        System.out.println("-----Search result--------");
        List<MovieDTO> result = movieSearchService.searchByTitle("mine");
        result.forEach(System.out::println);

        System.out.println("-----Lowest rated--------");
        List<MovieDTO> lowestRated = movieSearchService.getLowestRated(10);
        lowestRated.forEach(m -> System.out.println(m.originalTitle() + " rating: " + m.voteAverage()));

        System.out.println("-----Highest rated--------");
        List<MovieDTO> highestRated = movieSearchService.getTopRated(10);
        highestRated.forEach(m -> System.out.println(m.originalTitle() + " rating: " + m.voteAverage()));

        System.out.println("-----All movies average rating--------");
        Double totalAverage = movieSearchService.getAverageRating();
        System.out.println(totalAverage);

    }
}
