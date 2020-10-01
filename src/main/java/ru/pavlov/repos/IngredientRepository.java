package ru.pavlov.repos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.pavlov.domain.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

	public Ingredient findById(long id);
	public Ingredient findByName(String name);
	public Page<Ingredient> findByCommon(Pageable pageOption, boolean isCommon);
	
	@Query(value="SELECT * FROM ingredients WHERE type=?1", nativeQuery=true)
	public List<Ingredient> findByType(String type);
	
	@Query(value="SELECT * FROM ingredients WHERE name=?1 AND type=?2", nativeQuery=true)
	public Ingredient findByNameAndType(String name, String type);
	
	@Query(value="SELECT DISTINCT type FROM ingredients", nativeQuery=true)
	public List<String> getIngrTypes();			
}