package guru.springframework.services;

import guru.springframework.domain.Ingredient;

public interface IngredientService {
    Ingredient findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId);

    Ingredient saveIngredient(Ingredient ingredient);
}
