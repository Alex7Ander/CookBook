package ru.pavlov.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.pavlov.domain.RecipePhoto;

public interface RecipePhotoRepository extends JpaRepository<RecipePhoto, Long> {

}