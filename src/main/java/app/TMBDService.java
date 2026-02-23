package app;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TMBDService implements ITMBDService {
    private final HttpClient client;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String TMBD_BY_IMDB_ID = "https://api.themoviedb.org/3/find/%s?external_source=imdb_id&language=en-US&api_key=%s";
    private final String TMBD_BY_TITLE = "https://api.themoviedb.org/3/search/movie?query=%s&page=%d&api_key=%s";
    private final String TMBD_BY_ID = "https://api.themoviedb.org/3/movie/%d?language=en-US&api_key=%s";
    private final String TMBD_BY_RATING = "https://api.themoviedb.org/3/discover/movie?include_adult=false&include_video=false&language=en-US&page=%d&sort_by=popularity.desc&vote_average.gte=%f&&vote_average.lte=%f&api_key=%s";


    public TMBDService(HttpClient client, ObjectMapper objectMapper, String apiKey) {
        this.client = client;
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
    }

    @Override
    public MovieResultDTO getMoviesByRating(double lowerBoundRating, double upperBoundRating, int currentPage)
    {
        String url = String.format(TMBD_BY_RATING, currentPage, lowerBoundRating, upperBoundRating, apiKey);
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
        String url = String.format(TMBD_BY_IMDB_ID, id, apiKey);

        HttpRequest request = buildRequest(url);

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            MovieDTO movieDTO = objectMapper.readValue(response.body(), MovieDTO.class);
            return movieDTO;
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public MovieDTO getMovieById(int movieId)
    {
        String url = String.format(TMBD_BY_ID, movieId, apiKey);
        HttpRequest request = buildRequest(url);

        try
        {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            MovieDTO movieDTO = objectMapper.readValue(response.body(), MovieDTO.class);
            return movieDTO;
        } catch (IOException | InterruptedException e )
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public MovieResultDTO fetchMovieByTitle(String title, int currentPage)
    {
       validateNotBlank(title);
       String url = String.format(TMBD_BY_TITLE, title, currentPage, apiKey);

       HttpRequest request = buildRequest(url);

       try{
           HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
           MovieResultDTO movieResultDTO = objectMapper.readValue(response.body(), MovieResultDTO.class);

           return movieResultDTO;

       } catch (IOException | InterruptedException e) {
           throw new RuntimeException(e);
       }
    }

    private HttpRequest buildRequest(String url)
    {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

    }

    private void validateNotBlank(String value)
    {
        if(value == null ||value.isBlank())
        {
            throw new IllegalArgumentException("Bad request: Input cant be empty");
        }
    }
}
