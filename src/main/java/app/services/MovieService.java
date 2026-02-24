package app.services;

import app.dtos.MovieDTO;
import app.dtos.MovieDetailDTO;
import app.dtos.MovieResultDTO;
import app.integrations.ITMBDService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MovieService
{
    private final ITMBDService tmbdService;
    private static final int TMDB_MAX_PAGES = 500;

    public MovieService(ITMBDService tmbdService) {
        this.tmbdService = tmbdService;
    }

    public List<MovieDetailDTO> getAllMovieDetails(List<Long> movieIds)
    {
        List<MovieDetailDTO> movieDetailDTOS = new ArrayList<>();

        movieIds.forEach(id -> {

            MovieDetailDTO movieDetailDTO = tmbdService.getMovieDetailById(id);
            movieDetailDTOS.add(movieDetailDTO);

        });

        return movieDetailDTOS;


    }


    public List<MovieDTO> getAllDaMovies(int maxPages)
    {
        List<MovieDTO> movieDTOS = new ArrayList<>();
        int currentPage = 1;
        int totalPages;

        do
        {
            MovieResultDTO movieResultDTO = tmbdService.getAllDaMovies(currentPage);

            totalPages = movieResultDTO.totalPages();
            movieDTOS.addAll(movieResultDTO.movieDTOS());

            currentPage++;

        }
        while (currentPage <= totalPages && currentPage <= maxPages && currentPage <= TMDB_MAX_PAGES);

        return movieDTOS;
    }


    public List<MovieDTO> getMoviesByRating(double lowerBound, double upperBound, int maxPages)
    {
        List<MovieDTO> movieDTOS = new ArrayList<>();
        int currentPage = 1;
        int totalPages;
;
        do
        {
            MovieResultDTO movieResultDTO = tmbdService.getMoviesByRating(lowerBound, upperBound, currentPage);

            totalPages = movieResultDTO.totalPages();
            movieDTOS.addAll(movieResultDTO.movieDTOS());

            currentPage++;

        }
        while (currentPage <= totalPages && currentPage <= maxPages && currentPage <= TMDB_MAX_PAGES);

        return movieDTOS;
    }

    public List<MovieDTO> getSortedByReleaseDate(String query, int maxPages)
    {
        List<MovieDTO> movieDTOS = new ArrayList<>();
        int currentPage = 1;
        int totalPages;

        do
        {
            MovieResultDTO movieResultDTO = tmbdService.fetchMovieByTitle(query, currentPage);

            totalPages = movieResultDTO.totalPages();
            movieDTOS.addAll(movieResultDTO.movieDTOS());

            currentPage++;

        }
        while (currentPage <= totalPages && currentPage <= maxPages && currentPage <= TMDB_MAX_PAGES);

        return movieDTOS.stream()
                .filter(m -> m.releaseDate() != null)
                .sorted(Comparator.comparing(MovieDTO::getReleaseYear).reversed())
                .toList();
    }
}
