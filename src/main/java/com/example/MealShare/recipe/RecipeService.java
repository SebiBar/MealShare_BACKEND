package com.example.memanager.recipe;

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

    public void updateRecipe(Recipe recipe) {
        // Logic to update a recipe
    }

    public void deleteRecipe(Long id) {
        // Logic to delete a recipe
    }
}
