package ru.pavlov.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.pavlov.domain.Recipe;
import ru.pavlov.domain.User;

public interface RecipeRepository extends JpaRepository<Recipe, Integer>  {
	public List<Recipe> findByType(String type);
	public List<Recipe> findByName(String type);
	public List<Recipe> findByRecipeAuther(User recipeAuther);
	
	public Recipe findById(Long id);
	
}
