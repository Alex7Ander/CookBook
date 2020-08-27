package ru.pavlov.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.pavlov.domain.Ingredient;
import ru.pavlov.domain.Recipe;
import ru.pavlov.domain.RecipePhoto;
import ru.pavlov.domain.Review;
import ru.pavlov.domain.User;
import ru.pavlov.repos.IngredientRepository;
import ru.pavlov.repos.IngredientVolumeRepository;
import ru.pavlov.repos.RecipePhotoRepository;
import ru.pavlov.repos.RecipeRepository;
import ru.pavlov.repos.ReviewRepository;
import ru.pavlov.repos.UserRepository;
import ru.pavlov.security.CookBookUserDetails;
import ru.pavlov.services.RecipeService;
import ru.pavlov.yandex.disk.YandexDiskConnector;
import ru.pavlov.yandex.disk.YandexDiskException;

@Controller
@RequestMapping("/cookbook/**") 
public class CookBookController {
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@Value("${upload.path}")
	private String token;
	
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
	
	@Autowired
	private RecipeService recipeService;
	
	@Autowired
	private YandexDiskConnector yandexDiskConnector;
	
	@GetMapping("showCookbook")
	public String cookbook(@RequestParam(required = false) String name, 
						   @RequestParam(required = false) String type,
						   @RequestParam(required = false) String tagline, Model model) {		
		Recipe recipeExampleObject = new Recipe();
		if(name != null && name.length() != 0) recipeExampleObject.setName(name);
		if(type != null && type.length() != 0) recipeExampleObject.setType(type);
		if(tagline != null && tagline.length() != 0) recipeExampleObject.setTagline(tagline);
		
		Iterable<Recipe> recipes = recipeService.findRecipiesLike(recipeExampleObject); 		
		for(Recipe recipe : recipes) {
			String avatarImageFilePath = new File(".").getAbsolutePath() + "/target/classes/static/img/" + recipe.getName() + "_preview.jpg";
	        try(FileOutputStream fos=new FileOutputStream(avatarImageFilePath))
	        {
	        	byte[] previewImageByteArray = recipe.getPreviewImage();
	        	if(previewImageByteArray != null) {
	        		fos.write(previewImageByteArray, 0, previewImageByteArray.length);
	        	}          
	        }
	        catch(IOException ioExp){              
	            System.out.println(ioExp.getMessage());
	        }
		}	
        model.addAttribute("recipes", recipes);        
		Iterable<User> users = userRepo.findAll();
		model.addAttribute("users", users);		
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
		//List<Ingredient> ingredients = ingrRepo.findAll();
		//model.addAttribute("ingredients", ingredients);
		List<String> ingrTypes = ingrRepo.getIngrTypes();
		model.addAttribute("ingrTypes", ingrTypes);
		return "ingredients";
	}
	
	@GetMapping("showReviewbook")
	public String reviewbook(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, Model model) {
		User currentUser = currentUserDetails.getUser();
		List<Review> reviews = reviewRepo.findByUserId(currentUser.getId());
		model.addAttribute("reviews", reviews);
		return "reviewbook";
	}
	
	@GetMapping("loadPreview")
	@ResponseBody
	public byte[] loadPreviews(@RequestParam long recipeId) {
		Recipe recipe = this.recipeRepo.findById(recipeId);
		byte[] previewImageByteArray = recipe.getPreviewImage();
		return previewImageByteArray;
	}
	
}