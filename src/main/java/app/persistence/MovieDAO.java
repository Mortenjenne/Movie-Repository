package app.persistence;

import app.entities.Movie;
import app.exceptions.DatabaseException;
import app.persistence.daos.IMovieDAO;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
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
            try
            {
                em.getTransaction().begin();
                em.persist(movie);
                em.getTransaction().commit();
                return movie;
            }
            catch (PersistenceException e)
            {
                rollback(em);
                throw new DatabaseException("Failed to create movie: " + movie.getTitle(), e);
            }
            catch (RuntimeException e)
            {
                rollback(em);
                throw e;
            }
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
        validateNotNull(movie);
        validateId(movie.getId());

        try (EntityManager em = emf.createEntityManager())
        {
            try
            {
                em.getTransaction().begin();
                Movie exist = em.find(Movie.class, movie.getId());
                validateMovieExists(movie.getId(), exist);
                Movie merged = em.merge(movie);
                em.getTransaction().commit();
                return merged;
            }
            catch (PersistenceException e)
            {
                rollback(em);
                throw new DatabaseException("Failed to update movie: " + movie.getTitle(), e);
            }
            catch (RuntimeException e)
            {
                rollback(em);
                throw e;
            }
        }
    }

    @Override
    public boolean delete(Long id)
    {
        validateId(id);

        try (EntityManager em = emf.createEntityManager())
        {
            try {
                em.getTransaction().begin();
                Movie managed = em.find(Movie.class, id);
                validateMovieExists(id, managed);
                em.remove(managed);
                em.getTransaction().commit();
                return true;

            }
            catch (EntityNotFoundException e)
            {
                rollback(em);
                throw e;
            }
            catch (PersistenceException e)
            {
                rollback(em);
                throw new DatabaseException("Failed to delete movie with ID: " + id, e);
            }
        }
    }

    @Override
    public Movie getByID(Long id)
    {
        validateId(id);

        try(EntityManager em = emf.createEntityManager())
        {
            try
            {
                return em.createQuery("SELECT m FROM Movie m LEFT JOIN FETCH m.cast WHERE m.id = :id", Movie.class)
                        .setParameter("id", id)
                        .getSingleResult();

            }
            catch (NoResultException e)
            {
                throw new EntityNotFoundException("Movie with ID " + id + " was not found." + e.getMessage());
            }
        }
    }

    @Override
    public List<Movie> getMoviesByHighestRating(int limit)
    {
        try(EntityManager em = emf.createEntityManager())
        {
            return em.createQuery("SELECT m FROM Movie m ORDER BY m.rating DESC", Movie.class)
                    .setMaxResults(limit)
                    .getResultList();
        }
    }

    @Override
    public List<Movie> getMoviesByLowestRating(int limit)
    {
        try(EntityManager em = emf.createEntityManager())
        {
            return em.createQuery("SELECT m FROM Movie m ORDER BY m.rating ASC", Movie.class)
                    .setMaxResults(limit)
                    .getResultList();
        }
    }

    private void rollback(EntityManager em)
    {
        if (em.getTransaction().isActive())
        {
            em.getTransaction().rollback();
        }
    }

    private void validateMovieExists(Long id, Movie movie)
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
            throw new IllegalArgumentException("Movie cannot be null.");
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
