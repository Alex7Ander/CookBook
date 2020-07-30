package ru.pavlov.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ru.pavlov.domain.Ingredient;
import ru.pavlov.repos.IngredientRepository;

@Controller
@RequestMapping("/ingredient/**") // /ingredient/getIngredients
public class IngredientController {

	@Autowired
	private IngredientRepository ingrRepo;
	
	@PostMapping("save")
	@ResponseBody
	public String saveIngredient(@RequestParam String name, 
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
	public String getIngredients(@RequestParam String ingrType, Model model) {
		List<Ingredient> ingredients = ingrRepo.findByType(ingrType);
		model.addAttribute("ingredients", ingredients);
		ObjectMapper jsonCreator = new ObjectMapper();
		
		StringBuilder answer = new StringBuilder();
		answer.append("[");
		for(int i = 0; i< ingredients.size(); i++) {
			try {
				Ingredient ingredient = ingredients.get(i);
				String jsonResponse = jsonCreator.writeValueAsString(ingredient);
				answer.append(jsonResponse);
				if (i < ingredients.size()-1) answer.append(", ");
 			}
			catch(JsonProcessingException jpExp) {
				
			}
		}
		answer.append("]");
		return answer.toString();
	}
	
}