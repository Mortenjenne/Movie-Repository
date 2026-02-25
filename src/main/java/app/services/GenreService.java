package app.services;

import app.dtos.tmdb.TMDBGenreResultDTO;
import app.entities.Genre;
import app.persistence.daos.IGenreDAO;

import java.util.List;

public class GenreService
{
    private final IGenreDAO genreDAO;

    public GenreService(IGenreDAO genreDAO)
    {
        this.genreDAO = genreDAO;
    }

    public void saveAllGenres(List<Genre> genres)
    {
        if(genres == null)
        {
            throw new IllegalArgumentException("List of genres cant be null");
        }

        genres.forEach(genre ->
        {
            genreDAO.create(genre);
        });

    }
}
