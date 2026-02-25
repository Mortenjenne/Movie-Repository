package app.persistence.daos;

import app.entities.Person;

public interface IPersonDAO extends IEntityDAO<Person, Long>, IEntityReader<Person, Long>
{
    Person findById(Long id);
}
