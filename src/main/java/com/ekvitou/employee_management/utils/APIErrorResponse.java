package com.ekvitou.employee_management.utils;

import lombok.Builder;

import java.util.Date;

@Builder
public record APIErrorResponse (
        String status,
        Date timeStamp,
        String errorMessage
) {}
