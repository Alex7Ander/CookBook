package ru.pavlov.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.pavlov.domain.Recipe;
import ru.pavlov.domain.User;
import ru.pavlov.domain.UserRole;
import ru.pavlov.repos.RecipeRepository;
import ru.pavlov.security.CookBookUserDetails;

@Controller
@RequestMapping("/user/**")
public class UserController {

	@Autowired
	private RecipeRepository recipeRepo;
	
	@GetMapping("cookbook")
	public String cookbook(Model model) {
		Iterable<Recipe> recipes = recipeRepo.findAll();
		model.addAttribute("recipes", recipes);
		return "cookbook";
	}
	
	@GetMapping("recipe")
	public String recipe(@RequestParam(name="name", required=true) String name, Model model) {
		List<Recipe> recipes = recipeRepo.findByName(name);
		Recipe recipe = recipes.get(0);
		model.addAttribute("recipe", recipe);
		return "recipe";
	}
	
	@GetMapping("addrecipe")
	public String addrecipe() {
		return "addrecipe";
	}
	
	@GetMapping("personal")
	public String personal(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, Model model) {
		User currentUser = currentUserDetails.getUser();
		for (UserRole r : currentUser.getRoles()) {
			System.out.println(r.getRole().toString());
		}
		model.addAttribute("user", currentUser);
		return "personal";
	}
	
//---------------------------------
	
	@PostMapping("saverecipe")
	public String saverecipe(@RequestParam String name, @RequestParam String type, @RequestParam String tagline, @RequestParam String text, Model model) {
		Recipe recipe = new Recipe(name, type, tagline, text);
		recipeRepo.save(recipe);
		Iterable<Recipe> recipes = recipeRepo.findAll();
		model.addAttribute("recipes", recipes);
		return "cookbook";
	}
}
