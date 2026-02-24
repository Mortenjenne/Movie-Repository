package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Genre implements IEntity
{
    @Id
    @EqualsAndHashCode.Include
    private Long id; // TMDB ID

    @Setter
    @Column(nullable = false) // TODO add unique constraint in test phase -> keep if working
    private String name;

    public Genre(Long id, String name)
    {
        this.id = id;
        this.name = name;
    }
}
