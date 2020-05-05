package ru.pavlov.controllers;

import java.lang.reflect.Field;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.pavlov.domain.Recipe;
import ru.pavlov.repos.RecipeRepository;

@Controller
@RequestMapping("/recipe/**")
public class RecipeController {
	@Autowired
	private RecipeRepository recipeRepo;
	
	@PostMapping("editMainInfo")
	@ResponseBody
	public String editMainInfo(@RequestParam Map<String, String> allParametrs) {
		Long id = Long.parseLong(allParametrs.get("id"));
		allParametrs.remove("id");
		Recipe recipe = recipeRepo.findById(id);		
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
			}
		}
		recipeRepo.save(recipe);
		return "{}";
	}
	
	@PostMapping("editIngredientList")
	@ResponseBody
	public String editIngredientList() {
		return "";
	}
	
	
	
}