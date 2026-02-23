package app.persistence.daos;

import app.config.HibernateTestConfig;
import app.entities.IEntity;
import app.persistence.MovieDAO;
import app.testutils.TestCleanDB;
import app.testutils.TestPopulator;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PersonDAOTest
{
    private final EntityManagerFactory emf = HibernateTestConfig.getEntityManagerFactory();
    private Map<String, IEntity> seeded;
    private IPersonDAO personDAO;

    @BeforeEach
    void setUp()
    {
        TestCleanDB.truncateTables(emf);
        TestPopulator populator = new TestPopulator(emf);
        populator.populate();
        seeded = populator.getSeededData();
        personDAO = new PersonDAO(emf);
    }

    @Test
    @DisplayName("Create - Test creating Person")
    void create()
    {
    }

    @Test
    void getAll()
    {
    }

    @Test
    void update()
    {
    }

    @Test
    void delete()
    {
    }

    @Test
    void getByID()
    {
    }
}