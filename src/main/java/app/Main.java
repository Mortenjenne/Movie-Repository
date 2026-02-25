package app;

import app.config.HibernateConfig;
import app.dtos.GenreDTO;
import app.dtos.tmdb.TMDBGenreResultDTO;
import app.dtos.tmdb.TMDBMovieDTO;
import app.dtos.tmdb.TMDBMovieDetailDTO;
import app.entities.Genre;
import app.integrations.ITMBDClient;
import app.integrations.TMBDClient;
import app.persistence.MovieDAO;
import app.persistence.daos.*;
import app.services.GenreService;
import app.services.MovieFetchingService;
import app.services.MovieService;
import app.services.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManagerFactory;

import java.net.http.HttpClient;
import java.util.List;

public class Main {
    private static final String API_ACCESS_TOKEN = System.getenv("API_ACCESS_TOKEN");

    public static void main(String[] args)
    {

        HttpClient client = HttpClient.newHttpClient();
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ITMBDClient tmbdClient = new TMBDClient(client, objectMapper, API_ACCESS_TOKEN);
        MovieFetchingService movieFetchingService = new MovieFetchingService(tmbdClient);

        IPersonDAO personDAO = new PersonDAO(emf);
        IMovieDAO movieDAO = new MovieDAO(emf);
        IGenreDAO genreDAO = new GenreDAO(emf);

        MovieService movieService = new MovieService(movieDAO, personDAO);
        PersonService personService = new PersonService(personDAO);
        GenreService genreService = new GenreService(genreDAO);

        List<Genre> genres = movieFetchingService.getAllGenres();
        List<TMDBMovieDTO> danishTMDBMovieDTOS = movieFetchingService.getAllDaMovies(100);
        List<Long> movieIds = danishTMDBMovieDTOS.stream()
                .map(TMDBMovieDTO::movieId)
                .toList();


        List<TMDBMovieDetailDTO> TMDBMovieDetailDTOS = movieFetchingService.getAllMovieDetails(movieIds);

        genreService.saveAllGenres(genres);
        System.out.println("persisted genres");
        personService.saveAllPersons(TMDBMovieDetailDTOS);
        System.out.println("persisted persons");
        movieService.saveAllMovies(TMDBMovieDetailDTOS);
    }

}