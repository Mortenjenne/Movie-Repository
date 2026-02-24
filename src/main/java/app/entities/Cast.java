package app.entities;

import app.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "movie_cast")
public class Cast implements IEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "character_name")
    private String characterName;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    public Cast(String characterName, Role role, Person person)
    {
        this.characterName = characterName;
        this.role = role;
        this.person = person;
    }

    public void setPerson(Person person)
    {
        this.person = person;
    }

    public void setMovie(Movie movie)
    {
        this.movie = movie;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof Cast)) return false;
        Cast other = (Cast) o;
        return id != null && id.equals(other.id);
    }

    @Override
    public int hashCode()
    {
        return getClass().hashCode();
    }
}
