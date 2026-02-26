package app.persistence;

import app.entities.Movie;

import java.util.List;

public interface IMovieDAO extends IEntityDAO<Movie, Long>, IEntityReader<Movie, Long>
{
    List<Movie> getMoviesByHighestRating(int limit);

    List<Movie> getMoviesByLowestRating(int limit);

    Movie getMovieByTitle(String title);

    List<Movie> getMoviesByActor(String actor);

    List<Movie> getMoviesByDirector(String director);

    List<Movie> getMoviesByGenre(String genre);

    Movie getByIdWithDetails(Long id);

    Double getAverageMovieRatings();

    boolean existsById(Long id);

    List<Movie> searchByTitle(String title);
}
