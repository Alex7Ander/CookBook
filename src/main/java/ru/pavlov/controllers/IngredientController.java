package ru.pavlov.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.pavlov.domain.Ingredient;
import ru.pavlov.repos.IngredientRepository;
import ru.pavlov.security.CookBookUserDetails;
import ru.pavlov.yandex.disk.YandexDiskConnector;
import ru.pavlov.yandex.disk.YandexDiskException;

@Controller
@RequestMapping("/ingredient/**")
public class IngredientController {

	@Autowired
	private IngredientRepository ingrRepo;
	
	@Autowired
	private YandexDiskConnector yandexDiskConnector;
	
	@PostMapping("save")
	@ResponseBody
	public String saveIngredient(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, 
								 @RequestParam String name, 
								 @RequestParam String type, 
								 @RequestParam String descr,
								 @RequestParam Double prot, 
								 @RequestParam Double fat, 
								 @RequestParam Double carbo,
								 @RequestParam Boolean common) {		
		try {
			String response = null;
			Ingredient ingr = this.ingrRepo.findByNameAndType(name, type);
			if (ingr!= null) {
				response = "{\"error\": \"Ингредиент такого типа с таким именем уже существует в списке ингредиентов.\"}";
				return response;
			}
			
			//Creating ingredient object and setting main info
			Ingredient newIngredient = new Ingredient(name, type, descr, prot, fat, carbo);
			newIngredient.setCommon(common);
			newIngredient.setUser(currentUserDetails.getUser());
					
			this.ingrRepo.save(newIngredient);
			response = "{\"id\": \"" + newIngredient.getId().toString() + "\"}";
			return response;
		} 
		catch (Exception exp) {
			return "{\"error\": \"" + exp.getMessage() + "\"}";
		}		
	}
	
	@PostMapping("edit")
	@ResponseBody
	public String edit(@RequestParam long id, @RequestParam(required = false) String name, 
						@RequestParam(required = false) String type, 
						@RequestParam(required = false) String description, 
						@RequestParam(required = false) Double protein, 
						@RequestParam(required = false) Double fat,
						@RequestParam(required = false) Double carbohydrate) {
		String response = null;
		Ingredient ingredient = this.ingrRepo.findById(id);
		if(type!= null) {
			ingredient.setType(type);
		}
		if(name != null) {
			ingredient.setName(name);
		}
		if(description != null) {
			ingredient.setDescription(description);
		}
		if(protein != null) {
			ingredient.setProtein(protein);
		}
		if(fat != null) {
			ingredient.setFat(fat);
		}
		if(carbohydrate != null) {
			ingredient.setCarbohydrate(carbohydrate);
		}
		try {
			this.ingrRepo.save(ingredient);
			response = "{\"done\":\" + true + \"}";
		}
		catch(Exception exp) {
			response = "{\"error\":\"Не удалось сохранить изменения\"}";
		}		
		return response;
	}
	
	@PostMapping("delete")
	@ResponseBody
	public String delete(@RequestParam long id) {
		String response = null;
		Ingredient ingredient = this.ingrRepo.findById(id);
		if(ingredient == null) {
			response = "{\"error\": \"Ингредиента с таким id нет в БД\"}";
			return response;
		}
		try {
			this.ingrRepo.delete(ingredient);
			response = "{\"done\":\" + true + \"}";
		}
		catch(Exception exp) {
			response = "{\"error\": \"" + exp.getMessage() + "\"}";
		}
		return response;
	}
	
	@GetMapping("getProperties")
	@ResponseBody
	public String getProperties(@RequestParam String type, @RequestParam String name, Model model){
		Ingredient ingr = ingrRepo.findByNameAndType(name, type);
		ObjectMapper jsonCreator = new ObjectMapper();
		try {
			String jsonResponse = jsonCreator.writeValueAsString(ingr);
			return jsonResponse;
		}
		catch(JsonProcessingException jpExp) {
			jpExp.printStackTrace();
			return "{\"error\": \"" + jpExp.getMessage() + "\"}";
		}		
	}
	
