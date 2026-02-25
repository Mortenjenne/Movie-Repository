package app;

import app.config.HibernateConfig;
import app.dtos.GenreResultDTO;
import app.dtos.MovieDTO;
import app.dtos.MovieDetailDTO;
import app.integrations.ITMBDService;
import app.integrations.TMBDService;
import app.persistence.MovieDAO;
import app.persistence.daos.*;
import app.services.MovieService;
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
        ITMBDService tmdbService = new TMBDService(client, objectMapper, API_ACCESS_TOKEN);
        MovieService movieService = new MovieService(tmdbService);
        IMovieDAO movieDAO = new MovieDAO(emf);
        IPersonDAO personDAO = new PersonDAO(emf);
        IGenreDAO genreDAO = new GenreDAO(emf);

        GenreResultDTO genreResultDTO = tmdbService.getAllGenres();
        List<MovieDTO> danishMovieDTOS = movieService.getAllDaMovies(100);

        List<Long> movieIds = danishMovieDTOS.stream()
                .map(MovieDTO::movieId)
                .toList();

        List<MovieDetailDTO> movieDetailDTOS = movieService.getAllMovieDetails(movieIds);
    }

}