package app.testutils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public class TestCleanDB
{
    public static void truncateTables(EntityManagerFactory emf)
    {
        if (emf == null || !emf.isOpen())
            throw new IllegalStateException("EMF is closed in TestCleanDB");

        try (EntityManager em = emf.createEntityManager())
        {
            em.getTransaction().begin();
            em.createNativeQuery("""
            TRUNCATE TABLE
                movie, movie_cast, person, genre
            RESTART IDENTITY CASCADE
        """).executeUpdate();
            em.getTransaction().commit();
        }
    }

}
