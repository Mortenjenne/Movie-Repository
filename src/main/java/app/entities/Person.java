package app.entities;

import app.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(name = "person")
@Entity
public class Person implements IEntity
{
    @Id
    @Setter(AccessLevel.NONE)
    @Column(name = "person_id")
    private Long id; // TMDB ID

    @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    public Person(Long id, String name, Gender gender)
    {
        this.id = id;
        this.name = name;
        this.gender = gender;
    }

    @Override
    public boolean equals(Object object)
    {
        if (object == null || getClass() != object.getClass()) return false;
        Person person = (Person) object;
        return Objects.equals(id, person.id);
    }

    @Override
    public int hashCode()
    {
        return Objects.hashCode(id);
    }
}
