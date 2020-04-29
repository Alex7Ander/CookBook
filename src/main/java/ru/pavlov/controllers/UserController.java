package ru.pavlov.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import ru.pavlov.domain.RecipePhoto;
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
	private Map<Integer, byte[]> newRecipePhotos = new HashMap<>();
	
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
		List<Ingredient> ingredients = recipe.getIngredients();
		model.addAttribute("ingredients", ingredients);
		return "recipe";
	}
	
	@GetMapping("myrecipes")
	public String myRecipes(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, Model model) {
		User currentUser = currentUserDetails.getUser();
		List<Recipe> myRecipes = currentUser.getRecipes();
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
	public String personal(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, Model model){
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
		
		Iterable<Recipe> recipes = recipeRepo.findByCooker(currentUser);
		model.addAttribute("recipes", recipes);
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
		model.addAttribute("recipeIngredients", newRecipeIngredients);
		List<String> ingrTypes = ingrRepo.getIngrTypes();
		model.addAttribute("ingrTypes", ingrTypes);		
		List<Ingredient> ingredients = ingrRepo.findByType(curentIngrType);
		model.addAttribute("ingredients", ingredients);
		return "addrecipe";
	}
	//AJAX 	
	@GetMapping("getIngrList")
	public String getIngrList(@RequestParam String type, Model model) {
		List<Ingredient> ingredients = ingrRepo.findByType(type); //Ingredient
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
			//jpExp.printStackTrace();
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
	
	@PostMapping("editUser")
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
		this.userRepo.setUserInfoById(currentUser.getId(), 
				currentUser.getName(), 
				currentUser.getSurname(), 
				currentUser.getCity(), 
				currentUser.getTemperament(), 
				currentUser.getPhone());		
		
		if (avatar != null && !avatar.getOriginalFilename().isEmpty()) {
			File uploadDir = new File(uploadPath);
			if (!uploadDir.exists()) {
				uploadDir.mkdir();
			}
			String resultFileName = UUID.randomUUID().toString() + "." + avatar.getOriginalFilename();
			String path = uploadPath + "/" + resultFileName;
			File newFile = new File(path);
			avatar.transferTo(newFile);
			currentUser.setAvatarPath(resultFileName);
			this.userRepo.setUserAvatarById(currentUser.getId(), resultFileName);
		}		
		model.addAttribute("user", currentUser);
		return "personal";
	}
	
	@PostMapping("addRecipePhoto")
	@ResponseBody
	public String addRecipePhoto(@RequestParam(required = false, name="photo") MultipartFile photo) throws IOException {
		Integer hashCode = photo.hashCode();
		System.out.println("Photo uploaded. Its code is: " + hashCode.toString());
		byte[] byteArray = photo.getBytes();
		this.newRecipePhotos.put(hashCode, byteArray);		
		return hashCode.toString();
	}
	
	@PostMapping("deleteRecipePhoto")
	@ResponseBody
	public String deleteRecipePhoto(@RequestParam String code) {
		Integer reqCode = null;
		try {
			reqCode = Integer.parseInt(code);
			this.newRecipePhotos.remove(reqCode);
		} 
		catch(NumberFormatException nfExp) {
			return "";
		}		
		return "0";
	}
	
	@PostMapping("saverecipe")
	//@ResponseBody
	public String saverecipe(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, 
								@RequestParam Map<String, String> allParametrs,
								Model model) throws IOException {
		
		for (String key: allParametrs.keySet()) {
			System.out.println(key + ": " + allParametrs.get(key));
		}
		System.out.println("-----------------------------------");
		/*
		 						@RequestParam String name, 
								@RequestParam String type, 
								@RequestParam String tagline,
								@RequestParam String youtubeLink,
								@RequestParam String text,
		 */
		/*
		User currentUser = currentUserDetails.getUser();
		List<RecipePhoto> photos = new ArrayList<>();
		
		Recipe recipe = new Recipe(currentUser, name, type, tagline, youtubeLink, text, this.newRecipeIngredients);
		for (Integer photoKey : this.newRecipePhotos.keySet()) {
			byte[] byteArray = this.newRecipePhotos.get(photoKey);
			if (byteArray.length != 0) {				
				String recipePhotoFolder = uploadPath + name + "_" + type + "_" + UUID.randomUUID().toString();
				File uploadDir = new File(recipePhotoFolder);
				if (!uploadDir.exists()) {
					uploadDir.mkdir();
				}
				String resultFullPhotoName = recipePhotoFolder + "/" + UUID.randomUUID().toString() + ".jpg";
				FileOutputStream fos = new FileOutputStream(resultFullPhotoName);
				fos.write(byteArray);
				fos.close();
				
				RecipePhoto uploadedPhoto = new RecipePhoto(resultFullPhotoName);
				photos.add(uploadedPhoto);
			}
		}
		recipe.setPhotos(photos);
		recipeRepo.save(recipe);
		Iterable<Recipe> recipes = recipeRepo.findAll();
		model.addAttribute("recipes", recipes);
		this.newRecipeIngredients.clear();*/
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
}
