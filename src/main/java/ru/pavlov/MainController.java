package ru.pavlov;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.pavlov.domain.Recipe;
import ru.pavlov.repos.RecipeRepo;

@Controller
public class MainController {

	@Autowired
	private RecipeRepo recipeRepo;
	
	@GetMapping("cookbook")
	public String cookbook(Map<String, Object> model) {
		Iterable<Recipe> recipes = recipeRepo.findAll();
		model.put("recipes", recipes);
		return "cookbook";
	}
	
	@GetMapping("recipe")
	public String recipe(@RequestParam(name="name", required=true) String name, Map<String, Object> model) {
		List<Recipe> recipes = recipeRepo.findByName(name);
		Recipe recipe = recipes.get(0);
		model.put("recipe", recipe);
		return "recipe";
	}
}
