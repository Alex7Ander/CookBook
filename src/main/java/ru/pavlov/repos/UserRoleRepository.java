package ru.pavlov.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.pavlov.domain.UserRole;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    public List<UserRole> findByRole(String role);
}
