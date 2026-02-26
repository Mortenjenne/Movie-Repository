package app.services;

import app.dtos.MovieDTO;
import app.persistence.IMovieDAO;
import app.utils.DTOMapper;

import java.util.List;

public class MovieSearchService implements IMovieSearchService {
    private final IMovieDAO movieDAO;

    public MovieSearchService(IMovieDAO movieDAO)
    {
        this.movieDAO = movieDAO;
    }

    @Override
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

    @Override
    public List<MovieDTO> getTopRated(int limit)
    {
        return movieDAO.getMoviesByHighestRating(limit)
                .stream()
                .map(DTOMapper::mapMovieToDTO)
                .toList();
    }
    @Override
    public List<MovieDTO> getLowestRated(int limit)
    {
        return movieDAO.getMoviesByLowestRating(limit)
                .stream()
                .map(DTOMapper::mapMovieToDTO)
                .toList();
    }

    // Will not be implemented
    @Override
    public List<MovieDTO> getMostPopular(int limit)
    {
        return null;
    }

    @Override
    public List<MovieDTO> searchByActor(String actorName)
    {
        return movieDAO.getMoviesByActor(actorName)
                .stream()
                .map(DTOMapper::mapMovieToDTO)
                .toList();
    }

    @Override
    public List<MovieDTO> searchByDirector(String directorName)
    {
        return movieDAO.getMoviesByDirector(directorName)
                .stream()
                .map(DTOMapper::mapMovieToDTO)
                .toList();
    }

    @Override
    public List<MovieDTO> getMoviesByGenre(String genreName)
    {
        return movieDAO.getMoviesByGenre(genreName)
                .stream()
                .map(DTOMapper::mapMovieToDTO)
                .toList();
    }

    @Override
    public Double getAverageRating()
    {
        return movieDAO.getAverageMovieRatings();
    }
}
