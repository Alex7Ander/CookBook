package ru.pavlov;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.pavlov.domain.Recipe;
import ru.pavlov.domain.User;
import ru.pavlov.repos.RecipeRepository;
import ru.pavlov.repos.UserRepository;

@Controller
public class MainController {

	@Autowired
	private RecipeRepository recipeRepo;
	@Autowired 
	private UserRepository userRepo;
	
	@GetMapping("regPage")
	public String regPage() {
		return "regPage";
	}
	
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
	
	@GetMapping("addrecipe")
	public String addrecipe() {
		return "addrecipe";
	}
	
	@GetMapping("personal")
	public String personal() {
		return "personal";
	}
	
	//---------------------------------
	
	@PostMapping("saverecipe")
	public String saverecipe(@RequestParam String name, @RequestParam String type, @RequestParam String tagline, @RequestParam String text, Map<String, Object> model) {
		Recipe recipe = new Recipe(name, type, tagline, text);
		recipeRepo.save(recipe);
		Iterable<Recipe> recipes = recipeRepo.findAll();
		model.put("recipes", recipes);
		return "cookbook";
	}
	
	@PostMapping("regUser")
	public String regUser(@RequestParam String login, @RequestParam String password, @RequestParam String email) {
		User user = new User(login, password, email);
		userRepo.save(user);
		return "login";
	}
}