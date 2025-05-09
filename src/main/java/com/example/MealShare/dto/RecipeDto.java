package com.example.MealShare.dto;

import com.example.MealShare.recipe.Ingredient;
import com.example.MealShare.recipe.Recipe;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDto {
    private Long id;
    private String title;
    private String description;
    private String link;
    private int prepTime;
    private int cookTime;
    private int servingSize;
    private int calories;
    private int protein;
    private int carbs;
    private int fat;
    private List<Ingredient> ingredients;
    
    // User information
    private Long userId;
    private String username;
    
    public static RecipeDto fromRecipe(Recipe recipe) {
        return RecipeDto.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .link(recipe.getLink())
                .prepTime(recipe.getPrepTime())
                .cookTime(recipe.getCookTime())
                .servingSize(recipe.getServingSize())
                .calories(recipe.getCalories())
                .protein(recipe.getProtein())
                .carbs(recipe.getCarbs())
                .fat(recipe.getFat())
                .ingredients(recipe.getIngredients())
                .userId(recipe.getUser().getId())
                .username(recipe.getUser().getUsername())
                .build();
    }
}