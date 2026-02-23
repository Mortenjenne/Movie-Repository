package app;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MovieService
{
    private final ITMBDService tmbdService;

    public MovieService(ITMBDService tmbdService) {
        this.tmbdService = tmbdService;
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
        while (currentPage <= totalPages && currentPage <= maxPages && currentPage <= 500);

        return movieDTOS;
    }

    public List<MovieDTO> getSortedByReleaseDate(String query, int maxPages)
    {
        List<MovieDTO> movieDTOS = new ArrayList<>();
        int currentPage = 1;
        int totalPages;
        ;
        do
        {
            MovieResultDTO movieResultDTO = tmbdService.fetchMovieByTitle(query, currentPage);

            totalPages = movieResultDTO.totalPages();
            movieDTOS.addAll(movieResultDTO.movieDTOS());

            currentPage++;

        }
        while (currentPage <= totalPages && currentPage <= maxPages && currentPage <= 500);

        return movieDTOS.stream()
                .filter(m -> m.releaseDate() != null)
                .sorted(Comparator.comparing(MovieDTO::getReleaseYear).reversed())
                .toList();
    }
}
