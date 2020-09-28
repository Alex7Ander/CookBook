package ru.pavlov.controllers;

import java.io.File;
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

import ru.pavlov.domain.Ingredient;
import ru.pavlov.domain.IngredientVolume;
import ru.pavlov.domain.Recipe;
import ru.pavlov.domain.RecipePhoto;
import ru.pavlov.domain.User;
import ru.pavlov.repos.IngredientRepository;
import ru.pavlov.repos.IngredientVolumeRepository;
import ru.pavlov.repos.RecipePhotoRepository;
import ru.pavlov.repos.RecipeRepository;
import ru.pavlov.security.CookBookUserDetails;
import ru.pavlov.wrappers.IngredientVolumeWrapper;
import ru.pavlov.yandex.disk.YandexDiskConnector;
import ru.pavlov.yandex.disk.YandexDiskException;

@Controller
@RequestMapping("/recipe/**")  
public class RecipeController {
	
	@Value("${upload.path}")
	private String uploadPath;
	
	@Autowired
	private YandexDiskConnector yandexDiskConnector;
	
	@Autowired
	private RecipeRepository recipeRepo;
	
	@Autowired
	private IngredientRepository ingrRepo;
	
	@Autowired
	private IngredientVolumeRepository ingrVolumeRepo;
	
	@Autowired
	private RecipePhotoRepository recipePhotoRepo;	
	
	private Map<Integer, byte[]> newRecipePhotos = new HashMap<>();
		
	@GetMapping("show")
	public String recipe(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, @RequestParam(required = true, name="recipeId") Long recipeId, Model model) {
		Recipe recipe = recipeRepo.findById(recipeId);
		model.addAttribute("recipe", recipe);		
		List<String> ingrTypes = ingrRepo.getIngrTypes();
		model.addAttribute("ingrTypes", ingrTypes);
		
		User currentUser = currentUserDetails.getUser();
		if (recipe.getRecipeAuther().equals(currentUser)) {
			model.addAttribute("editable", true);
		}
		else {
			model.addAttribute("editable", false);
		}			
				
		List<IngredientVolume> currentRecipeIngredientVolume = recipe.getIngredients();	
		List<IngredientVolumeWrapper> ingredientsVolumesWrappers = new ArrayList<>();
		double totalResultCalorie = 0;
		for(IngredientVolume ingredientVolume : currentRecipeIngredientVolume) {
			IngredientVolumeWrapper ingredientVolumeWrapper = new IngredientVolumeWrapper(ingredientVolume);
			ingredientsVolumesWrappers.add(ingredientVolumeWrapper);
			totalResultCalorie += ingredientVolumeWrapper.getResultCalorie();
		}
		model.addAttribute("ingredientsVolumesWrappers", ingredientsVolumesWrappers);
		model.addAttribute("totalResultCalorie", totalResultCalorie);
		
		List<RecipePhoto> recipePhotos = recipe.getPhotos();
		model.addAttribute("recipePhotos", recipePhotos);			
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
		return response;
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
		//расчет полной калорийности
		double totalResultCalorie = 0;
		for(IngredientVolume ingredientVolume : recipe.getIngredients()) {
			totalResultCalorie += ingredientVolume.getIngredient().getCalorie()*ingredientVolume.getVolume()/100;
		}
		return "{\"message\":\"success\", \"ingredientVolumeId\":\""+ingredientVolumeId.toString()+"\", \"totalCalorie\":\""+totalResultCalorie+"\"}";
	}
	
	@PostMapping("deleteIngredient")
	@ResponseBody
	public String deleteIngredient(@RequestParam long ingredientVolumeId) {
		System.out.println("Удаление ингредиента из рецепта (ingredientVolume)");		
		IngredientVolume currentIngredient = ingrVolumeRepo.findById(ingredientVolumeId);
		System.out.println("ingredientVolume по id="+ingredientVolumeId+" найден");
		System.out.println("Удаляем");
		ingrVolumeRepo.delete(currentIngredient);
		System.out.println("Удалено. Проверим.");
		IngredientVolume currentIngredient2 = ingrVolumeRepo.findById(ingredientVolumeId);
		if(currentIngredient2 != null) {
			System.out.println("Не удалено");
			return "{\"error\": \"Не удалено\"}";
		}
		else {
			System.out.println("Удалено");
			//расчет полной калорийности
			Recipe recipe = currentIngredient.getRecipe();
			double totalResultCalorie = 0;
			for(IngredientVolume ingredientVolume : recipe.getIngredients()) {
				totalResultCalorie += ingredientVolume.getIngredient().getCalorie()*ingredientVolume.getVolume()/100;
			}
			return "{\"message\": \"success\", \"totalCalorie\": \""+totalResultCalorie+"\"}";
		}		
	}
	