	@GetMapping("getIngredients")
	@ResponseBody
	public String getIngredients(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, @RequestParam String ingrType, Model model) {
		List<Ingredient> allIngredientsofThisType = ingrRepo.findByType(ingrType);
		List<Ingredient> requestedIngredientsofThisType = new ArrayList<>();
		for (int i = 0; i < allIngredientsofThisType.size(); i++ ) {
			Ingredient ingredient = allIngredientsofThisType.get(i);
			if(ingredient.isCommon() || ingredient.getUser().equals(currentUserDetails.getUser())) {
				requestedIngredientsofThisType.add(ingredient);
			}
		}		
		model.addAttribute("ingredients", requestedIngredientsofThisType);
		ObjectMapper jsonCreator = new ObjectMapper();
		
		StringBuilder answer = new StringBuilder();
		answer.append("[");
		for(int i = 0; i< requestedIngredientsofThisType.size(); i++) {
			Ingredient ingredient = requestedIngredientsofThisType.get(i);
			try {				
				String jsonResponse = jsonCreator.writeValueAsString(ingredient);
				answer.append(jsonResponse);
				if (i < requestedIngredientsofThisType.size()-1) answer.append(", ");
				
 			}
			catch(JsonProcessingException jpExp) {
				System.err.println("Error [ingredient -> json] for ingredient with id= " + ingredient.getId());
				jpExp.printStackTrace();
			}
		}
		answer.append("]");
		return answer.toString();
	}
	
	@GetMapping("loadPhoto")
	@ResponseBody
	public byte[] loadPhoto(@RequestParam long ingrId) {
		Ingredient ingredient = this.ingrRepo.findById(ingrId);
		String photoName = ingredient.getPreviewPhotoName();
		byte[] bytes = null;
		try {
			bytes = yandexDiskConnector.getTargetFileByteArrayByPath("/ApplicationsFolder/CookBook/_IngredientsPhotos/"+photoName);
		} catch (IOException | YandexDiskException exp) {
			System.out.println("При загрузке превью фото для ингредиента " + ingrId + ") " + ingredient.getName() + " произошла ошибка: " + exp.getMessage());
		}
		return bytes;
	}
	
	@PostMapping("saveImage")
	@ResponseBody
	public String saveImage( @RequestParam MultipartFile ingrImage, @RequestParam long ingrId) {
		String response = null;
		byte[] byteArray = null;
		try {
			byteArray = ingrImage.getBytes();
		}
		catch(IOException ioExp) {
			response = "{\"error\":\"нет файла с изображением, возможно он был поврежден при передаче.\"}";
			return response;
		}
				
		Ingredient ingredient = this.ingrRepo.findById(ingrId);
		if(ingredient == null) {
			response = "{\"error\":\"Ингредиент не найден\"}";
			return response;
		}
		
		if (byteArray.length != 0) {	
			String uniqPhotoName = UUID.randomUUID().toString() + ".jpg";
			uniqPhotoName.replace(' ', '_');
			String uploadTempDirPath = new File(".").getAbsolutePath() + "/temp";
			String resultFullPhotoName = uploadTempDirPath + "/" + uniqPhotoName;
			File uploadDir = new File(uploadTempDirPath);
			if(!uploadDir.exists()){
				uploadDir.mkdir();
			}									
			try {
				FileOutputStream fos = new FileOutputStream(resultFullPhotoName);
				fos.write(byteArray);
				fos.close();
				yandexDiskConnector.uploadFile("/ApplicationsFolder/CookBook/_IngredientsPhotos", uniqPhotoName, resultFullPhotoName);
				
				String oldPreviewPhotoName = ingredient.getPreviewPhotoName();
				ingredient.setPreviewPhotoName(uniqPhotoName);
				this.ingrRepo.save(ingredient);
				
				if(oldPreviewPhotoName != null) {
					yandexDiskConnector.delete("/ApplicationsFolder/CookBook/_IngredientsPhotos/" + oldPreviewPhotoName);
				}
				
				for (File f : uploadDir.listFiles()) {				
					f.delete();
				}
				uploadDir.delete();
				
				response = "{\"done\":\"true\"}";
				return response;
			} catch (IOException | YandexDiskException exp) {
				exp.printStackTrace();
				response = "{\"error\":\""+exp.getMessage()+"\"}";
				return response;
			}				
		}
		else {
			response = "{\"error\":\"Массив байт загруженной фотографии пуст\"}";
			return response;
		}
	}	
}