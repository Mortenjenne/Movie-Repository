package app.persistence;

import app.entities.Person;

public interface IPersonDAO extends IEntityDAO<Person, Long>, IEntityReader<Person, Long>
{
    Person findById(Long id);
    boolean existsById(Long id);
}
