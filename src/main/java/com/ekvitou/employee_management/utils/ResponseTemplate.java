package com.ekvitou.employee_management.utils;

import lombok.Builder;

import java.util.Date;

@Builder
public record ResponseTemplate<T>(
        String status,
        Date date,
        String message,
        // generic type
        T data
) { }
