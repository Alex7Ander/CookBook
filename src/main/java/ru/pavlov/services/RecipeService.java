package ru.pavlov.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import ru.pavlov.domain.Recipe;
import ru.pavlov.repos.RecipeRepository;

@Service
public class RecipeService {

	@Autowired RecipeRepository recipeRepo;
	
	public List<Recipe> findRecipiesLike(Recipe recipe){
		return this.recipeRepo.findAll(Example.of(recipe));
	}
	
	
}
