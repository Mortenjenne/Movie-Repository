package app.services;

import app.dtos.MovieDTO;

import java.util.List;

public interface IMovieSearchService {
    List<MovieDTO> searchByTitle(String title);

    List<MovieDTO> getTopRated(int limit);

    List<MovieDTO> getLowestRated(int limit);

    List<MovieDTO> getMostPopular(int limit);

    List<MovieDTO> searchByActor(String actorName);

    List<MovieDTO> searchByDirector(String directorName);

    List<MovieDTO> getMoviesByGenre(String genreName);

    Double getAverageRating();
}
