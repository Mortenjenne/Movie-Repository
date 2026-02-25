package app.services;

import app.dtos.tmdb.TMDBGenreResultDTO;
import app.dtos.tmdb.TMDBMovieDTO;
import app.dtos.tmdb.TMDBMovieDetailDTO;
import app.dtos.tmdb.TMDBMovieResultDTO;
import app.entities.Genre;
import app.integrations.ITMBDClient;

import java.util.ArrayList;
import java.util.List;

public class MovieFetchingService
{
    private final ITMBDClient tmdbService;
    private static final int TMDB_MAX_PAGES = 500;

    public MovieFetchingService(ITMBDClient tmdbService) {
        this.tmdbService = tmdbService;
    }

    public List<TMDBMovieDetailDTO> getAllMovieDetails(List<Long> movieIds)
    {
        List<TMDBMovieDetailDTO> TMDBMovieDetailDTOS = new ArrayList<>();

        movieIds.forEach(id -> {

            TMDBMovieDetailDTO TMDBMovieDetailDTO = tmdbService.getMovieDetailById(id);
            TMDBMovieDetailDTOS.add(TMDBMovieDetailDTO);
        });
        return TMDBMovieDetailDTOS;
    }

    public List<Genre> getAllGenres()
    {
        TMDBGenreResultDTO TMDBGenreResultDTO = tmdbService.getAllGenres();
        return TMDBGenreResultDTO.genres();
    }

    public List<TMDBMovieDTO> getAllDaMovies(int maxPages)
    {
        List<TMDBMovieDTO> TMDBMovieDTOS = new ArrayList<>();
        int currentPage = 1;
        int totalPages;

        do
        {
            TMDBMovieResultDTO TMDBMovieResultDTO = tmdbService.getAllDaMovies(currentPage);

            totalPages = TMDBMovieResultDTO.totalPages();
            TMDBMovieDTOS.addAll(TMDBMovieResultDTO.TMDBMovieDTOS());

            currentPage++;

        }
        while (currentPage <= totalPages && currentPage <= maxPages && currentPage <= TMDB_MAX_PAGES);

        return TMDBMovieDTOS;
    }
}