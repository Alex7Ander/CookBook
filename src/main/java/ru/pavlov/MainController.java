package ru.pavlov;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import ru.pavlov.repos.RecipeRepo;

@Controller
public class MainController {
	@Autowired
	private RecipeRepo recipeRepo;
	
    @GetMapping
    public String main(Map<String, Object> model) {
        return "main";
    }
	
    @GetMapping("/autorisation")
    public String greeting(@RequestParam(name="name", required=false, defaultValue="World") String name, Map<String, Object> model) {
        model.put("name", name);
        return "autorisation";
    }
    
    @PostMapping("registration")
    public String registration() {
        return "registration";
    }

}
