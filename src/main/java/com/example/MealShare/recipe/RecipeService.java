package com.example.MealShare.recipe;

import org.springframework.stereotype.Service;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeService(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public void createRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    public Recipe getRecipeById(Long id) {
        return recipeRepository.getReferenceById(id);
    }

    public Recipe updateRecipe(Recipe updatedRecipe) {
        Recipe existingRecipe = recipeRepository.getReferenceById(updatedRecipe.getId());

        // Update recipe fields
        existingRecipe.setTitle(updatedRecipe.getTitle());
        existingRecipe.setDescription(updatedRecipe.getDescription());
        // Update other recipe fields as needed

        // Clear and re-add ingredients to maintain proper tracking
        existingRecipe.getIngredients().clear();

        // Ensure each ingredient references this recipe
        updatedRecipe.getIngredients().forEach(ingredient -> {
            ingredient.setRecipe(existingRecipe);
            existingRecipe.getIngredients().add(ingredient);
        });

        return recipeRepository.save(existingRecipe);
    }

    public void deleteRecipe(Long id) {
        recipeRepository.deleteById(id);
    }
}
