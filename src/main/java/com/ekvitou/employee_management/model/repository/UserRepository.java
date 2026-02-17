package com.ekvitou.employee_management.model.repository;

import com.ekvitou.employee_management.model.entity.Employee;
import com.ekvitou.employee_management.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUuid(String uuid);
    Optional<User> findUserByKeycloakId(String keycloakId);
    Boolean existsByUsername(String username);
    Boolean existsByEmployee(Employee employee);
    Boolean existsByEmail(String email);
    Page<User> findUsersByIsDeletedIsFalse(Pageable pageable);
    Page<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCaseAndIsDeletedIsFalse(String username, String email, Pageable pageable);
}
