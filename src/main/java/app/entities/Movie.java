package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name = "movie")
@Entity
public class Movie implements IEntity
{

    @Id
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Include
    private Long id; // TMDB ID

    @Column(name = "title")
    private String title;

    @Column(name = "original_title")
    private String originalTitle;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "release_year")
    private LocalDate releaseYear;

    @Column(name = "language")
    private String language; // TODO Add ENUM

    @Column(name = "rating")
    private double rating;

    @Column(name = "tagline")
    private String tagline;

    @Column(name = "status")
    private String status; // TODO Add ENUM

    @ManyToMany
    @JoinTable(name = "movie_genre",
            joinColumns = @JoinColumn(name = "movies"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres = new HashSet<>();

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Cast> cast = new HashSet<>();

    public Movie(Long id, String title, String originalTitle, String description, LocalDate releaseYear, String language, double rating, String tagline, String status, Set<Genre> genres)
    {
        this.id = id;
        this.title = title;
        this.originalTitle = originalTitle;
        this.description = description;
        this.releaseYear = releaseYear;
        this.language = language;
        this.rating = rating;
        this.tagline = tagline;
        this.status = status;
        this.genres = genres;
    }

    public void addGenre(Genre genre)
    {
        if (genre == null)
        {
            throw new IllegalArgumentException ("Genre cannot be null");
        }
        this.genres.add(genre);
    }

    public void removeGenre(Genre genre)
    {
        if (this.genres.contains(genre))
        {
            this.genres.remove(genre);
        }
        else
        {
            throw new IllegalArgumentException ("The movie \"" + this.title + "\" does not contain the genre: " + genre);
        }
    }

    public void addCast(Cast castMember)
    {
        if (castMember == null)
        {
            throw new IllegalArgumentException("Cast cannot be null");
        }
        cast.add(castMember);
        castMember.setMovie(this);
    }

    public void removeCast(Cast castMember)
    {
        if (!this.cast.contains(castMember))
        {
            throw new IllegalArgumentException("Cast member not found in movie: " + this.title);
        }
        cast.remove(castMember);
        castMember.setMovie(null);
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        Movie movie = (Movie) object;
        return Objects.equals(id, movie.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
