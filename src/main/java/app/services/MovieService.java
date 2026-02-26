package app.services;

import app.dtos.*;
import app.dtos.tmdb.TMDBMovieDetailDTO;
import app.entities.Cast;
import app.entities.Genre;
import app.entities.Movie;
import app.enums.Role;
import app.persistence.IMovieDAO;
import app.persistence.IPersonDAO;
import app.utils.DTOMapper;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class MovieService implements IMovieService {
    private final IMovieDAO movieDAO;
    private final IPersonDAO personDAO;

    public MovieService(IMovieDAO movieDAO, IPersonDAO personDAO)
    {
        this.movieDAO = movieDAO;
        this.personDAO = personDAO;
    }

    @Override
    public MovieDTO submitMovie(TMDBMovieDetailDTO dto)
    {
        validateNotNull(dto);
        validateId(dto.id());

        Movie movie = buildMovie(dto);
        boolean exists = movieDAO.existsById(dto.id());

        Movie result;
        if(exists)
        {
            result = movieDAO.update(movie);
        }
        else
        {
           result = movieDAO.create(movie);
        }

        return DTOMapper.mapMovieToDTO(result);
    }

    @Override
    public MovieDTO updateMovie(UpdateMovieDTO updateMovieDTO)
    {
        validateNotNull(updateMovieDTO);
        validateId(updateMovieDTO.movieId());

        Movie movie = movieDAO.getByID(updateMovieDTO.movieId());
        movie.setTitle(updateMovieDTO.title());

        Movie updated = movieDAO.update(movie);

        return DTOMapper.mapMovieToDTO(updated);
    }

    @Override
    public Set<MovieDTO> getAllMovies()
    {
        return movieDAO.getAll()
                .stream()
                .map(DTOMapper::mapMovieToDTO)
                .collect(Collectors.toSet());
    }

    @Override
    public MovieDTO findById(Long id)
    {
        validateId(id);

        Movie movie = movieDAO.getByID(id);
        return DTOMapper.mapMovieToDTO(movie);
    }

    @Override
    public MovieFullDetailDTO getFullMovieDetail(Long id)
    {
        validateId(id);


        Movie movie = movieDAO.getByIdWithDetails(id);

        MovieDTO movieDTO = DTOMapper.mapMovieToDTO(movie);

        List<CastDTO> cast = movie.getCast()
                .stream()
                .map(DTOMapper::mapCastToDTO)
                .toList();

        List<GenreDTO> genres = movie.getGenres()
                .stream()
                .map(DTOMapper::mapGenreToDTO)
                .toList();

        return new MovieFullDetailDTO(movieDTO, cast, genres);
    }

    @Override
    public void saveAllMovies(List<TMDBMovieDetailDTO> TMDBMovieDetailDTOS)
    {
        if(TMDBMovieDetailDTOS == null || TMDBMovieDetailDTOS.isEmpty())
        {
            throw new IllegalArgumentException("List of movie details cannot be null");
        }

        TMDBMovieDetailDTOS.forEach(m -> {
            Movie movie = buildMovie(m);
            boolean existing = movieDAO.existsById(movie.getId());

            if (!existing) {
                movieDAO.create(movie);
            }

        });
    }

    private Movie buildMovie(TMDBMovieDetailDTO dto) {
        Set<Genre> genres = dto.genres().stream()
                .map(g -> new Genre(g.genreId(), g.name()))
                .collect(Collectors.toSet());

        String originCountry = "";

        if (dto.originCountries() != null && !dto.originCountries().isEmpty()) {
            originCountry = dto.originCountries().get(0);
        }

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
                originCountry,
                dto.runtime(),
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

        castActors.forEach(movie::addCast);

        castDirectors.forEach(movie::addCast);

        return movie;
    }

    private void validateNotNull(Object exists)
    {
        if(exists == null)
        {
            throw new IllegalArgumentException("Movie cant be null");
        }
    }

    private void validateId(Long id)
    {
        if (id == null || id <= 0)
        {
            throw new IllegalArgumentException("Invalid ID: Must be provided and greater than 0.");
        }
    }
}
