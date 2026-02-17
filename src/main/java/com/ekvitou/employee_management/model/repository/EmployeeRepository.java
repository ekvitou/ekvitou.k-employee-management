package com.ekvitou.employee_management.model.repository;

import com.ekvitou.employee_management.model.entity.Employee;
import com.ekvitou.employee_management.model.entity.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @EntityGraph(attributePaths = {"department", "position"})
    Optional<Employee> findEmployeeByUuid(String uuid);

    @EntityGraph(attributePaths = {"department", "position"})
    Page<Employee> findEmployeesByIsDeletedIsFalse(Pageable pageable);

    @EntityGraph(attributePaths = {"department", "position"})
    Page<Employee> findEmployeesByStatusAndIsDeletedIsFalse(EmployeeStatus status, Pageable pageable);

    Boolean existsByEmail(String email);

    @EntityGraph(attributePaths = {"department", "position"})
    @Query("""
        SELECT e FROM Employee e
        WHERE e.isDeleted = false
        AND (
            LOWER(e.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
            OR LOWER(e.email) LIKE LOWER(CONCAT('%', :keyword, '%'))
        )
    """)
    Page<Employee> searchEmployees(@Param("keyword") String keyword, Pageable pageable);

    @EntityGraph(attributePaths = {"department", "position"})
    Page<Employee> findEmployeesByDepartment_UuidAndIsDeletedIsFalse(String departmentUuid, Pageable pageable);

    @EntityGraph(attributePaths = {"department", "position"})
    @Query("""
        SELECT e FROM Employee e
        WHERE e.isDeleted = false
        AND (:status IS NULL OR e.status = :status)
        AND (:departmentUuid IS NULL OR e.department.uuid = :departmentUuid)
        AND (:positionUuid IS NULL OR e.position.uuid = :positionUuid)
    """)
    Set<Employee> findEmployeesForReport(
            @Param("status") EmployeeStatus status,
            @Param("departmentUuid") String departmentUuid,
            @Param("positionUuid") String positionUuid
    );

    @EntityGraph(attributePaths = {"department", "position"})
    @Query("""
        SELECT e FROM Employee e
        WHERE e.isDeleted = false
        AND (:status IS NULL OR e.status = :status)
        AND (:departmentUuid IS NULL OR e.department.uuid = :departmentUuid)
        AND (:positionUuid IS NULL OR e.position.uuid = :positionUuid)
    """)
    Page<Employee> findEmployeesForReport(
            @Param("status") EmployeeStatus status,
            @Param("departmentUuid") String departmentUuid,
            @Param("positionUuid") String positionUuid,
            Pageable pageable
    );
}
