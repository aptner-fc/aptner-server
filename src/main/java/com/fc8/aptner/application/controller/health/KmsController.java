package com.fc8.aptner.application.controller.health;

import com.fc8.aptner.common.exception.code.SuccessCode;
import com.fc8.aptner.common.response.CommonResponse;
import com.fc8.aptner.common.utils.KmsUtils;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = {"/v1/api/health/kms", "/api/health/kms"})
@RequiredArgsConstructor
public class KmsController {

    private final KmsUtils kmsUtils;

    @GetMapping("/encrypt")
    public ResponseEntity<CommonResponse<String>> getEncryptText(
            @NotNull @RequestParam String plainText) {
        return CommonResponse.success(SuccessCode.SUCCESS, kmsUtils.encrypt(plainText));
    }

    @GetMapping("/decrypt")
    public ResponseEntity<CommonResponse<String>> getDecryptText(
            @NotNull @RequestParam String encryptText) {
        return CommonResponse.success(SuccessCode.SUCCESS, kmsUtils.decrypt(encryptText));
    }

}
