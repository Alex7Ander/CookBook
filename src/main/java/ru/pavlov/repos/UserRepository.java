package ru.pavlov.repos;

import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.pavlov.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	public User findByName(String username);
	public User findByUserLoginName(String login);
	public User findByEmail(String email);
	
	@Transactional
	@Modifying
	@Query("update User u set u.name = ?2, u.surname = ?3, u.city = ?4, u.temperament = ?5, u.phone = ?6 where u.id = ?1")
	void setUserInfoById(Long id, String name, String surname, String city, String temperament, String phone);
	
	@Transactional
	@Modifying	
	@Query("update User u set u.avatarPath = ?2 where u.id = ?1")
	void setUserAvatarById(Long id, String avatarPath);
}