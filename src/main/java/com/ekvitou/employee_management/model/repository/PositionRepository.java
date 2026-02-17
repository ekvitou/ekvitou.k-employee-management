package com.ekvitou.employee_management.model.repository;

import com.ekvitou.employee_management.model.entity.Position;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PositionRepository extends JpaRepository<Position, Long> {
    Optional<Position> findPositionByUuid(String uuid);
    Page<Position> findPositionsByIsDeletedIsFalse(Pageable pageable);
    Boolean existsByPositionNameAndIsDeletedIsFalse(String positionName);
}
