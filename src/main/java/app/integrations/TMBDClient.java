package app.integrations;

import app.dtos.tmdb.TMDBGenreResultDTO;
import app.dtos.tmdb.TMDBMovieDetailDTO;
import app.dtos.tmdb.TMDBMovieResultDTO;
import app.exceptions.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TMBDClient implements ITMBDClient
{
    private final HttpClient client;
    private final ObjectMapper objectMapper;
    private final String apiAccessToken;

    private final String TMBD_ALL_GENRES = "https://api.themoviedb.org/3/genre/movie/list";
    private final String TMDB_ALL_DA_MOVIES_FROM_5_YEARS = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=da&page=%d&primary_release_date.gte=2021-01-01&sort_by=popularity.desc&with_original_language=da";
    private final String TMDB_MOVIE_DETAIL = "https://api.themoviedb.org/3/movie/%d?append_to_response=credits";

    public TMBDClient(HttpClient client, ObjectMapper objectMapper, String apiAccessToken) {
        this.client = client;
        this.objectMapper = objectMapper;
        this.apiAccessToken = apiAccessToken;
    }

    @Override
    public TMDBMovieDetailDTO getMovieDetailById(Long id)
    {
        String url = String.format(TMDB_MOVIE_DETAIL, id);
        HttpRequest request = buildRequest(url);
        try
        {
            String response = getResponse(request);
            TMDBMovieDetailDTO resultDTO = objectMapper.readValue(response, TMDBMovieDetailDTO.class);
            return resultDTO;
        }
        catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TMDBGenreResultDTO getAllGenres() {
        HttpRequest request = buildRequest(TMBD_ALL_GENRES);
        try
        {
            String response = getResponse(request);
            TMDBGenreResultDTO resultDTO = objectMapper.readValue(response, TMDBGenreResultDTO.class);
            return resultDTO;
        }
        catch (IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public TMDBMovieResultDTO getAllDaMovies(int currentPage)
    {
        String url = String.format(TMDB_ALL_DA_MOVIES_FROM_5_YEARS, currentPage);
        HttpRequest request = buildRequest(url);
        try
        {
            String response = getResponse(request);
            TMDBMovieResultDTO resultDTO = objectMapper.readValue(response, TMDBMovieResultDTO.class);
            return resultDTO;
        }
        catch (IOException | InterruptedException e) {
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
}