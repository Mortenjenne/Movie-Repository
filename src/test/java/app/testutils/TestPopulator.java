package app.testutils;

import app.entities.IEntity;
import app.persistence.daos.IMovieDAO;

import java.util.Map;

public class TestPopulator
{
    private final IMovieDAO movieDAO;
    private final Map<String, IEntity> seeded;

    public TestPopulator(IMovieDAO movieDAO, Map<String, IEntity> seeded)
    {
        this.movieDAO = movieDAO;
        this.seeded = seeded;
    }

    public Map<String, IEntity> getSeededData()
    {
        return seeded;
    }

    public void populate()
    {
        populateMovies();
    }

    private void populateMovies()
    {
    }
}
