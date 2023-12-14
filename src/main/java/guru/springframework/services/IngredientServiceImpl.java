package guru.springframework.services;

import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

    private final RecipeRepository recipeRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public IngredientServiceImpl(RecipeRepository recipeRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.recipeRepository = recipeRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public Ingredient findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

        if (!recipeOptional.isPresent()) {
            log.error("recipe id not found. Id: " + recipeId);
        }

        Recipe recipe = recipeOptional.get();

        Optional<Ingredient> ingredientOptional = recipe
                .getIngredients()
                .stream()
                .filter(s -> s.getId().equals(ingredientId))
                .findFirst();

        if (!ingredientOptional.isPresent()) {
            log.error("ingredient id not found. Id: " + ingredientId);
        }
        System.out.println(ingredientOptional.get().getRecipe() == null ? "true" : false);
        return ingredientOptional.get();
    }

    @Override
    @Transactional
    public Ingredient saveIngredient(Ingredient ingredient) {
        Optional<Recipe> recipeOptional = recipeRepository.findById(ingredient.getRecipe().getId());

        if (!recipeOptional.isPresent()) {

            //todo toss error if not found!
            log.error("Recipe not found for id: " + ingredient.getRecipe().getId());
            return new Ingredient();
        } else {
            Recipe recipe = recipeOptional.get();

            Optional<Ingredient> ingredientOptional = recipe
                    .getIngredients()
                    .stream()
                    .filter(ing -> ing.getId().equals(ingredient.getId()))
                    .findFirst();

            if (ingredientOptional.isPresent()) {
                Ingredient ingredientFound = ingredientOptional.get();
                ingredientFound.setDescription(ingredient.getDescription());
                ingredientFound.setAmount(ingredient.getAmount());
                ingredientFound.setUom(unitOfMeasureRepository
                        .findById(ingredient.getUom().getId())
                        .orElseThrow(() -> new RuntimeException("UOM NOT FOUND"))); //todo address this
            } else {
                //add new Ingredient
                recipe.addIngredient(new Ingredient());
            }

            Recipe savedRecipe = recipeRepository.save(recipe);

            //to do check for fail
            return savedRecipe.getIngredients().stream()
                    .filter(recipeIngredients -> recipeIngredients.getId().equals(ingredient.getId()))
                    .findFirst()
                    .get();
        }
    }
}
