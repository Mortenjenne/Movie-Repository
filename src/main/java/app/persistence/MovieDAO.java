package app.persistence;

import app.entities.Movie;
import app.persistence.daos.IMovieDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.TypedQuery;

import java.util.HashSet;
import java.util.Set;

public class MovieDAO implements IMovieDAO
{
    private final EntityManagerFactory emf;

    public MovieDAO(EntityManagerFactory emf)
    {
        this.emf = emf;
    }

    @Override
    public Movie create(Movie movie)
    {
        validateNotNull(movie);

        try(EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.persist(movie);
            em.getTransaction().commit();

            return movie;
        }
    }

    @Override
    public Set<Movie> getAll()
    {
        try(EntityManager em = emf.createEntityManager())
        {
            TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m", Movie.class);

            return new HashSet<>(query.getResultList());
        }
    }

    @Override
    public Movie update(Movie movie)
    {
        return null;
    }

    @Override
    public boolean delete(Long id)
    {
        return false;
    }

    @Override
    public Movie getByID(Long id)
    {
        validateId(id);

        try(EntityManager em = emf.createEntityManager())
        {
            TypedQuery<Movie> query = em.createQuery("SELECT m FROM Movie m WHERE m.id = :id", Movie.class)
                    .setParameter("id", id);
            Movie movie = query.getSingleResult();
            checkMovieExists(id, movie);

            return movie;
        }
    }

    private void checkMovieExists(Long id, Movie movie)
    {
        if (movie == null)
        {
            throw new EntityNotFoundException("Movie with ID " + id + " was not found.");
        }
    }
    
    private void validateNotNull(Movie movie)
    {
        if (movie == null)
        {
            throw new IllegalArgumentException("Movie" + " cannot be null.");
        }
    }

    private void validateId(Long id)
    {
        if (id == null || id <= 0)
        {
            throw new IllegalArgumentException("Invalid ID: Must be provided and greater than 0.");
        }
    }
}
