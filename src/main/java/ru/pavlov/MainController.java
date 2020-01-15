package ru.pavlov;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.pavlov.domain.Recipe;
import ru.pavlov.repos.RecipeRepo;

@Controller
public class MainController {

	@Autowired
	private RecipeRepo recipeRepo;
	/*
	@GetMapping
	public String hello() {
		return "hello";
	}
	*/
	@GetMapping("cookbook")
	public String cookbook(Map<String, Object> model) {
		Iterable<Recipe> recipes = recipeRepo.findAll();
		model.put("recipes", recipes);
		return "cookbook";
	}
	
    @GetMapping(path="/autorisation")
    public String autorisation() {
        return "autorisation";
    }

    @GetMapping(path="/registration")
    public String registration() {
        return "registration";
    }
    
    @GetMapping(path="/resurection")
    public String resurection() {
        return "registration";
    }
}
