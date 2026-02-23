package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "original_title")
    private String originalTitle;

    @Column(name = "description")
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

    public Movie(String title, String originalTitle, String description, LocalDate releaseYear, String language, double rating, String tagline, String status)
    {
        this.title = title;
        this.originalTitle = originalTitle;
        this.description = description;
        this.releaseYear = releaseYear;
        this.language = language;
        this.rating = rating;
        this.tagline = tagline;
        this.status = status;
    }
}
