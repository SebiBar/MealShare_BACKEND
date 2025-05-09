package com.example.MealShare.recipe;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Recipes", description = "Recipe management API")
public class RecipeController {
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Operation(summary = "Get user recipes", description = "Retrieves all recipes created by a specific user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved recipes",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class))),
        @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
    })
    @GetMapping("/users/{userId}/recipes")
    public ResponseEntity<List<Recipe>> getUserRecipes(@PathVariable Long userId) {
        List<Recipe> recipes = recipeService.getRecipesByUserId(userId);
        return ResponseEntity.ok(recipes);
    }

    @Operation(summary = "Get recipe by ID", description = "Retrieves a specific recipe by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved recipe",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class))),
        @ApiResponse(responseCode = "404", description = "Recipe not found", content = @Content)
    })
    @GetMapping("/recipes/{recipeId}")
    public ResponseEntity<Recipe> getRecipe(@PathVariable Long recipeId) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        return ResponseEntity.ok(recipe);
    }

    @Operation(summary = "Create recipe", description = "Creates a new recipe for the authenticated user")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Recipe successfully created",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class))),
        @ApiResponse(responseCode = "400", description = "Invalid recipe data", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    })
    @PostMapping("/recipes")
    public ResponseEntity<?> createRecipe(@RequestBody Recipe recipe, Authentication authentication) {
        Recipe createdRecipe = recipeService.createRecipeForUser(recipe, authentication.getName());
        return new ResponseEntity<>(createdRecipe, HttpStatus.CREATED);
    }

    @Operation(summary = "Update recipe", description = "Updates an existing recipe if the authenticated user is the owner")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recipe successfully updated",
                content = @Content(mediaType = "application/json", schema = @Schema(implementation = Recipe.class))),
        @ApiResponse(responseCode = "400", description = "Invalid recipe data", content = @Content),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden - not the recipe owner", content = @Content),
        @ApiResponse(responseCode = "404", description = "Recipe not found", content = @Content)
    })
    @PutMapping("/recipes/{recipeId}")
    public ResponseEntity<?> updateRecipe(@PathVariable Long recipeId,
                                          @RequestBody Recipe recipe,
                                          Authentication authentication) {
        Recipe updatedRecipe = recipeService.updateRecipeIfOwner(recipeId, recipe, authentication.getName());
        return ResponseEntity.ok(updatedRecipe);
    }

    @Operation(summary = "Delete recipe", description = "Deletes a recipe if the authenticated user is the owner")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recipe successfully deleted"),
        @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
        @ApiResponse(responseCode = "403", description = "Forbidden - not the recipe owner", content = @Content),
        @ApiResponse(responseCode = "404", description = "Recipe not found", content = @Content)
    })
    @DeleteMapping("/recipes/{recipeId}")
    public ResponseEntity<?> deleteRecipe(@PathVariable Long recipeId, Authentication authentication) {
        recipeService.deleteRecipeIfOwner(recipeId, authentication.getName());
        return ResponseEntity.ok("Recipe deleted successfully");
    }
}
