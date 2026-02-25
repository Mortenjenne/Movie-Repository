package app.integrations;

import app.dtos.tmdb.TMDBGenreResultDTO;
import app.dtos.tmdb.TMDBMovieDetailDTO;
import app.dtos.tmdb.TMDBMovieResultDTO;

public interface ITMBDClient
{
    TMDBMovieDetailDTO getMovieDetailById(Long id);

    TMDBGenreResultDTO getAllGenres();

    TMDBMovieResultDTO getAllDaMovies(int currentPage);
}