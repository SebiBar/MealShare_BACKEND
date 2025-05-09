package com.example.MealShare.search;

import com.example.MealShare.recipe.RecipeService;
import com.example.MealShare.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
@Tag(name = "Search", description = "Search API for finding recipes and users")
public class SearchController {

    private final RecipeService recipeService;
    private final UserService userService;

    public SearchController(RecipeService recipeService, UserService userService) {
        this.recipeService = recipeService;
        this.userService = userService;
    }

    @Operation(summary = "Search recipes and users", description = "Searches for recipes and users matching the query string")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully performed search",
                content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Invalid query parameter", content = @Content)
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> searchAll(
            @Parameter(description = "Search query string", required = true) @RequestParam String query) {
        Map<String, Object> results = new HashMap<>();
        results.put("recipes", recipeService.searchRecipes(query));
        results.put("users", userService.searchUsers(query));
        return ResponseEntity.ok(results);
    }
}
