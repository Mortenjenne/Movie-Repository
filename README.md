# Movie Repository (TMDb Integration) — SP-1

Backend project for the **SP-1 Movie Repository** assignment.  
The goal is to **fetch movie data from the TMDb API**, map JSON into **DTOs**, and (in the full assignment scope) **store and query** the data using **JPA/Hibernate** with a DAO + service architecture.

> [The Movie Database API](https://www.themoviedb.org/)  

---

## Features (current state)

- Fetch movies from TMDB using Java `HttpClient`
- JSON mapping with Jackson into DTOs:
  - `MovieDTO`
  - `MovieResultDTO`
- Service layer (used from `Main`) for higher-level operations, e.g.:
  - Fetch movies by rating range (paged)
  - Sort/filter movies by release date based on a title search string

---

## How it works (architecture)

### DTO layer
TMDB JSON responses are mapped into DTOs using Jackson annotations.

Example DTO fields:
- `id`
- `original_title`
- `overview`
- `vote_average`
- `release_date` (`LocalDate`)

### Integration layer
`TMBDService` (note the name) constructs TMDB URLs and performs HTTP requests.  
It returns DTOs (`MovieDTO`, `MovieResultDTO`) to the service layer.

### Service layer
`MovieService` is used by `Main` to implement application use cases (rating queries, sorting, etc.), and is the intended place to:
- convert DTOs ↔ Entities
- orchestrate persistence via DAOs

### Persistence layer 

- Entities stored in PostgreSQL via JPA/Hibernate
- DAO classes providing CRUD
- Service layer converting between DTOs and Entities

---

## Assignment Requirements (what this project targets)

From the assignment description, the final backend should be able to:

- Store and retrieve information about:
  - movies
  - actors
  - directors
  - genres
- Fetch Danish movies released in the last 5 years (one-time ingestion)
- Provide queries such as:
  - list all movies
  - search movies by (case-insensitive) title substring
  - list genres and movies by genre
  - top-10 lowest/highest rated movies
  - top-10 most popular movies
- CRUD operations for movies (at least title + release date)
- Tests using **JUnit** and **Testcontainers**

---

## Contributors

Group assignment repository: **Mortenjenne/Movie-Repository**  
[Daniel Hangaard](https://github.com/DHangaard)
[Morten Jensen](https://github.com/mortenjenne/)

---

## License

The source code in this repository is licensed under the **MIT License**. See the [LICENSE](./LICENSE) file for details.
