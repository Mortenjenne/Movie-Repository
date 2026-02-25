package app.services;

import app.dtos.MovieDTO;
import app.entities.Movie;
import app.persistence.daos.IMovieDAO;
import app.utils.DTOMapper;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public class MovieSearchService
{
    private final IMovieDAO movieDAO;

    public MovieSearchService(IMovieDAO movieDAO)
    {
        this.movieDAO = movieDAO;
    }

    public List<MovieDTO> searchByTitle(String title)
    {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title must not be empty");
        }

        return movieDAO.searchByTitle(title)
                .stream()
                .map(DTOMapper::mapMovieToDTO)
                .toList();
    }

    public List<MovieDTO> getMoviesByGenre(String genreName)
    {
        return null;
    }
    public List<MovieDTO> getTopRated(int limit)
    {
        return null;
    }
    public List<MovieDTO> getLowestRated(int limit)
    {
        return null;
    }
    public List<MovieDTO> getMostPopular(int limit)
    {
        return null;
    }

    public Double getAverageRating()
    {
        return movieDAO.getAverageMovieRatings();
    }
}
