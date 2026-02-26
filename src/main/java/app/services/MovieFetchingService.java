package app.services;

import app.dtos.tmdb.TMDBGenreResultDTO;
import app.dtos.tmdb.TMDBMovieDTO;
import app.dtos.tmdb.TMDBMovieDetailDTO;
import app.dtos.tmdb.TMDBMovieResultDTO;
import app.entities.Genre;
import app.integrations.ITMDBClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MovieFetchingService
{
    private final ITMDBClient tmdbService;
    private static final int TMDB_MAX_PAGES = 500;

    public MovieFetchingService(ITMDBClient tmdbService)
    {
        this.tmdbService = tmdbService;
    }

    public List<TMDBMovieDetailDTO> getAllMovieDetails(Set<Long> movieIds)
    {
        List<TMDBMovieDetailDTO> TMDBMovieDetailDTOS = new ArrayList<>();

        movieIds.forEach(id ->
        {
            TMDBMovieDetailDTO TMDBMovieDetailDTO = tmdbService.getMovieDetailById(id);
            TMDBMovieDetailDTOS.add(TMDBMovieDetailDTO);
        });
        return TMDBMovieDetailDTOS;
    }

    public List<TMDBMovieDetailDTO> getMovieDetailsWithThreads(Set<Long> movieIds)
    {
        List<TMDBMovieDetailDTO> result = new ArrayList<>();

        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<Future<TMDBMovieDetailDTO>> futures = movieIds.stream()
                .map(id -> executorService.submit(() -> tmdbService.getMovieDetailById(id)))
                .toList();

        for (Future<TMDBMovieDetailDTO> future : futures)
        {
            try
            {
                result.add(future.get());
            }
            catch (InterruptedException | ExecutionException e)
            {
                throw new RuntimeException(e);
            }
        }

        executorService.shutdown();
        return result;
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