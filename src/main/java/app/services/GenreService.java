package app.services;

import app.dtos.GenreResultDTO;
import app.dtos.MovieDetailDTO;
import app.persistence.daos.IGenreDAO;

import java.util.List;

public class GenreService
{
    private final IGenreDAO genreDAO;

    public GenreService(IGenreDAO genreDAO)
    {
        this.genreDAO = genreDAO;
    }

    public void saveAllGenres(GenreResultDTO genreResultDTO)
    {
        if(genreResultDTO == null)
        {
            throw new IllegalArgumentException("List of genres cant be null");
        }

        genreResultDTO.genres().forEach(genre ->
        {
            genreDAO.create(genre);
        });

    }
}
