package app.config;

import app.entities.Genre;
import app.entities.Movie;
import app.entities.Person;
import app.entities.Cast;
import org.hibernate.cfg.Configuration;

final class EntityRegistry
{
    private EntityRegistry() {}

    static void registerEntities(Configuration configuration)
    {
        configuration.addAnnotatedClass(Movie.class);
        configuration.addAnnotatedClass(Genre.class);
        configuration.addAnnotatedClass(Person.class);
        configuration.addAnnotatedClass(Cast.class);
    }
}
