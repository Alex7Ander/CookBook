package ru.pavlov.controllers;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.pavlov.domain.IngredientVolume;
import ru.pavlov.domain.Recipe;
import ru.pavlov.domain.RecipePhoto;
import ru.pavlov.domain.User;
import ru.pavlov.repos.IngredientRepository;
import ru.pavlov.repos.IngredientVolumeRepository;
import ru.pavlov.repos.RecipePhotoRepository;
import ru.pavlov.repos.RecipeRepository;
import ru.pavlov.repos.ReviewRepository;
import ru.pavlov.repos.UserRepository;
import ru.pavlov.security.CookBookUserDetails;

@Controller
@RequestMapping("/recipe/**")
public class RecipeController {
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
	
	@GetMapping("show")
	public String recipe(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, @RequestParam(required = true, name="recipeId") Long recipeId, Model model) {
		Recipe recipe = recipeRepo.findById(recipeId);
		
		double totalRecipeCalorie = 0;
		for (IngredientVolume ingr : recipe.getIngredients()) {
			totalRecipeCalorie += ingr.getResultCalorie();
		}
		model.addAttribute("totalRecipeCalorie", totalRecipeCalorie);
		
		List<String> ingrTypes = ingrRepo.getIngrTypes();
		model.addAttribute("ingrTypes", ingrTypes);
		
		User currentUser = currentUserDetails.getUser();
		if (recipe.getRecipeAuther().equals(currentUser)) {
			model.addAttribute("editable", true);
		}
		else {
			model.addAttribute("editable", false);
		}
		model.addAttribute("recipe", recipe);	
		
		List<IngredientVolume> ingredients = recipe.getIngredients();
		model.addAttribute("ingredients", ingredients);
		
		List<RecipePhoto> recipePhotos = recipe.getPhotos();
		model.addAttribute("recipePhotos", recipePhotos);
		
		for (RecipePhoto rp : recipePhotos) {
			System.out.println(rp.getPhotoPath());
		}		
		return "recipe";
	}
	
	@PostMapping("editMainInfo")
	@ResponseBody
	public String editMainInfo(@RequestParam Map<String, String> allParametrs) {
		Long id = Long.parseLong(allParametrs.get("id"));
		allParametrs.remove("id");
		Recipe recipe = recipeRepo.findById(id);		
		for(String key : allParametrs.keySet()) {
			String value = allParametrs.get(key);
			try {
				Field field = recipe.getClass().getDeclaredField(key);
				field.setAccessible(true);
				field.set(recipe, value);	
			}
			catch(NoSuchFieldException | IllegalAccessException reflExp) {
				System.out.println("Ошибка при изменении значения поля: " + key);
				reflExp.printStackTrace();
			}
		}
		recipeRepo.save(recipe);
		return "{}";
	}
	
	@PostMapping("addNewIngredient")
	@ResponseBody
	public String addIngredient(@RequestParam long recipeId,
			 @RequestParam String name, 
			 @RequestParam String type, 
			 @RequestParam String descr,
			 @RequestParam String prot, 
			 @RequestParam String fat, 
			 @RequestParam String carbo) {
		return "";
	}
	
	@PostMapping("addExistingIngredient")
	@ResponseBody
	public String addExistingIngredient() {
		return "";
	}
	
	@PostMapping("deleteIngredient")
	@ResponseBody
	public String deleteIngredient(@RequestParam long recipeId, @RequestParam long ingredientId) {
		Recipe recipe = recipeRepo.findById(recipeId);		
		List<IngredientVolume> currenRecipeIngredients = recipe.getIngredients();
		IngredientVolume currentIngredient = ingrVolumeRepo.findById(ingredientId);
		for(IngredientVolume ingredietnFromList : currenRecipeIngredients) {
			if(ingredietnFromList.equals(currentIngredient)) {
				ingrVolumeRepo.delete(ingredietnFromList);
				recipeRepo.save(recipe);
				break;
			}
		}
		return "{}";
	}
	
	@PostMapping("editIngredientVolume")
	@ResponseBody
	public String editIngredientVolume(@RequestParam long recipeId, @RequestParam long ingredientId, @RequestParam double newValue) {
		Recipe recipe = recipeRepo.findById(recipeId);		
		List<IngredientVolume> currenRecipeIngredients = recipe.getIngredients();
		IngredientVolume currentIngredient = ingrVolumeRepo.findById(ingredientId);
		for(IngredientVolume ingredietnFromList : currenRecipeIngredients) {
			if(ingredietnFromList.equals(currentIngredient)) {
				ingredietnFromList.setVolume(newValue);
				ingrVolumeRepo.save(ingredietnFromList);
				break;
			}
		}		
		return "{}";
	}
		
}