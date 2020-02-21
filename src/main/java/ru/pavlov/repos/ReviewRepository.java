package ru.pavlov.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.pavlov.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	public List<Review> findByUserId(Long userId);
}
