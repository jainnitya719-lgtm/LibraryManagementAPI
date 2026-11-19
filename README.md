# Library Management REST API (Spring Boot + JdbcTemplate + MySQL)

## Run
1. Install JDK 17 and MySQL.
2. Run `database.sql` in MySQL (creates `library_db` and `books`).
3. Set `spring.datasource.password` in `src/main/resources/application.properties`.
4. `mvn spring-boot:run` (or run `LibraryApplication.java` from your IDE).
5. Import `postman/Library-API.postman_collection.json` into Postman.

## Endpoints
| Operation | Method & URL | Response |
|---|---|---|
| Add book | POST /api/books | 201 / 400 / 409 |
| Get all | GET /api/books | 200 |
| Get one | GET /api/books/{id} | 200 / 404 |
| Update | PUT /api/books/{id} | 200 / 400 / 404 |
| Delete | DELETE /api/books/{id} | 200 / 404 |
