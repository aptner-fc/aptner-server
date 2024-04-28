package com.fc8.aptner;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 블루그린 배포 시 4xx 에러 방지를 위함
 */

@RestController
public class DefaultController {

    @GetMapping("/")
    public String home() {
        return "<h1>APTNER<h1>";
    }
}