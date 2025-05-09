# MealShare

MealShare is a Spring Boot application that allows users to share and discover recipes. Users can create, update, and delete their own recipes, as well as search for recipes and users.

## Features

- User authentication and authorization with JWT
- Recipe management (create, read, update, delete)
- Recipe search functionality
- User search functionality
- Nutritional information tracking

## Technologies Used

- Java 17
- Spring Boot 3.x
- Spring Security with JWT authentication
- Spring Data JPA
- Hibernate
- Swagger/OpenAPI for API documentation
- Lombok for reducing boilerplate code
- Gradle for build management

## API Documentation

The API documentation is available at `http://localhost:8080/swagger-ui.html` when the application is running.

### Authentication Endpoints

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/api/auth/login` | Authenticate user | `LoginRequest` (username, password) | JWT token |
| POST | `/api/auth/register` | Register new user | `RegisterRequest` (username, email, password) | Success message |

### Recipe Endpoints

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/users/{userId}/recipes` | Get all recipes by user ID | - | List of recipes |
| GET | `/api/recipes/{recipeId}` | Get recipe by ID | - | Recipe details |
| POST | `/api/recipes` | Create new recipe | Recipe object | Created recipe |
| PUT | `/api/recipes/{recipeId}` | Update recipe | Recipe object | Updated recipe |
| DELETE | `/api/recipes/{recipeId}` | Delete recipe | - | Success message |

### Search Endpoints

| Method | Endpoint | Description | Request Params | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/search?query={query}` | Search recipes and users | `query` (search term) | Map with recipes and users |

## Recipe Model

A recipe in MealShare includes:

- Title
- Description
- Link to external recipe source (optional)
- Preparation time
- Cooking time
- Serving size
- Nutritional information (calories, protein, carbs, fat)
- List of ingredients

## Security

The application uses JWT (JSON Web Tokens) for authentication. To access protected endpoints, you need to:

1. Obtain a token by logging in via `/api/auth/login`
2. Include the token in the Authorization header of subsequent requests:
   ```
   Authorization: Bearer your_token_here
   ```