package ru.pavlov.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

@Controller
@RequestMapping("/ingredient/**")
public class IngredientController {

	@Autowired
	private IngredientRepository ingrRepo;
	
	@PostMapping("save")
	@ResponseBody
	public String saveIngredient(@AuthenticationPrincipal CookBookUserDetails currentUserDetails, 
								 @RequestParam String name, 
								 @RequestParam String type, 
								 @RequestParam String descr,
								 @RequestParam String prot, 
								 @RequestParam String fat, 
								 @RequestParam String carbo) {		
		try {
			String response = null;
			Ingredient ingr = this.ingrRepo.findByName(name);
			if (ingr!= null) {
				response = "{\"error\": \"Ингредиент с таким именем уже существует в списке ингредиентов. Его тип: " + ingr.getType() + "\"}";
				return response;
			}
			double protein = Double.parseDouble(prot);
			double fatInt = Double.parseDouble(fat);
			double carbohydrate = Double.parseDouble(carbo);
			Ingredient newIngredient = new Ingredient(name, type, descr, protein, fatInt, carbohydrate);
			newIngredient.setUser(currentUserDetails.getUser());
			newIngredient.setCommon(false);
			this.ingrRepo.save(newIngredient);
			response = "{\"id\": \"" + newIngredient.getId().toString() + "\"}";
			return response;
		} catch (Exception exp) {
			return "{\"error\": \"" + exp.getMessage() + "\"}";
		}		
	}
	
	@GetMapping("getProperties")
	@ResponseBody
	public String addIngrToList(@RequestParam String type, @RequestParam String name, Model model){
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
	
	@GetMapping("loadImg")
	@ResponseBody
	public byte[] loadImg(@RequestParam long ingrId) {
		Ingredient ingredient = this.ingrRepo.findById(ingrId);
		byte[] imgByteArray = ingredient.getImage();				
		return imgByteArray;
	}
	
	@PostMapping("saveImage")
	public String saveImage(@RequestParam long imageId, @RequestParam(required = true, name="ingrImage") MultipartFile image) {
		String response = null;
		byte[] byteArray = null;
		try {
			byteArray = image.getBytes();
		}
		catch(IOException ioExp) {
			response = "{\"error\":\"нет файла с изображением, возможно он был поврежден при передаче.\"}";
			return response;
		}
		Ingredient ingredient = this.ingrRepo.findById(imageId);
		if(ingredient == null) {
			response = "{\"error\":\"ингредиент не найден\"}";
			return response;
		}
		ingredient.setImage(byteArray);
		this.ingrRepo.save(ingredient);
		response = "{\"done\":\" + true + \"}";
		return response;
	}
	
}