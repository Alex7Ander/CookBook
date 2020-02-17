package ru.pavlov;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.pavlov.domain.Recipe;
import ru.pavlov.domain.User;
import ru.pavlov.repos.RecipeRepository;
import ru.pavlov.repos.UserRepository;
import ru.pavlov.security.CookBookUserDetails;

@Controller
public class MainController {

	@Autowired
	private RecipeRepository recipeRepo;
	@Autowired 
	private UserRepository userRepo;

// /**
/*	@GetMapping("/login")
	public String login() {
		return "login";
	}
*/	
	@GetMapping("regPage")
	public String regPage() {
		return "regPage";
	}
	
	@PostMapping("regUser")
	public String regUser(@RequestParam String login, @RequestParam String password, @RequestParam String email) {
		User user = new User(login, password, email);
		userRepo.save(user);
		return "redirect:/login";
	}
	
// /user/**	
	@GetMapping("user/cookbook")
	public String cookbook(Model model) {
		Iterable<Recipe> recipes = recipeRepo.findAll();
		model.addAttribute("recipes", recipes);
		return "cookbook";
	}
	
	@GetMapping("user/recipe")
	public String recipe(@RequestParam(name="name", required=true) String name, Model model) {
		List<Recipe> recipes = recipeRepo.findByName(name);
		Recipe recipe = recipes.get(0);
		model.addAttribute("recipe", recipe);
		return "recipe";
	}
	
	@GetMapping("user/addrecipe")
	public String addrecipe() {
		return "addrecipe";
	}
	
	@GetMapping("user/personal")
	public String personal(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, Model model) {
		User currentUser = currentUserDetails.getUser();
		model.addAttribute("user", currentUser);
		return "personal";
	}
	
//---------------------------------
	
	@PostMapping("user/saverecipe")
	public String saverecipe(@RequestParam String name, @RequestParam String type, @RequestParam String tagline, @RequestParam String text, Model model) {
		Recipe recipe = new Recipe(name, type, tagline, text);
		recipeRepo.save(recipe);
		Iterable<Recipe> recipes = recipeRepo.findAll();
		model.addAttribute("recipes", recipes);
		return "cookbook";
	}

}