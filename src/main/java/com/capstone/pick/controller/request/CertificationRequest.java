package com.capstone.pick.controller.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CertificationRequest {

    private String toPhoneNumber;
    private String userCheckNumber;
}
