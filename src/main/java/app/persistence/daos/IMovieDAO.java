package app.persistence.daos;

import app.entities.Movie;

public interface IMovieDAO extends IEntityDAO<Movie, Long>, IEntityReader<Movie, Long>
{

}
