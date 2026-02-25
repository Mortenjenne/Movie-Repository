package app.persistence.daos;

import app.entities.Movie;

import java.util.List;

public interface IMovieDAO extends IEntityDAO<Movie, Long>, IEntityReader<Movie, Long>
{
    public List<Movie> getMoviesByHighestRating(int limit);

    public List<Movie> getMoviesByLowestRating(int limit);

    public Movie getMovieByTitle(String title);
}
