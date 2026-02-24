package app.entities;

import app.enums.Gender;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Person implements IEntity
{
    @Id
    @Setter(AccessLevel.NONE)
    private Long id; // TMDB ID

    @Setter
    @Column(name = "name")
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
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person other = (Person) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode()
    {
        return getClass().hashCode();
    }
}
