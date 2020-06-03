package ru.pavlov.controllers;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
import ru.pavlov.domain.User;
import ru.pavlov.repos.IngredientRepository;
import ru.pavlov.repos.RecipeRepository;
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
	
	
	private String curentIngrType = null;
	private List<Ingredient> newRecipeIngredients = new ArrayList<>();
	//private Map<Integer, byte[]> newRecipePhotos = new HashMap<>();
		
	@GetMapping("show")
	public String user(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, Model model){
		User currentUser = currentUserDetails.getUser();		
		model.addAttribute("user", currentUser);		
		String avatarPath = null;
		if (currentUser.getAvatarPath() != null) {
			avatarPath = "/uploadimg/" + currentUser.getAvatarPath();
		} 
		else {
			avatarPath = "/uploadimg/No_avatar.png";
		}		 
		model.addAttribute("avatarPath", avatarPath);
		
		Iterable<Recipe> recipes = recipeRepo.findByRecipeAuther(currentUser);
		model.addAttribute("recipes", recipes);
		return "user";
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
		/*
		this.userRepo.setUserInfoById(currentUser.getId(), 
				currentUser.getName(), 
				currentUser.getSurname(), 
				currentUser.getCity(), 
				currentUser.getTemperament(), 
				currentUser.getPhone());		
		*/
		if (avatar != null && !avatar.getOriginalFilename().isEmpty()) {
			File uploadDir = new File(uploadPath);
			if (!uploadDir.exists()) {
				uploadDir.mkdir();
			}
			String resultFileName = UUID.randomUUID().toString() + "." + avatar.getOriginalFilename();
			String path = uploadPath + "/avatars/" + resultFileName;
			File newFile = new File(path);
			avatar.transferTo(newFile);
			currentUser.setAvatarPath(resultFileName);
			//this.userRepo.setUserAvatarById(currentUser.getId(), resultFileName);
		}		
		userRepo.save(currentUser);
		model.addAttribute("user", currentUser);
		return "user";
	}
	
	//AJAX 	
	@PostMapping("getIngrList")
	public String getIngrList(@RequestParam String type, Model model) {
		List<Ingredient> ingredients = ingrRepo.findByType(type); 
		model.addAttribute("ingredients", ingredients);
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
			String jsonResponse = jsonCreator.writeValueAsString(ingr);
			return jsonResponse;
		}
		catch(JsonProcessingException jpExp) {
			System.out.println(jpExp.getMessage());
			return "{name:'Error'}";
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
	

	
}
