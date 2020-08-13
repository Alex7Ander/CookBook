package ru.pavlov.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.pavlov.domain.Ingredient;
import ru.pavlov.domain.User;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

	public Ingredient findById(long id);
	public Ingredient findByName(String name);
	public Ingredient findByNameAndType(String name, String type);
	public List<Ingredient> findByType(String type);
	
	@Query(value="SELECT DISTINCT type FROM ingredients", nativeQuery=true)
	public List<String> getIngrTypes();			
}