	@PostMapping("editIngredientVolume")
	@ResponseBody 
	public String editIngredientVolume(@RequestParam long ingredientVolumeId, @RequestParam double newValue) {
		IngredientVolume currentIngredient = ingrVolumeRepo.findById(ingredientVolumeId);
		currentIngredient.setVolume(newValue);
		ingrVolumeRepo.save(currentIngredient);			
		//Проверка сохранения
		IngredientVolume currentIngredient2 = ingrVolumeRepo.findById(ingredientVolumeId);
		if(currentIngredient2.getVolume() != newValue) {
			return "{\"error\": \"Значение не изменено\"}";
			
		}
		else {
			//расчет калорийности нового количесва ингредиента
			double calorie = currentIngredient.getIngredient().getCalorie() * newValue / 100;
			//расчет полной калорийности
			Recipe recipe = currentIngredient.getRecipe();
			double totalResultCalorie = 0;
			for(IngredientVolume ingredientVolume : recipe.getIngredients()) {
				totalResultCalorie += ingredientVolume.getIngredient().getCalorie()*ingredientVolume.getVolume()/100;
			}
			return "{\"message\": \"success\",\"calorie\": \""+calorie+"\",\"totalCalorie\": \""+totalResultCalorie+"\"}";
		}
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
		RecipePhoto uploadedPhoto = null;
		if (byteArray.length != 0) {	
			String uniqPhotoName = UUID.randomUUID().toString() + ".jpg";
			String internalPathToTargetFolder = "/ApplicationsFolder/CookBook/" + recipe.getPhotoFolder();
			try {
				yandexDiskConnector.uploadFile(internalPathToTargetFolder, uniqPhotoName, byteArray);
				uploadedPhoto = new RecipePhoto(internalPathToTargetFolder + "/" + uniqPhotoName, recipe);
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
					yandexDiskConnector.delete(rp.getPhotoPath());
					response = "{\"done\":\" + true + \"}";
				} catch (IOException | YandexDiskException e) {
					response = "{\"error\":\" Ошибка при удалении ото из Yandex Disk + \"}";
					e.printStackTrace();
				}
				break;
			}
			response = "{\"error\":\"Фото не найдено на диске\"}";
		}
		return response;
	}
	
	@PostMapping("setPreview")
	@ResponseBody
	public String setRecipePhotoAsPreview(@RequestParam long recipeId, @RequestParam long photoId) {
		Recipe recipe = this.recipeRepo.findById(recipeId);
		RecipePhoto previewPhoto = this.recipePhotoRepo.findById(photoId);
		if(!previewPhoto.getRecipe().equals(recipe)) {
			return "{\"error\":\" Фото с id = "+photoId+" не соответствет рецепту с id = "+recipeId+"\"}";
		}
		for(RecipePhoto recipePhoto : recipe.getPhotos()) {
			if(recipePhoto.isPreview()) {
				recipePhoto.setPreview(false);
				this.recipePhotoRepo.save(recipePhoto);
				break;
			}
		}
		previewPhoto.setPreview(true);
		this.recipePhotoRepo.save(previewPhoto);
		return "{\"done\":\" + true + \"}";
	}
	//---------------------------------------------------------------------
	
	@PostMapping("save")
	@ResponseBody
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
		
		String previewPhotoCode = allParametrs.get("previewRb");
		allParametrs.remove("previewRB");
		
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
		recipePhotosYandexDiskFolder.replace(" ", "_");
		yandexDiskConnector.createFolder("/ApplicationsFolder/CookBook/" + recipePhotosYandexDiskFolder);
		recipe.setPhotoFolder(recipePhotosYandexDiskFolder);
		for (Integer photoKey : this.newRecipePhotos.keySet()) {
			byte[] byteArray = this.newRecipePhotos.get(photoKey);
			if (byteArray.length != 0) {	
				String uniqPhotoName = UUID.randomUUID().toString() + ".jpg";
				String resultFullPhotoName = uploadTempDirPath + "/" + uniqPhotoName;
				photoPaths.add(resultFullPhotoName);
				FileOutputStream fos = new FileOutputStream(resultFullPhotoName);
				fos.write(byteArray);
				fos.close();				
				RecipePhoto uploadedPhoto = new RecipePhoto("/ApplicationsFolder/CookBook/" + recipePhotosYandexDiskFolder + "/" + uniqPhotoName, recipe);
				photos.add(uploadedPhoto);				
				yandexDiskConnector.uploadFile("/ApplicationsFolder/CookBook/" + recipePhotosYandexDiskFolder, uniqPhotoName, resultFullPhotoName);
				
				if(photoKey.toString().equals(previewPhotoCode)) {
					uploadedPhoto.setPreview(true);
				}
			}
		}
		
		for (File f : uploadDir.listFiles()) {				
			f.delete();
		}
		uploadDir.delete();
		//Clear container for photos - newRecipePhotos
		this.newRecipePhotos.clear();	
		recipe.setPhotos(photos);
		recipeRepo.save(recipe);
		recipePhotoRepo.saveAll(photos);
		ingrVolumeRepo.saveAll(ingredients);				
		Iterable<Recipe> recipes = recipeRepo.findAll();
		model.addAttribute("recipes", recipes);
		return "{\"done\":\" + true + \"}";
	}
	
	@PostMapping("delete")
	@ResponseBody
	public String deleterecipe(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, @RequestParam(required = true, name="recipeId") Long recipeId) throws IOException, YandexDiskException {
		String response = "";
		Recipe recipe = this.recipeRepo.findById(recipeId);
		User currentUser = currentUserDetails.getUser();
		if (recipe.getRecipeAuther().equals(currentUser)) {
			String yandexDiskPhotosFilePath = "/ApplicationsFolder/CookBook/" + recipe.getPhotoFolder();
			for (RecipePhoto photo : recipe.getPhotos()) {
				this.recipePhotoRepo.delete(photo);
			}
			this.recipeRepo.delete(recipe);			
			this.yandexDiskConnector.delete(yandexDiskPhotosFilePath);
			response = "{\"done\":\" + true + \"}";
		}
		else {
			response = "{\"error\":\" Нет прав на удаление рецепта + \"}";
		}
		return response;	
	}
	
	@GetMapping("loadPhoto")
	@ResponseBody
	public byte[] loadPhoto(@RequestParam long photoId) {
		RecipePhoto sendingPhoto = this.recipePhotoRepo.findById(photoId);
		String internalPathToTargetFile = sendingPhoto.getPhotoPath();
		byte[] bytes = null;
		try {
			bytes = yandexDiskConnector.getTargetFileByteArrayByPath(internalPathToTargetFile);
		} catch (IOException | YandexDiskException e) {
			e.printStackTrace();
		}
		return bytes;
	}
		
}