package app.persistence.daos;

import app.entities.Genre;
import app.entities.Person;
import app.exceptions.DatabaseException;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

public class PersonDAO implements IPersonDAO
{
    private final EntityManagerFactory emf;

    public PersonDAO(EntityManagerFactory emf)
    {
        this.emf = emf;
    }

    @Override
    public Person create(Person person)
    {
        validateNotNull(person);

        try(EntityManager em = emf.createEntityManager())
        {
            try
            {
                em.getTransaction().begin();
                em.persist(person);
                em.getTransaction().commit();
                return person;
            }
            catch (PersistenceException e)
            {
                rollback(em);
                throw new DatabaseException("Failed to create person: " + person.getName(), e);
            }
            catch (RuntimeException e)
            {
                rollback(em);
                throw e;
            }
        }
    }

    @Override
    public Set<Person> getAll()
    {
        try (EntityManager em = emf.createEntityManager())
        {
            try
            {
                TypedQuery<Person> query = em.createQuery("SELECT p FROM Person p", Person.class);
                return new HashSet<>(query.getResultList());
            }
            catch (PersistenceException e)
            {
                throw new DatabaseException("Failed to fetch people: " + e.getMessage());
            }
        }
    }

    @Override
    public Person update(Person person)
    {
        validateNotNull(person);
        validateId(person.getId());

        try (EntityManager em = emf.createEntityManager())
        {
            validateGenreExists(em, person);

            try
            {
                em.getTransaction().begin();
                Person managedPerson = em.merge(person);
                em.getTransaction().commit();
                return managedPerson;
            }
            catch (PersistenceException e)
            {
                rollback(em);
                throw new DatabaseException("Failed to update person: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean delete(Long id)
    {
        validateId(id);

        try (EntityManager em = emf.createEntityManager())
        {
            Person managedPerson = em.find(Person.class, id);
            validateGenreExists(em, managedPerson);

            try {
                em.getTransaction().begin();
                em.remove(managedPerson);
                em.getTransaction().commit();
                return true;
            }
            catch (PersistenceException e)
            {
                rollback(em);
                throw new DatabaseException("Failed to delete person with ID: " + id, e);
            }
            catch (RuntimeException e)
            {
                rollback(em);
                throw e;
            }
        }
    }

    @Override
    public Person getByID(Long id)
    {
        validateId(id);

        try (EntityManager em = emf.createEntityManager())
        {
            Person managedPerson = em.find(Person.class, id);
            validateGenreExists(em, managedPerson);
            return managedPerson;
        }
        catch (PersistenceException e)
        {
            throw new DatabaseException("Failed to find person with id: " + id + " " + e.getMessage());
        }
    }

    private void rollback(EntityManager em)
    {
        if (em.getTransaction().isActive())
        {
            em.getTransaction().rollback();
        }
    }

    private void validateGenreExists(EntityManager em, Person person)
    {
        Person exists = em.find(Person.class, person);
        if (exists == null)
        {
            throw new EntityNotFoundException("Person with ID " + person.getId() + " was not found.");
        }
    }

    private void validateNotNull(Person person)
    {
        if (person == null)
        {
            throw new IllegalArgumentException("Person cannot be null.");
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
