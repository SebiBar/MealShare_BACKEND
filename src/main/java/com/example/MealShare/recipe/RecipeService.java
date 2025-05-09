package com.example.MealShare.recipe;

import com.example.MealShare.dto.RecipeDto;
import com.example.MealShare.exceptions.ResourceNotFoundException;
import com.example.MealShare.exceptions.UnauthorizedException;
import com.example.MealShare.user.UserRepository;
import com.example.MealShare.user.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    public RecipeService(RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    public RecipeDto getRecipeById(Long id) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
        return RecipeDto.fromRecipe(recipe);
    }

    public List<Recipe> getRecipesByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return user.getRecipes();
    }

    public List<RecipeDto> searchRecipes(String query) {
        return recipeRepository.findByTitleContainingIgnoreCase(query)
                .stream()
                .map(RecipeDto::fromRecipe)
                .collect(Collectors.toList());
    }

    public Recipe createRecipeForUser(Recipe recipe, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        recipe.setUser(user);

        // Set the recipe reference for each ingredient
        if (recipe.getIngredients() != null && !recipe.getIngredients().isEmpty()) {
            recipe.getIngredients().forEach(ingredient -> {
                ingredient.setRecipe(recipe);
            });
        }

        return recipeRepository.save(recipe);
    }

    public Recipe updateRecipeIfOwner(Long recipeId, Recipe updatedRecipe, String username) {
        Recipe existingRecipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        // Check if the authenticated user is the owner
        if (!existingRecipe.getUser().getUsername().equals(username)) {
            throw new UnauthorizedException("You don't have permission to update this recipe");
        }

        // Update recipe fields
        existingRecipe.setTitle(updatedRecipe.getTitle());
        existingRecipe.setDescription(updatedRecipe.getDescription());
        // Update other fields as needed

        // Clear and re-add ingredients
        existingRecipe.getIngredients().clear();
        updatedRecipe.getIngredients().forEach(ingredient -> {
            ingredient.setRecipe(existingRecipe);
            existingRecipe.getIngredients().add(ingredient);
        });

        return recipeRepository.save(existingRecipe);
    }

    public void deleteRecipeIfOwner(Long recipeId, String username) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));

        // Check if the authenticated user is the owner
        if (!recipe.getUser().getUsername().equals(username)) {
            throw new UnauthorizedException("You don't have permission to delete this recipe");
        }

        recipeRepository.delete(recipe);
    }
}
