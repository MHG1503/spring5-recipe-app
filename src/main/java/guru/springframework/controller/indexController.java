package guru.springframework.controller;

import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class indexController {
    private final RecipeService recipeService;
    public indexController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }
    @RequestMapping({"","/","/index"})
    public String getIndexPage(Model model){
        log.debug("I'm in Index Controller");
        model.addAttribute("recipes",recipeService.getRecipes());
        return "index";
    }
}
