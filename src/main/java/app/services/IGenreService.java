package app.services;

import app.dtos.GenreDTO;
import app.entities.Genre;

import java.util.List;

public interface IGenreService
{
    void saveAllGenres(List<Genre> genres);
    GenreDTO submitGenre(Genre genre);
}
