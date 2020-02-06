package ru.pavlov.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.pavlov.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	public User findByName(String username);
	public User findByUserLoginName(String login);
}
