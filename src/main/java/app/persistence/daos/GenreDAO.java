package app.persistence.daos;

import app.entities.Genre;
import app.exceptions.DatabaseException;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

public class GenreDAO implements IGenreDAO
{
    private final EntityManagerFactory emf;

    public GenreDAO(EntityManagerFactory emf)
    {
        this.emf = emf;
    }

    @Override
    public Genre create(Genre genre)
    {
        validateNotNull(genre);

        try(EntityManager em = emf.createEntityManager())
        {
            try
            {
                em.getTransaction().begin();
                em.persist(genre);
                em.getTransaction().commit();
                return genre;
            }
            catch (PersistenceException e)
            {
                rollback(em);
                throw new DatabaseException("Failed to create genre: " + genre.getName(), e);
            }
            catch (RuntimeException e)
            {
                rollback(em);
                throw e;
            }
        }
    }

    @Override
    public Set<Genre> getAll()
    {
        try (EntityManager em = emf.createEntityManager())
        {
            try
            {
                TypedQuery<Genre> query = em.createQuery("SELECT g FROM Genre g", Genre.class);
                return new HashSet<>(query.getResultList());
            }
            catch (PersistenceException e)
            {
                throw new DatabaseException("Failed to fetch genres: " + e.getMessage());
            }
        }
    }

    @Override
    public Genre update(Genre genre)
    {
        validateNotNull(genre);
        validateId(genre.getId());

        try (EntityManager em = emf.createEntityManager())
        {
            Genre exists = em.find(Genre.class, genre.getId());
            validateGenreExists(exists, exists.getId());

            try
            {
                em.getTransaction().begin();
                Genre managedGenre = em.merge(genre);
                em.getTransaction().commit();
                return managedGenre;
            }
            catch (PersistenceException e)
            {
                rollback(em);
                throw new DatabaseException("Failed to update genre: " + e.getMessage());
            }
        }
        catch (EntityNotFoundException e)
        {
            throw new DatabaseException("Failed to update genre" + e.getMessage());
        }
    }

    @Override
    public boolean delete(Long id)
    {
        validateId(id);

        try (EntityManager em = emf.createEntityManager())
        {
            Genre managedGenre = em.find(Genre.class, id);
            validateGenreExists(managedGenre, id);

            try {
                em.getTransaction().begin();
                em.remove(managedGenre);
                em.getTransaction().commit();
                return true;
            }
            catch (PersistenceException e)
            {
                rollback(em);
                throw new DatabaseException("Failed to delete genre with ID: " + id, e);
            }
            catch (RuntimeException e)
            {
                rollback(em);
                throw e;
            }
        }
    }

    @Override
    public Genre getByID(Long id)
    {
        validateId(id);

        try (EntityManager em = emf.createEntityManager())
        {
            Genre managedGenre = em.find(Genre.class, id);
            validateGenreExists(managedGenre, id);
            return managedGenre;
        }
        catch (PersistenceException e)
        {
            throw new DatabaseException("Failed to find genre with id: " + id + " " + e.getMessage());
        }
    }

    private void rollback(EntityManager em)
    {
        if (em.getTransaction().isActive())
        {
            em.getTransaction().rollback();
        }
    }

    private void validateGenreExists(Genre exists, Long id)
    {
        if (exists == null)
        {
            throw new EntityNotFoundException("Genre with ID " + id + " was not found.");
        }
    }

    private void validateNotNull(Genre genre)
    {
        if (genre == null)
        {
            throw new IllegalArgumentException("Genre cannot be null.");
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
