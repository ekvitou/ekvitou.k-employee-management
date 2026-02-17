package com.ekvitou.employee_management.model.service;

import com.ekvitou.employee_management.model.dto.CreatePositionDto;
import com.ekvitou.employee_management.model.dto.PositionResponseDto;
import com.ekvitou.employee_management.model.dto.UpdatePositionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PositionService {
    PositionResponseDto createPosition(CreatePositionDto createPositionDto);
    String deletePositionByUuid(String uuid);
    PositionResponseDto updatePositionByUuid(String uuid, UpdatePositionDto updatePositionDto);
    PositionResponseDto getPositionByUuid(String uuid);
    Page<PositionResponseDto> getAllPositions(Pageable pageable);
}

