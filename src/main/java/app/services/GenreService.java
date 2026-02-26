package app.services;

import app.dtos.GenreDTO;
import app.entities.Genre;
import app.persistence.IGenreDAO;
import app.utils.DTOMapper;

import java.util.List;

public class GenreService implements IGenreService
{
    private final IGenreDAO genreDAO;

    public GenreService(IGenreDAO genreDAO)
    {
        this.genreDAO = genreDAO;
    }

    @Override
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

    @Override
    public GenreDTO submitGenre(Genre genre)
    {
        validateNotNull(genre);
        validateId(genre.getId());

        boolean exists = genreDAO.existsById(genre.getId());

        Genre result;
        if(exists)
        {
            result = genreDAO.update(genre);
        }
        else
        {
            result = genreDAO.create(genre);
        }

        return DTOMapper.mapGenreToDTO(result);
    }

    private void validateNotNull(Object exists)
    {
        if(exists == null)
        {
            throw new IllegalArgumentException("Movie cant be null");
        }
    }

    private void validateId(Long id)
    {
        if (id == null || id <= 0)
        {
            throw new IllegalArgumentException("Invalid ID: Must be provided and greater than 0.");
        }
    }
}
