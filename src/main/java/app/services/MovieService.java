package app.services;

import app.dtos.MovieDTO;
import app.dtos.tmdb.TMDBMovieDetailDTO;
import app.dtos.UpdateMovieDTO;
import app.entities.Cast;
import app.entities.Genre;
import app.entities.Movie;
import app.enums.Role;
import app.persistence.daos.IMovieDAO;
import app.persistence.daos.IPersonDAO;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MovieService
{
    private final IMovieDAO movieDAO;
    private final IPersonDAO personDAO;

    public MovieService(IMovieDAO movieDAO, IPersonDAO personDAO)
    {

        this.movieDAO = movieDAO;
        this.personDAO = personDAO;
    }

    public MovieDTO submitMovie(TMDBMovieDetailDTO dto)
    {
        validateNotNull(dto);

        Set<Genre> genres = dto.genres().stream()
                .map(g -> new Genre(g.movieId(), g.name()))
                .collect(Collectors.toSet());

        Movie movie = new Movie(
                dto.id(),
                dto.title(),
                dto.originalTitle(),
                dto.overview(),
                dto.releaseDate(),
                dto.originalLanguage(),
                dto.voteAverage(),
                dto.tagline(),
                dto.status(),
                genres
        );

        Set<Cast> castActors = dto.TMDBCreditDTO().actorsDTOs()
                .stream()
                .filter(a -> a.role().equals("Acting"))
                .map(a -> new Cast(a.characterName(), Role.ACTOR, personDAO.getByID(a.personId())))
                .collect(Collectors.toSet());

        Set<Cast> castDirectors = dto.TMDBCreditDTO().TMDBCrewDTOS()
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


    }

    public void updateMovie(UpdateMovieDTO updateMovieDTO)
    {
        validateNotNull(updateMovieDTO);

        Movie movie = movieDAO.getByID(updateMovieDTO.movieId());
        movie.setTitle(updateMovieDTO.title());

        Movie updated = movieDAO.update(movie);
    }

    public void saveAllMovies(List<TMDBMovieDetailDTO> TMDBMovieDetailDTOS)
    {
        if(TMDBMovieDetailDTOS == null || TMDBMovieDetailDTOS.isEmpty())
        {
            throw new IllegalArgumentException("List of movie details cannot be null");
        }

        TMDBMovieDetailDTOS.forEach(m -> {

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

            Set<Cast> castActors = m.TMDBCreditDTO().actorsDTOs()
                    .stream()
                    .filter(a -> a.role().equals("Acting"))
                    .map(a -> new Cast(a.characterName(), Role.ACTOR, personDAO.getByID(a.personId())))
                    .collect(Collectors.toSet());

            Set<Cast> castDirectors = m.TMDBCreditDTO().TMDBCrewDTOS()
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

    public void validateNotNull(Object exists)
    {
        if(exists == null)
        {
            throw new IllegalArgumentException("Movie cant be null");
        }
    }
}
