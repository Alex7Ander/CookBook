package ru.pavlov.repos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import ru.pavlov.domain.Recipe;

public interface RecipeRepo extends CrudRepository<Recipe, Integer>  {
	List<Recipe> findByType(String type);
	List<Recipe> findByName(String type);
}
