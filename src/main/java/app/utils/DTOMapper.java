package app.utils;

import app.dtos.CastDTO;
import app.dtos.GenreDTO;
import app.dtos.MovieDTO;
import app.dtos.PersonDTO;
import app.entities.Cast;
import app.entities.Genre;
import app.entities.Movie;
import app.entities.Person;

public class DTOMapper
{

    public static MovieDTO mapMovieToDTO(Movie movie) {
        return new MovieDTO(
                movie.getId(),
                movie.getOriginalTitle(),
                movie.getTitle(),
                movie.getOriginCountry(),
                movie.getLanguage(),
                movie.getDescription(),
                movie.getReleaseYear(),
                movie.getRuntime(),
                movie.getStatus(),
                movie.getTagline(),
                movie.getRating()
        );
    }

    public static PersonDTO mapPersonToDTO(Person person)
    {
        return new PersonDTO(
                person.getId(),
                person.getName(),
                person.getGender()
        );
    }

    public static CastDTO mapCastToDTO(Cast cast)
    {
        PersonDTO personDTO = new PersonDTO(
                cast.getPerson().getId(),
                cast.getPerson().getName(),
                cast.getPerson().getGender()
        );

        return new CastDTO(
                cast.getId(),
                cast.getCharacterName(),
                cast.getRole(),
                personDTO
        );
    }

    public static GenreDTO mapGenreToDTO(Genre genre)
    {
        return new GenreDTO(
                genre.getId(),
                genre.getName()
        );
    }
}
