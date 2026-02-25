package app.services;

import app.dtos.MovieDetailDTO;
import app.entities.Cast;
import app.entities.Genre;
import app.entities.Movie;
import app.enums.Role;
import app.persistence.daos.IMovieDAO;
import app.persistence.daos.IPersonDAO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MovieServiceNew
{
    private final IMovieDAO movieDAO;
    private final IPersonDAO personDAO;

    public MovieServiceNew(IMovieDAO movieDAO, IPersonDAO personDAO) {

        this.movieDAO = movieDAO;
        this.personDAO = personDAO;
    }

    public void saveAllMovies(List<MovieDetailDTO> movieDetailDTOS)
    {
        if(movieDetailDTOS == null || movieDetailDTOS.isEmpty())
        {
            throw new IllegalArgumentException("List of movie details cannot be null");
        }

        movieDetailDTOS.forEach(m -> {

            Set<Genre> genres = m.genres().stream()
                    .map(g -> new Genre(g.movieId(), g.name()))
                    .collect(Collectors.toSet());

            Movie movie = new Movie(
                    m.id(),
                    m.title(),
                    m.originalTitle(),
                    m.overview(),
                    m.releaseDate(),
                    m.originalLanguage(),
                    m.voteAverage(),
                    m.tagline(),
                    m.status(),
                    genres
            );

            Set<Cast> castActors = m.creditDTO().actorsDTOs()
                    .stream()
                    .filter(a -> a.role().equals("Acting"))
                    .map(a -> new Cast(a.characterName(), Role.ACTOR, personDAO.getByID(a.personId())))
                    .collect(Collectors.toSet());

            Set<Cast> castDirectors = m.creditDTO().crewDTOs()
                    .stream()
                    .filter(a -> a.job().equals("Director"))
                    .map(a -> new Cast(null, Role.DIRECTOR, personDAO.getByID(a.personId())))
                    .collect(Collectors.toSet());


            castActors.forEach(cm -> {
                movie.addCast(cm);
            });

            castDirectors.forEach(cm -> {
                movie.addCast(cm);
            });

            movieDAO.create(movie);
        });

    }
}
