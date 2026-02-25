package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@NoArgsConstructor
@ToString
@Entity
public class Genre implements IEntity
{
    @Id
    @Column(name = "genre_id")
    private Long id; // TMDB ID

    @Setter
    @Column(nullable = false) // TODO add unique constraint in test phase -> keep if working
    private String name;

    public Genre(Long id, String name)
    {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Genre genre = (Genre) object;
        return Objects.equals(id, genre.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
