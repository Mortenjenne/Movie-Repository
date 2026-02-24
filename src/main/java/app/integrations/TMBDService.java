package app.integrations;

import app.dtos.GenreResultDTO;
import app.dtos.MovieDTO;
import app.dtos.MovieDetailDTO;
import app.dtos.MovieResultDTO;
import app.exceptions.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TMBDService implements ITMBDService {
    private final HttpClient client;
    private final ObjectMapper objectMapper;

    // ========== REFACTOR ==========
    private final String apiAccessToken;
    private final String TMBD_BY_IMDB_ID = "https://api.themoviedb.org/3/find/%s?external_source=imdb_id&language=en-US&api_key=%s";
    private final String TMBD_BY_TITLE = "https://api.themoviedb.org/3/search/movie?query=%s&page=%d&api_key=%s";
    private final String TMBD_BY_ID = "https://api.themoviedb.org/3/movie/%d?language=en-US&api_key=%s";
    private final String TMBD_BY_RATING = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=%d&sort_by=popularity.desc&vote_average.gte=%f&&vote_average.lte=%f&api_key=%s";
    // ========== REFACTOR ==========

    private final String TMBD_ALL_GENRES = "https://api.themoviedb.org/3/genre/movie/list";
    private final String TMDB_ALL_DA_MOVIES_FROM_5_YEARS = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=da&page=%d&primary_release_date.gte=2021-01-01&sort_by=popularity.desc&with_original_language=da";
    private final String TMDB_MOVIE_DETAIL = "https://api.themoviedb.org/3/movie/%d?append_to_response=credits";

    public TMBDService(HttpClient client, ObjectMapper objectMapper, String apiAccessToken) {
        this.client = client;
        this.objectMapper = objectMapper;
        this.apiAccessToken = apiAccessToken;
    }

    @Override
    public MovieDetailDTO getMovieDetailById(Long id)
    {
        String url = String.format(TMDB_MOVIE_DETAIL, id);
        HttpRequest request = buildRequest(url);
        try
        {
            String response = getResponse(request);
            MovieDetailDTO resultDTO = objectMapper.readValue(response, MovieDetailDTO.class);
            return resultDTO;
        }
        catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public GenreResultDTO getAllGenres() {
        HttpRequest request = buildRequest(TMBD_ALL_GENRES);
        try
        {
            String response = getResponse(request);
            GenreResultDTO resultDTO = objectMapper.readValue(response, GenreResultDTO.class);
            return resultDTO;
        }
        catch (IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MovieResultDTO getAllDaMovies(int currentPage)
    {
        String url = String.format(TMDB_ALL_DA_MOVIES_FROM_5_YEARS, currentPage);
        HttpRequest request = buildRequest(url);
        try
        {
            String response = getResponse(request);
            MovieResultDTO resultDTO = objectMapper.readValue(response, MovieResultDTO.class);
            return resultDTO;
        }
        catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public MovieResultDTO getMoviesByRating(double lowerBoundRating, double upperBoundRating, int currentPage) {
        String url = String.format(TMBD_BY_RATING, currentPage, lowerBoundRating, upperBoundRating, apiAccessToken);
        HttpRequest request = buildRequest(url);

        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            MovieResultDTO movieResultDTO = objectMapper.readValue(response.body(), MovieResultDTO.class);

            return movieResultDTO;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public MovieDTO fetchMovieByImdbId(String id)
    {
        String url = String.format(TMBD_BY_IMDB_ID, id, apiAccessToken);
        HttpRequest request = buildRequest(url);
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            MovieDTO movieDTO = objectMapper.readValue(response.body(), MovieDTO.class);
            return movieDTO;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public MovieDTO getMovieById(int movieId) {
        String url = String.format(TMBD_BY_ID, movieId, apiAccessToken);
        HttpRequest request = buildRequest(url);
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            MovieDTO movieDTO = objectMapper.readValue(response.body(), MovieDTO.class);
            return movieDTO;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MovieResultDTO fetchMovieByTitle(String title, int currentPage) {
        validateNotBlank(title);
        String url = String.format(TMBD_BY_TITLE, title, currentPage, apiAccessToken);

        HttpRequest request = buildRequest(url);

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            MovieResultDTO movieResultDTO = objectMapper.readValue(response.body(), MovieResultDTO.class);

            return movieResultDTO;

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private String getResponse(HttpRequest request) throws IOException, InterruptedException
    {
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200)
        {
            throw new ApiException(response.statusCode(), "TMDB returned error code: " + response.statusCode());
        }
        return response.body();
    }

    private HttpRequest buildRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("accept", "application/json")
                .header("Authorization", "bearer " + apiAccessToken)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
    }

    private void validateNotBlank(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Bad request: Input cant be empty");
        }
    }
}
