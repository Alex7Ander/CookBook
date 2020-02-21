package ru.pavlov.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pavlov.domain.Ingredient;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

}
