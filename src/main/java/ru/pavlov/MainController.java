package ru.pavlov;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.pavlov.repos.RecipeRepo;

@Controller
public class MainController {
	@SuppressWarnings("unused")
	@Autowired
	private RecipeRepo recipeRepo;
	
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
