package app;

import java.util.List;

public interface ITMBDService {
    MovieDTO fetchMovieByImdbId(String id);

    MovieResultDTO fetchMovieByTitle(String title, int currentPage);

    MovieDTO getMovieById(int movieId);

    MovieResultDTO getMoviesByRating(double lowerBoundRating, double upperBoundRating, int currentPage);
}
