package ru.pavlov.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

	@Value("${upload.path}")
	private String uploadPath;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private RecipeRepository recipeRepo;
	
	@Autowired
	private IngredientRepository ingrRepo;
	
	@Autowired
	private ReviewRepository reviewRepo;
	
	private String curentIngrType = null;
	private List<Ingredient> newRecipeIngredients = new ArrayList<>();
		
	@GetMapping("show")
	public String user(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, Model model){
		User currentUser = currentUserDetails.getUser();
		model.addAttribute("user", currentUser);	
		if(currentUser.getActivationCode() == null) {
			System.out.println("Activation code is null. User is activated");
			model.addAttribute("activated", true);
		} 
		else {
			System.out.println("Activation code is " + currentUser.getActivationCode() + ". User is NOT activated");
			model.addAttribute("activated", false);
		}
		Iterable<Recipe> recipes = recipeRepo.findByRecipeAuther(currentUser);
		model.addAttribute("recipes", recipes);
		return "user";
	}
	
	@GetMapping("loadAvatar")
	@ResponseBody
	public byte[] loadAvatar(@RequestParam long userId) {
		User user = this.userRepo.findById(userId);
		byte[] avatarByteArray = user.getImage();
		return avatarByteArray;
	}
	
	@GetMapping("editpage")
	public String editPerson(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, Model model) {
		User currentUser = currentUserDetails.getUser();
		model.addAttribute("user", currentUser);
		return "edituserpage";
	}
	
	@PostMapping("edit")
	public String edituser(@RequestParam(required = false, name="name") String name, 
							@RequestParam(required = false, name="surname") String surname,
							@RequestParam(required = false, name="city") String city,
							@RequestParam(required = false, name="temperament") String temperament,
							@RequestParam(required = false, name="phone") String phone,
							@RequestParam(required = false, name="avatar") MultipartFile avatar,
							@AuthenticationPrincipal CookBookUserDetails currentUserDetails, Model model) throws IOException {		
		User currentUser = currentUserDetails.getUser();
		if (name != ValueConstants.DEFAULT_NONE) currentUser.setName(name);
		if (surname != ValueConstants.DEFAULT_NONE) currentUser.setSurname(surname);
		if (city != ValueConstants.DEFAULT_NONE) currentUser.setCity(city);
		if (temperament != ValueConstants.DEFAULT_NONE) currentUser.setTemperament(temperament);
		if (phone != ValueConstants.DEFAULT_NONE) currentUser.setPhone(phone);
		if(avatar != null && avatar.getBytes().length != 0) {
			byte[] avatarImageByteArray = avatar.getBytes();
			currentUser.setImage(avatarImageByteArray);				
		}
        String avatarFileName = currentUser.getName() + "_avatar.jpg";
        model.addAttribute("avatarPath", avatarFileName);
		userRepo.save(currentUser);
		model.addAttribute("user", currentUser);
		Iterable<Recipe> recipes = recipeRepo.findByRecipeAuther(currentUser);
		model.addAttribute("recipes", recipes);
		return "user";
	}
	
	//AJAX 	
	@PostMapping("getIngrList")
	public String getIngrList(@RequestParam String type, Model model) {
		
		System.out.println("1");
		List<Ingredient> ingredients = ingrRepo.findByType(type);
		
		System.out.println("2");
		model.addAttribute("ingredients", ingredients);
		

		System.out.println("3");
		return "ingrSelectElement";
	}
		
//---------------------------------	
	@GetMapping("addIngrToList")
	@ResponseBody
	public String addIngrToList(@RequestParam String type, @RequestParam String name, Model model){
		Ingredient ingr = ingrRepo.findByNameAndType(name, type);
		if (ingr != null) {
			this.newRecipeIngredients.add(ingr);
		}
		model.addAttribute("recipeIngredients", newRecipeIngredients);
		List<String> ingrTypes = ingrRepo.getIngrTypes();
		model.addAttribute("ingrTypes", ingrTypes);		
		List<Ingredient> ingredients = ingrRepo.findByType(curentIngrType);
		model.addAttribute("ingredients", ingredients);
		ObjectMapper jsonCreator = new ObjectMapper();
		try {
			return jsonCreator.writeValueAsString(ingr);
		}
		catch(JsonProcessingException jpExp) {
			System.out.println(jpExp.getMessage());
			return "{\"name\": \"Error\"}";
		}		
	}
		
	@PostMapping("setCurentIngrType")
	public String setCurentIngrType(@RequestParam String type, Model model) {
		this.curentIngrType = type;
		model.addAttribute("recipeIngredients", newRecipeIngredients);
		List<String> ingrTypes = ingrRepo.getIngrTypes();
		model.addAttribute("ingrTypes", ingrTypes);		
		List<Ingredient> ingredients = ingrRepo.findByType(curentIngrType);
		model.addAttribute("ingredients", ingredients);
		return "addrecipe";
	}
	
	@PostMapping("saveReview")
	@ResponseBody
	public String saveReview(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, @RequestParam String reviewText) {
		User user = currentUserDetails.getUser();
		Review review = new Review();
		review.setText(reviewText);
		review.setUserId(user.getId());
		reviewRepo.save(review);
		
		return "{\"done\": \"true\"}";
	}
	
}
