package app.persistence.daos;

import app.entities.Movie;

import java.util.List;

public interface IMovieDAO extends IEntityDAO<Movie, Long>, IEntityReader<Movie, Long>
{
    public List<Movie> getMoviesByHighestRating(int limit);

    public List<Movie> getMoviesByLowestRating(int limit);

    public Movie getMovieByTitle(String title);

    Movie getByIdWithDetails(Long id);

    Double getAverageMovieRatings();

    boolean existsById(Long id);

    List<Movie> searchByTitle(String title);
}
