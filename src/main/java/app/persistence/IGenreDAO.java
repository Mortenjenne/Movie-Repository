package app.persistence;

import app.entities.Genre;

public interface IGenreDAO extends IEntityDAO<Genre, Long>, IEntityReader<Genre, Long>
{

}
