package com.capstone.pick.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final int errorCode;
    private final String errorMessage;
}
