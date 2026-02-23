package app;

import app.dtos.MovieDTO;
import app.integrations.ITMBDService;
import app.integrations.TMBDService;
import app.services.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.http.HttpClient;
import java.util.List;

public class Main {
    private static final String API_KEY = System.getenv("TMDB_APIKEY");

    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ITMBDService tmdbService = new TMBDService(client, objectMapper, API_KEY);


        //MovieResultDTO movieResultDTO = tmdbService.fetchMovieByTitle("matrix");
        //MovieDTO movieDTO = tmdbService.getMovieById(373223);
        //System.out.println(movieDTO);
        //movieResultDTO.movieDTOS().forEach(System.out::println);
        MovieService movieService = new MovieService(tmdbService);
        List<MovieDTO> movieDTOS = movieService.getMoviesByRating(8,9,20);
        System.out.println(movieDTOS.size());

        List<MovieDTO> sortedMovies = movieService.getSortedByReleaseDate("the", 10);
        sortedMovies.forEach(m -> System.out.println("ReleaseDate:" + m.releaseDate() + " Title: " + m.title()));


    }
}