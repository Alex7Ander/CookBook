package ru.pavlov.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.pavlov.domain.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    //public List<String> findRoleByUserName(String username);
}
