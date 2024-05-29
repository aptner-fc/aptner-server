package com.fc8.application;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 무중단 배포 시 4xx 에러 방지를 위함
 */

@RestController
public class DefaultController {

    @GetMapping("/")
    public String home() {
        return "<h1>aptner<h1>";
    }

    @GetMapping("/health")
    public String health() {
        return "<h1>aptner<h1>";
    }

}