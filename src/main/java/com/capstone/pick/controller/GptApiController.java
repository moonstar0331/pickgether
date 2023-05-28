package com.capstone.pick.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class GptApiController {
    @Value("${openai.api-key}")
    private String apiKey;

    @GetMapping("/api/key")
    public ResponseEntity<String> getApiKey() {
        return ResponseEntity.ok(apiKey);
    }
}
