package ru.pavlov.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ValueConstants;

import ru.pavlov.domain.Ingredient;
import ru.pavlov.domain.Recipe;
import ru.pavlov.domain.Review;
import ru.pavlov.domain.User;
import ru.pavlov.repos.IngredientRepository;
import ru.pavlov.repos.RecipeRepository;
import ru.pavlov.repos.ReviewRepository;
import ru.pavlov.repos.UserRepository;
import ru.pavlov.security.CookBookUserDetails;

@Controller
@RequestMapping("/user/**")
public class UserController {

	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RecipeRepository recipeRepo;
	
	@Autowired
	private IngredientRepository ingrRepo;
	
	@Autowired 
	private ReviewRepository reviewRepo;
	
	private String curentIngrType = null;
	private List<Ingredient> recipeIngredients = new ArrayList<>();
	
	@GetMapping("cookbook")
	public String cookbook(Model model) {
		Iterable<Recipe> recipes = recipeRepo.findAll();
		model.addAttribute("recipes", recipes);
		return "cookbook";
	}
	
	@GetMapping("recipe")
	public String recipe(@RequestParam(required = true, name="recipeId") Long recipeId, Model model) {
		Recipe recipe = recipeRepo.findById(recipeId);
		model.addAttribute("recipe", recipe);
		return "recipe";
	}
	

	
	@GetMapping("myrecipes")
	public String myRecipes(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, Model model) {
		User currentUser = currentUserDetails.getUser();
		List<Recipe> myRecipes = recipeRepo.findByUserId(currentUser.getId());
		model.addAttribute("recipes", myRecipes);
		return "myrecipes";
	}
	
	@GetMapping("ingredients")
	public String ingredients(Model model) {
		List<Ingredient> ingredients = ingrRepo.findAll();
		model.addAttribute("ingredients", ingredients);
		return "ingredients";
	}
	
	@GetMapping("personal")
	public String personal(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, Model model) {
		User currentUser = currentUserDetails.getUser();
		model.addAttribute("user", currentUser);
		return "personal";
	}
	
	@GetMapping("editpersonal")
	public String editPerson(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, Model model) {
		User currentUser = currentUserDetails.getUser();
		model.addAttribute("user", currentUser);
		return "editpersonal";
	}
	
	@GetMapping("reviewbook")
	public String reviewbook(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, Model model) {
		User currentUser = currentUserDetails.getUser();
		List<Review> reviews = reviewRepo.findByUserId(currentUser.getId());
		model.addAttribute("reviews", reviews);
		return "reviewbook";
	}
		
	@GetMapping("addrecipe")
	public String addrecipe(Model model) {
		model.addAttribute("recipeIngredients", recipeIngredients);
		List<String> ingrTypes = ingrRepo.getIngrTypes();
		model.addAttribute("ingrTypes", ingrTypes);		
		List<Ingredient> ingredients = ingrRepo.findByType(curentIngrType);
		model.addAttribute("ingredients", ingredients);
		return "addrecipe";
	}
	//AJAX 	
	@GetMapping("getIngrList")
	public String getIngrList(@RequestParam String type, Model model) {
		List<Ingredient> ingredients =  ingrRepo.findByType(type);
		model.addAttribute("ingredients", ingredients);
		return "ingrSelectElement";
	}
	
//---------------------------------	
	@PostMapping("addIngrToList")
	public String addIngrToList(@RequestParam String type, @RequestParam String name, Model model) {
		Ingredient ingr = ingrRepo.findByNameAndType(name, type);
		if (ingr != null) {
			this.recipeIngredients.add(ingr);
		}
		model.addAttribute("recipeIngredients", recipeIngredients);
		List<String> ingrTypes = ingrRepo.getIngrTypes();
		model.addAttribute("ingrTypes", ingrTypes);		
		List<Ingredient> ingredients = ingrRepo.findByType(curentIngrType);
		model.addAttribute("ingredients", ingredients);
		return "addrecipe";
	}
	

	
	@PostMapping("setCurentIngrType")
	public String setCurentIngrType(@RequestParam String type, Model model) {
		this.curentIngrType = type;
		model.addAttribute("recipeIngredients", recipeIngredients);
		List<String> ingrTypes = ingrRepo.getIngrTypes();
		model.addAttribute("ingrTypes", ingrTypes);		
		List<Ingredient> ingredients = ingrRepo.findByType(curentIngrType);
		model.addAttribute("ingredients", ingredients);
		return "addrecipe";
	}
	
	@PostMapping("saverecipe")
	public String saverecipe(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, 
								@RequestParam String name, 
								@RequestParam String type, 
								@RequestParam String tagline, 
								@RequestParam String text, 
								Model model) {
		Long userId= currentUserDetails.getUser().getId();
		Recipe recipe = new Recipe(userId, name, type, tagline, text, null);
		recipeRepo.save(recipe);
		Iterable<Recipe> recipes = recipeRepo.findAll();
		model.addAttribute("recipes", recipes);
		this.recipeIngredients.clear();
		return "cookbook";
	}
	
	@PostMapping("saveIngredient")
	@ResponseBody
	public String saveIngredient(@RequestParam String name, 
									@RequestParam String type, 
									@RequestParam String descr,
									@RequestParam String prot, 
									@RequestParam String fat, 
									@RequestParam String carbo) {		
		try {
			int protein = Integer.parseInt(prot);
			int fatInt = Integer.parseInt(fat);
			int carbohydrate = Integer.parseInt(carbo);
			Ingredient ingr = new Ingredient(name, type, descr, protein, fatInt, carbohydrate);
			this.ingrRepo.save(ingr);
			Integer id = ingr.getId();
			return id.toString();
		} catch (Exception exp) {
			return "0";
		}		
	}
	
	@PostMapping("editUser")
	public String edituser(@RequestParam(required = false, name="name") String name, 
							@RequestParam(required = false, name="surname") String surname,
							@RequestParam(required = false, name="city") String city,
							@RequestParam(required = false, name="temperament") String temperament,
							@RequestParam(required = false, name="phone") String phone,
							@AuthenticationPrincipal CookBookUserDetails currentUserDetails, Model model) {		
		User currentUser = currentUserDetails.getUser();
		if (name != ValueConstants.DEFAULT_NONE) currentUser.setName(name);
		if (surname != ValueConstants.DEFAULT_NONE) currentUser.setSurname(surname);
		if (city != ValueConstants.DEFAULT_NONE) currentUser.setCity(city);
		if (temperament != ValueConstants.DEFAULT_NONE) currentUser.setTemperament(temperament);
		if (phone != ValueConstants.DEFAULT_NONE) currentUser.setPhone(phone);
		this.userRepo.setUserInfoById(currentUser.getId(), currentUser.getName(), currentUser.getSurname(), currentUser.getCity(), currentUser.getTemperament(), currentUser.getPhone());
		model.addAttribute("user", currentUser);
		return "personal";
	}
}
