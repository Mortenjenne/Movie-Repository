package app.services;

import app.dtos.tmdb.TMDBGenreResultDTO;
import app.persistence.daos.IGenreDAO;

public class GenreService
{
    private final IGenreDAO genreDAO;

    public GenreService(IGenreDAO genreDAO)
    {
        this.genreDAO = genreDAO;
    }

    public void saveAllGenres(TMDBGenreResultDTO TMDBGenreResultDTO)
    {
        if(TMDBGenreResultDTO == null)
        {
            throw new IllegalArgumentException("List of genres cant be null");
        }

        TMDBGenreResultDTO.genres().forEach(genre ->
        {
            genreDAO.create(genre);
        });

    }
}
