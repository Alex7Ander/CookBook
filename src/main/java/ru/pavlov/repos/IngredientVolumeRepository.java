package ru.pavlov.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pavlov.domain.IngredientVolume;

public interface IngredientVolumeRepository extends JpaRepository<IngredientVolume, Long> {

}
