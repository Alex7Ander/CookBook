package ru.pavlov.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
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
import org.springframework.web.multipart.MultipartFile;

import ru.pavlov.aws.AWSConnector;
import ru.pavlov.domain.Ingredient;
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
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@Autowired
	private AWSConnector awsConnector;
	
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
	
	private Map<Integer, byte[]> newRecipePhotos = new HashMap<>();
	
	@GetMapping("show")
	public String recipe(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, @RequestParam(required = true, name="recipeId") Long recipeId, Model model) {
		Recipe recipe = recipeRepo.findById(recipeId);
		model.addAttribute("recipe", recipe);

		double totalRecipeCalorie = 0;
		for (IngredientVolume ingr : recipe.getIngredients()) {
			try {
				totalRecipeCalorie += ingr.getResultCalorie();
			}			
			catch(NullPointerException npExp) {
				npExp.printStackTrace();
			}
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
		
		List<IngredientVolume> ingredientsVolumes = recipe.getIngredients();
		model.addAttribute("ingredientsVolumes", ingredientsVolumes);
		
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
		String response = "{\"wrongFields\":\"[";
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
				response += (key + ", ");
			}
		}
		response += "]\"}";
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
	public String addExistingIngredient(@RequestParam long recipeId, @RequestParam long ingredientId, @RequestParam double volume) {
		Recipe recipe = recipeRepo.findById(recipeId);
		Ingredient ingredient = ingrRepo.findById(ingredientId);
		
		IngredientVolume ingrVolume = new IngredientVolume(ingredient, volume, recipe);
		ingrVolumeRepo.save(ingrVolume);
		Long ingredientVolumeId = ingrVolume.getId();
		
		System.out.println("В рецепт " + recipe.getName() + " добавлене ингредиент " + ingrVolume.getName() + 
				".\nИдентификатор записи в тблице ingredientVolume - " + ingredientVolumeId.toString());
		
		recipe.getIngredients().add(ingrVolume);
		recipeRepo.save(recipe);
		return "{\"message\": \"success\", \"ingredientVolumeId\" : \""+ingredientVolumeId.toString()+"\"}";
	}
	
	@PostMapping("deleteIngredient")
	@ResponseBody
	public String deleteIngredient(@RequestParam long recipeId, @RequestParam long ingredientVolumeId) {
		Recipe recipe = recipeRepo.findById(recipeId);		
		List<IngredientVolume> currentRecipeIngredientsVolumes = recipe.getIngredients();
		IngredientVolume currentIngredient = ingrVolumeRepo.findById(ingredientVolumeId);
		for(IngredientVolume ingredientFromList : currentRecipeIngredientsVolumes) {
			if(ingredientFromList.equals(currentIngredient)) {
				ingrVolumeRepo.delete(ingredientFromList);
				break;
			}
		}
		return "{\"message\": \"success\"}";
	}
	
	@PostMapping("editIngredientVolume")
	@ResponseBody 
	public String editIngredientVolume(@RequestParam long recipeId, @RequestParam long ingredientVolumeId, @RequestParam double newValue) {
		Recipe recipe = recipeRepo.findById(recipeId);		
		List<IngredientVolume> currenRecipeIngredients = recipe.getIngredients();
		IngredientVolume currentIngredient = ingrVolumeRepo.findById(ingredientVolumeId);
		for(IngredientVolume ingredietnFromList : currenRecipeIngredients) {
			if(ingredietnFromList.equals(currentIngredient)) {
				ingredietnFromList.setVolume(newValue);
				ingrVolumeRepo.save(ingredietnFromList);
				break;
			}
		}		
		return "{}";
	}

	/* PHOTOS */
	//---------------------------------------------------------------------
	@PostMapping("sendPhoto")
	@ResponseBody
	public String addRecipePhoto(@RequestParam(required = false, name="photo") MultipartFile photo) throws IOException {
		Integer hashCode = photo.hashCode();
		System.out.println("Photo uploaded. Its code is: " + hashCode.toString());
		byte[] byteArray = photo.getBytes();
		this.newRecipePhotos.put(hashCode, byteArray);
		String response = "{\"code\":\"" + hashCode.toString() + "\"}";
		return response;
	}
	
	@PostMapping("saveUnsavedePhoto")
	@ResponseBody
	public String saveUnsavedePhoto(@RequestParam long recipeId, @RequestParam String code) throws IOException {
		String response = null;
		Recipe recipe = recipeRepo.findById(recipeId);
		
		byte[] byteArray = this.newRecipePhotos.get(Integer.parseInt(code));
		String recipePhotoFolder = recipe.getPhotoFolder(); //name of the folder with photos
		String recipePhotoFolderFullPath = uploadPath + "/" + recipePhotoFolder;
		RecipePhoto uploadedPhoto = null;
		if (byteArray.length != 0) {	
			String uniqPhotoName = UUID.randomUUID().toString() + ".jpg";
			String resultFullPhotoName = recipePhotoFolderFullPath + "/" + uniqPhotoName; //full path to the currently saving photo (including its uniq name) 
			FileOutputStream fos = new FileOutputStream(resultFullPhotoName);
			fos.write(byteArray);
			fos.close();
			
			uploadedPhoto = new RecipePhoto(uniqPhotoName, recipe);
			recipe.getPhotos().add(uploadedPhoto);
			recipePhotoRepo.save(uploadedPhoto);			
			response = "{\"id\":\"" + uploadedPhoto.getId() + "\"}";;
		}
		else {
			response = "{\"error\":\"фото не загружено\"}";
		}		
		return response;
	}
	
	@PostMapping("dropUnsavedPhoto")
	@ResponseBody
	public String dropUnsavedPhoto(@RequestParam String code) {
		Integer reqCode = null;
		String response = null;
		try {
			reqCode = Integer.parseInt(code);
			this.newRecipePhotos.remove(reqCode);
			response = "{\"done\":\" + true + \"}";
		} 
		catch(NumberFormatException nfExp) {
			response = "{\"error\":\" + неправильные формат кода фотографии. + \"}";
		}		
		return response;
	}
	
	@PostMapping("savePhoto")
	@ResponseBody
	public String savePhoto(@RequestParam long photoCode) {
		
		return "";
	}
	
	@PostMapping("deletePhoto")
	@ResponseBody
	public String deletePhoto(@RequestParam long recipeId, @RequestParam long photoId) {
		String response = null;
		Recipe recipe = this.recipeRepo.findById(recipeId);
		boolean fileDeleted = false;
		for (RecipePhoto rPhoto : recipe.getPhotos()) {
			if (rPhoto.getId() == photoId) {
				String photoPath = uploadPath + "/" + recipe.getPhotoFolder() + "/" + rPhoto.getPhotoPath();								
				recipePhotoRepo.delete(rPhoto);
				recipe.getPhotos().remove(rPhoto);
				File photoFile = new File(photoPath);
				fileDeleted = photoFile.delete();				
				break;
			}
		}
		if(fileDeleted) {
			response = "{\"done\":\" + true + \"}";
		}
		else {
			response = "{\"error\":\" Фото не найдено на диске + \"}";
		}
		return response;
	}
	//---------------------------------------------------------------------
	
	@PostMapping("save")
	public String saverecipe(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, 
								@RequestParam Map<String, String> allParametrs,
								Model model) throws IOException {		
		User currentUser = currentUserDetails.getUser();	
		//Main recipe info
		String name = allParametrs.get("name");
		allParametrs.remove("name");
		String type = allParametrs.get("type");
		allParametrs.remove("type");
		String tagline = allParametrs.get("tagline");
		allParametrs.remove("tagline");
		String youtubeLink = allParametrs.get("youtubeLink");
		allParametrs.remove("youtubeLink");
		String text = allParametrs.get("text");
		allParametrs.remove("text");
		
		List<IngredientVolume> ingredients = new ArrayList<>();
		Recipe recipe = new Recipe(currentUser, name, type, tagline, youtubeLink, text, ingredients);
		
		//Ingredients of recipe and their volume		
		for (String ingredientName : allParametrs.keySet()) {
			Double volume = null;
			try {
				volume = Double.parseDouble(allParametrs.get(ingredientName));
			} catch(NumberFormatException nfExp) {
				System.out.println("Ошибка перевода строки в число");
				System.out.println("");
				volume = 0.0;
			} 
			Ingredient currentIngredient = this.ingrRepo.findByName(ingredientName);
			
			IngredientVolume ingrVolume = new IngredientVolume(currentIngredient, volume, recipe);
			ingredients.add(ingrVolume);
		}
		
		//Photos saving 
		List<RecipePhoto> photos = new ArrayList<>();
		List<String> photoPaths = new ArrayList<>();
		//Create temporary folder
		String uploadTempDirPath = new File(".").getAbsolutePath() + "/temp";
		File uploadDir = new File(uploadTempDirPath);
		if (!uploadDir.exists()) {
			uploadDir.mkdir();
		}
		for (Integer photoKey : this.newRecipePhotos.keySet()) {
			byte[] byteArray = this.newRecipePhotos.get(photoKey);
			if (byteArray.length != 0) {	
				String uniqPhotoName = UUID.randomUUID().toString() + ".jpg";
				String resultFullPhotoName = uploadTempDirPath + "/" + uniqPhotoName;
				photoPaths.add(resultFullPhotoName);
				FileOutputStream fos = new FileOutputStream(resultFullPhotoName);
				fos.write(byteArray);
				fos.close();				
				RecipePhoto uploadedPhoto = new RecipePhoto(uniqPhotoName, recipe);
				photos.add(uploadedPhoto);
			}
		}
		//Creating bucket for photos;				
		String recipePhotoBucket = UUID.randomUUID().toString();  //name of the folder with photos
		awsConnector.createBucket(recipePhotoBucket);
		for(int i = 0; i < photoPaths.size(); i++) {
			String path = photoPaths.get(i);
			File photoFile = new File(path);
			awsConnector.uploadFile(recipePhotoBucket, photos.get(i).getPhotoPath(), photoFile);
		}
		recipe.setPhotoFolder(recipePhotoBucket);
		recipe.setPhotos(photos);
		recipeRepo.save(recipe);
		recipePhotoRepo.saveAll(photos);
		ingrVolumeRepo.saveAll(ingredients);
				
		Iterable<Recipe> recipes = recipeRepo.findAll();
		model.addAttribute("recipes", recipes);
		return "cookbook";
	}
		
}