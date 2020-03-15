package ru.pavlov.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.pavlov.domain.Recipe;

public interface RecipeRepository extends JpaRepository<Recipe, Integer>  {
	public List<Recipe> findByType(String type);
	public List<Recipe> findByName(String type);
	//public List<Recipe> findByUserId(Long userId);
	public Recipe findById(Long id);
}
