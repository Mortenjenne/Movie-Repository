package app;

import app.config.HibernateConfig;
import app.dtos.tmdb.TMDBGenreResultDTO;
import app.dtos.tmdb.TMDBMovieDTO;
import app.dtos.tmdb.TMDBMovieDetailDTO;
import app.integrations.ITMBDClient;
import app.integrations.TMBDClient;
import app.persistence.MovieDAO;
import app.persistence.daos.*;
import app.services.MovieFetchingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManagerFactory;

import java.net.http.HttpClient;
import java.util.List;

public class Main {
    private static final String API_ACCESS_TOKEN = System.getenv("API_ACCESS_TOKEN");

    public static void main(String[] args) {
        Long start = System.currentTimeMillis(); // TODO Remove in production

        HttpClient client = HttpClient.newHttpClient();
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ITMBDClient tmdbService = new TMBDClient(client, objectMapper, API_ACCESS_TOKEN);
        MovieFetchingService movieFetchingService = new MovieFetchingService(tmdbService);
        IMovieDAO movieDAO = new MovieDAO(emf);
        IPersonDAO personDAO = new PersonDAO(emf);
        IGenreDAO genreDAO = new GenreDAO(emf);

        TMDBGenreResultDTO TMDBGenreResultDTO = tmdbService.getAllGenres();
        List<TMDBMovieDTO> danishTMDBMovieDTOS = movieFetchingService.getAllDaMovies(100);

        List<Long> movieIds = danishTMDBMovieDTOS.stream()
                .map(TMDBMovieDTO::movieId)
                .toList();

        List<TMDBMovieDetailDTO> TMDBMovieDetailDTOS = movieFetchingService.getAllMovieDetails(movieIds);
    }

}