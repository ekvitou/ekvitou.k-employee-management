package com.ekvitou.employee_management.model.service;

import com.ekvitou.employee_management.exception.DuplicatePositionException;
import com.ekvitou.employee_management.exception.PositionAlreadyDeletedException;
import com.ekvitou.employee_management.exception.PositionDeleteException;
import com.ekvitou.employee_management.exception.PositionNotFoundException;
import com.ekvitou.employee_management.mapper.PositionMapstruct;
import com.ekvitou.employee_management.model.dto.CreatePositionDto;
import com.ekvitou.employee_management.model.dto.PositionResponseDto;
import com.ekvitou.employee_management.model.dto.UpdatePositionDto;
import com.ekvitou.employee_management.model.entity.Position;
import com.ekvitou.employee_management.model.repository.PositionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PositionServiceImpl implements PositionService {
    private final PositionRepository positionRepository;
    private final PositionMapstruct positionMapstruct;

    @Override
    public PositionResponseDto createPosition(CreatePositionDto dto) {
        if (positionRepository.existsByPositionNameAndIsDeletedIsFalse(dto.name())) {
            throw new DuplicatePositionException("Position name already exists");
        }
        Position position = new Position();
        position.setPositionName(dto.name());
        position.setPositionLevel(dto.level());
        position.setIsDeleted(false);
        positionRepository.save(position);
        return positionMapstruct.mapFromPositionToPositionResponseDto(position);
    }

    @Override
    public String deletePositionByUuid(String uuid) {
        Position position = positionRepository.findPositionByUuid(uuid)
                .orElseThrow(() -> new PositionNotFoundException("Position not found"));
        if (Boolean.TRUE.equals(position.getIsDeleted())) {
            throw new PositionAlreadyDeletedException("Position is already deleted");
        }
        if (!position.getEmployees().isEmpty()) {
            throw new PositionDeleteException("Cannot delete position with assigned employees");
        }
        position.setIsDeleted(true);
        positionRepository.save(position);
        return position.getUuid();
    }

    @Override
    public PositionResponseDto updatePositionByUuid(String uuid, UpdatePositionDto dto) {
        Position position = positionRepository.findPositionByUuid(uuid)
                .orElseThrow(() -> new PositionNotFoundException("Position not found"));
        if (Boolean.TRUE.equals(position.getIsDeleted())) {
            throw new PositionAlreadyDeletedException("Cannot update deleted position");
        }
        if (!position.getPositionName().equals(dto.name())
                && positionRepository.existsByPositionNameAndIsDeletedIsFalse(dto.name())) {
            throw new DuplicatePositionException("Position name already exists");
        }
        position.setPositionName(dto.name());
        position.setPositionLevel(dto.level());
        positionRepository.save(position);
        return positionMapstruct.mapFromPositionToPositionResponseDto(position);
    }

    @Override
    public PositionResponseDto getPositionByUuid(String uuid) {
        Position position = positionRepository.findPositionByUuid(uuid)
                .orElseThrow(() -> new PositionNotFoundException("Position not found"));
        if (Boolean.TRUE.equals(position.getIsDeleted())) {
            throw new PositionAlreadyDeletedException("Position is already deleted");
        }
        return positionMapstruct.mapFromPositionToPositionResponseDto(position);
    }

    @Override
    public Page<PositionResponseDto> getAllPositions(Pageable pageable) {
        return positionRepository.findPositionsByIsDeletedIsFalse(pageable)
                .map(positionMapstruct::mapFromPositionToPositionResponseDto);
    }
}
