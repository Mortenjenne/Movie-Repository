package app.services;

import app.dtos.MovieDTO;
import app.dtos.MovieFullDetailDTO;
import app.dtos.UpdateMovieDTO;
import app.dtos.tmdb.TMDBMovieDetailDTO;

import java.util.List;
import java.util.Set;

public interface IMovieService
{
    MovieDTO submitMovie(TMDBMovieDetailDTO dto);

    MovieDTO updateMovie(UpdateMovieDTO updateMovieDTO);

    Set<MovieDTO> getAllMovies();

    MovieDTO findById(Long id);

    MovieFullDetailDTO getFullMovieDetail(Long id);

    void saveAllMovies(List<TMDBMovieDetailDTO> TMDBMovieDetailDTOS);
}
