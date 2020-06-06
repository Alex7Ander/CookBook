package ru.pavlov.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.pavlov.domain.Ingredient;
import ru.pavlov.domain.Recipe;
import ru.pavlov.domain.Review;
import ru.pavlov.domain.User;
import ru.pavlov.repos.IngredientRepository;
import ru.pavlov.repos.IngredientVolumeRepository;
import ru.pavlov.repos.RecipePhotoRepository;
import ru.pavlov.repos.RecipeRepository;
import ru.pavlov.repos.ReviewRepository;
import ru.pavlov.repos.UserRepository;
import ru.pavlov.security.CookBookUserDetails;

@Controller
@RequestMapping("/cookbook/**") 
public class CookBookController {
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RecipeRepository recipeRepo;
	
	@Autowired
	private IngredientRepository ingrRepo;
	
	@Autowired
	private IngredientVolumeRepository ingrVolumeRepo;
	
	@Autowired
	private RecipePhotoRepository recipePhotoRepo;	
	
	@Autowired 
	private ReviewRepository reviewRepo;
	
	@GetMapping("showCookbook")
	public String cookbook(@RequestParam(required = false) String name, 
						   @RequestParam(required = false) String type,
						   @RequestParam(required = false) String tagline,
						   @RequestParam(required = false) String auther, Model model) {
		Iterable<Recipe> recipes = recipeRepo.findAll();
		model.addAttribute("recipes", recipes);
		/*
		List<String> photoPaths = new ArrayList<>();
		for(Recipe recipe : recipes) {
			String path = uploadPath + "/" + recipe.getPhotoFolder();
			RecipePhoto rPhoto = null;
			try {
				rPhoto = recipe.getPhotos().get(0);
				path += ("/" + rPhoto.getPhotoPath());
			}
			catch(Exception exp) {
				System.out.println("В рецепте " + recipe.getName() + " нет фото");
			}	
			photoPaths.add(path);
		}
		model.addAttribute("photoPaths", photoPaths);
		*/
		return "cookbook";
	}
	
	@GetMapping("addrecipe")
	public String addrecipe(Model model) {
		List<String> ingrTypes = ingrRepo.getIngrTypes();
		model.addAttribute("ingrTypes", ingrTypes);		
		return "addrecipe";
	}
	
	@GetMapping("showIngredients")
	public String ingredients(Model model) {		
		List<Ingredient> ingredients = ingrRepo.findAll();
		model.addAttribute("ingredients", ingredients);
		return "ingredients";
	}
	
	@GetMapping("showReviewbook")
	public String reviewbook(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, Model model) {
		User currentUser = currentUserDetails.getUser();
		List<Review> reviews = reviewRepo.findByUserId(currentUser.getId());
		model.addAttribute("reviews", reviews);
		return "reviewbook";
	}
	
}