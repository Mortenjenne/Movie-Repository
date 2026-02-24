package app.integrations;

import app.dtos.GenreResultDTO;
import app.dtos.MovieDTO;
import app.dtos.MovieDetailDTO;
import app.dtos.MovieResultDTO;

public interface ITMBDService {
    MovieDTO fetchMovieByImdbId(String id);

    MovieResultDTO fetchMovieByTitle(String title, int currentPage);

    MovieDTO getMovieById(int movieId);

    MovieResultDTO getMoviesByRating(double lowerBoundRating, double upperBoundRating, int currentPage);

    GenreResultDTO getAllGenres();

    MovieResultDTO getAllDaMovies(int currentPage);

    MovieDetailDTO getMovieDetailById(Long id);
}
