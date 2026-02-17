package com.ekvitou.employee_management.controller;

import com.ekvitou.employee_management.model.dto.CreatePositionDto;
import com.ekvitou.employee_management.model.dto.UpdatePositionDto;
import com.ekvitou.employee_management.model.service.PositionService;
import com.ekvitou.employee_management.utils.ResponseTemplate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.Date;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/positions")
public class PositionController {
    private final PositionService positionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    public ResponseTemplate<Object> createPosition(@RequestBody @Valid CreatePositionDto createPositionDto) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.CREATED.toString())
                .message("Successfully created a position!")
                .date(Date.from(Instant.now()))
                .data(positionService.createPosition(createPositionDto))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @DeleteMapping("/{uuid}")
    public ResponseTemplate<Object> deletePositionByUuid(@PathVariable String uuid) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully deleted a position!")
                .date(Date.from(Instant.now()))
                .data(positionService.deletePositionByUuid(uuid))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR')")
    @PatchMapping("/{uuid}")
    public ResponseTemplate<Object> updatePositionByUuid(@PathVariable String uuid, @RequestBody @Valid UpdatePositionDto updatePositionDto) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully updated a position!")
                .date(Date.from(Instant.now()))
                .data(positionService.updatePositionByUuid(uuid, updatePositionDto))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR','USER')")
    @GetMapping("/{uuid}")
    public ResponseTemplate<Object> getPositionByUuid(@PathVariable String uuid) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully retrieved a position!")
                .date(Date.from(Instant.now()))
                .data(positionService.getPositionByUuid(uuid))
                .build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','HR','USER')")
    @GetMapping
    public ResponseTemplate<Object> getAllPositions(Pageable pageable) {
        return ResponseTemplate
                .builder()
                .status(HttpStatus.OK.toString())
                .message("Successfully retrieved all positions!")
                .date(Date.from(Instant.now()))
                .data(positionService.getAllPositions(pageable))
                .build();
    }
}
