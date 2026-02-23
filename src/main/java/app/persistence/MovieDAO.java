package app.persistence;

import app.entities.Movie;
import app.persistence.daos.IEntityDAO;
import app.persistence.daos.IEntityReader;

import java.util.Set;

public class MovieDAO implements IEntityDAO<Movie, Long>, IEntityReader<Movie, Long> {
    @Override
    public Movie create(Movie movie) {
        return null;
    }

    @Override
    public Set<Movie> getAll() {
        return Set.of();
    }

    @Override
    public Movie update(Movie movie) {
        return null;
    }

    @Override
    public boolean delete(Long id) {
        return false;
    }

    @Override
    public Movie getByID(Long id) {
        return null;
    }
}
