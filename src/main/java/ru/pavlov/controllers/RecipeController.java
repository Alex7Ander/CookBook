package ru.pavlov.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
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
import ru.pavlov.yandex.disk.YandexDiskConnector;
import ru.pavlov.yandex.disk.YandexDiskException;

@Controller
@RequestMapping("/recipe/**")  
public class RecipeController {
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@Autowired
	private AWSConnector awsConnector;
	
	@Autowired
	private YandexDiskConnector yandexDiskConnector;
	
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
		
		//Создаем папку для фото
		String tempPhotoFolderPath = "/home/alex/eclipse-workspace/CookBook/target/classes/static/img/" + recipe.getPhotoFolder(); //"C:\\Users\\user\\eclipse-workspace\\CookBook\\target\\classes\\static\\img\\" + recipe.getPhotoFolder();		
		File tempPhotoFolder = new File(tempPhotoFolderPath);
		tempPhotoFolder.mkdir();
				
		for (RecipePhoto rp : recipePhotos) {			
			try {	
				String internalPathToTargetFile = "/ApplicationsFolder/CookBook/" + recipe.getPhotoFolder() + "/" + rp.getPhotoPath();
				String downloadedPhotoFileFullPath = tempPhotoFolderPath + "/" + rp.getPhotoPath();
				try {
					yandexDiskConnector.downloadFile(internalPathToTargetFile, downloadedPhotoFileFullPath);
					rp.setDownloadedPhotoPath("/img/" + recipe.getPhotoFolder() + "/" + rp.getPhotoPath());
				} catch (YandexDiskException e) {
					e.printStackTrace();
				}				
			} catch (IOException e) {
				System.err.println("Error while downloading file " + rp.getPhotoPath() + " from bucket " + recipe.getPhotoFolder());
				e.printStackTrace();
			}			
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
		String recipePhotoFolderFullPath = "/home/alex/eclipse-workspace/CookBook/target/classes/static/img/" + recipe.getPhotoFolder();
		RecipePhoto uploadedPhoto = null;
		if (byteArray.length != 0) {	
			String uniqPhotoName = UUID.randomUUID().toString() + ".jpg";
			String resultFullPhotoName = recipePhotoFolderFullPath + "/" + uniqPhotoName; //full path to the currently saving photo (including its uniq name) 
			FileOutputStream fos = new FileOutputStream(resultFullPhotoName);
			fos.write(byteArray);
			fos.close();
			String internalPathToTargetFolder = "/ApplicationsFolder/CookBook/" + recipe.getPhotoFolder();
			try {
				yandexDiskConnector.uploadFile(internalPathToTargetFolder, uniqPhotoName, resultFullPhotoName);
				uploadedPhoto = new RecipePhoto(uniqPhotoName, recipe);
				recipe.getPhotos().add(uploadedPhoto);
				recipePhotoRepo.save(uploadedPhoto);			
				response = "{\"id\":\"" + uploadedPhoto.getId() + "\"}";
			} catch (IOException | YandexDiskException e) {
				response = "{\"error\":\"ошибка при загрузке файла на Yandex Disk\"}";
				e.printStackTrace();
			}
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
	
	@PostMapping("deletePhoto")
	@ResponseBody
	public String deletePhoto(@RequestParam long recipeId, @RequestParam long photoId) {
		String response = null;
		Recipe recipe = this.recipeRepo.findById(recipeId);
		for (RecipePhoto rp : recipe.getPhotos()) {
			if (rp.getId() == photoId) {												
				recipePhotoRepo.delete(rp);
				recipe.getPhotos().remove(rp);
				try {
					yandexDiskConnector.delete("/ApplicationsFolder/CookBook/" + recipe.getPhotoFolder() + "/" + rp.getPhotoPath());
					String photoPath = "/home/alex/eclipse-workspace/CookBook/target/classes/static/img/" + recipe.getPhotoFolder() + "/" + rp.getPhotoPath();
					File photoFile = new File(photoPath);
					photoFile.delete();
					response = "{\"done\":\" + true + \"}";
				} catch (IOException | YandexDiskException e) {
					response = "{\"error\":\" Ошибка при удалении ото из Yandex Disk + \"}";
					e.printStackTrace();
				}
				break;
			}
			response = "{\"error\":\" Фото не найдено на диске + \"}";
		}
		return response;
	}
	//---------------------------------------------------------------------
	
	@PostMapping("save")
	public String saverecipe(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, 
								@RequestParam Map<String, String> allParametrs,
								Model model) throws IOException, YandexDiskException {		
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
		
		//Creating yandex folder for photos;				
		String recipePhotosYandexDiskFolder = UUID.randomUUID().toString();  //name of the folder with photos
		yandexDiskConnector.createFolder("/ApplicationsFolder/CookBook/" + recipePhotosYandexDiskFolder);
		recipe.setPhotoFolder(recipePhotosYandexDiskFolder);
		for (Integer photoKey : this.newRecipePhotos.keySet()) {
			byte[] byteArray = this.newRecipePhotos.get(photoKey);
			if(recipe.getPreviewImage() == null) {
				recipe.setPreviewImage(byteArray);
			}
			if (byteArray.length != 0) {	
				String uniqPhotoName = UUID.randomUUID().toString() + ".jpg";
				String resultFullPhotoName = uploadTempDirPath + "/" + uniqPhotoName;
				photoPaths.add(resultFullPhotoName);
				FileOutputStream fos = new FileOutputStream(resultFullPhotoName);
				fos.write(byteArray);
				fos.close();				
				RecipePhoto uploadedPhoto = new RecipePhoto(uniqPhotoName, recipe);
				photos.add(uploadedPhoto);
				yandexDiskConnector.uploadFile("/ApplicationsFolder/CookBook/" + recipePhotosYandexDiskFolder, uniqPhotoName, resultFullPhotoName);
			}
		}
		//Clear container for photos - newRecipePhotos
		this.newRecipePhotos.clear();
	
		recipe.setPhotos(photos);
		recipeRepo.save(recipe);
		recipePhotoRepo.saveAll(photos);
		ingrVolumeRepo.saveAll(ingredients);
				
		Iterable<Recipe> recipes = recipeRepo.findAll();
		model.addAttribute("recipes", recipes);
		return "cookbook";
	}
	
	@PostMapping("delete")
	public String deleterecipe(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, @RequestParam(required = true, name="recipeId") Long recipeId,
								Model model) throws IOException, YandexDiskException {
		Recipe recipe = this.recipeRepo.findById(recipeId);
		User currentUser = currentUserDetails.getUser();
		if (recipe.getRecipeAuther().equals(currentUser)) {
			String yandexDiskPhotosFilePath = "/ApplicationsFolder/CookBook/" + recipe.getPhotoFolder();
			this.recipeRepo.delete(recipe);			
			this.yandexDiskConnector.delete(yandexDiskPhotosFilePath);
		}
		Iterable<Recipe> recipes = recipeRepo.findAll();
		model.addAttribute("recipes", recipes);
		return "cookbook";		
	}
		
}