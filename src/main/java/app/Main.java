package app;

import app.dtos.GenreResultDTO;
import app.dtos.MovieDTO;
import app.dtos.MovieDetailDTO;
import app.integrations.ITMBDService;
import app.integrations.TMBDService;
import app.services.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.http.HttpClient;
import java.util.List;

public class Main {
    private static final String API_ACCESS_TOKEN = System.getenv("API_ACCESS_TOKEN");

    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        ITMBDService tmdbService = new TMBDService(client, objectMapper, API_ACCESS_TOKEN);
        MovieService movieService = new MovieService(tmdbService);

        //MovieResultDTO movieResultDTO = tmdbService.fetchMovieByTitle("matrix");
        //MovieDTO movieDTO = tmdbService.getMovieById(373223);
        //System.out.println(movieDTO);
        //movieResultDTO.movieDTOS().forEach(System.out::println);

        //List<MovieDTO> movieDTOS = movieService.getMoviesByRating(8,9,20);
        //System.out.println(movieDTOS.size());
        //List<MovieDTO> sortedMovies = movieService.getSortedByReleaseDate("the", 10);
        //sortedMovies.forEach(m -> System.out.println("ReleaseDate:" + m.releaseDate() + " Title: " + m.title()));

        //GenreResultDTO genreResultDTO = tmdbService.getAllGenres();
        //System.out.println(genreResultDTO);

        List<MovieDTO> danishMovieDTOS = movieService.getAllDaMovies(100);
        //System.out.println(danishMovieDTOS.size());

        List<Long> movieIds = danishMovieDTOS.stream()
                .map(MovieDTO::movieId)
                .toList();

        List<MovieDetailDTO> movieDetailDTOS = movieService.getAllMovieDetails(movieIds);
        System.out.println(movieDetailDTOS.size());


    }
}