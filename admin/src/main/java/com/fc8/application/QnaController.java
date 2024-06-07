package com.fc8.application;

import com.fc8.annotation.CheckApartType;
import com.fc8.annotation.CheckCurrentAdmin;
import com.fc8.facade.QnaFacade;
import com.fc8.platform.common.exception.code.SuccessCode;
import com.fc8.platform.common.response.CommonResponse;
import com.fc8.platform.domain.enums.ProcessingStatus;
import com.fc8.platform.dto.record.CurrentAdmin;
import com.fc8.platform.dto.request.WriteQnaAnswerRequest;
import com.fc8.platform.dto.response.WriteQnaAnswerResponse;
import com.fc8.platform.mapper.QnaMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = {"/v1/api/qna", "/api/qna"})
@RequiredArgsConstructor
public class QnaController {

    private final QnaMapper qnaMapper;
    private final QnaFacade qnaFacade;

    @CheckApartType
    @PostMapping(value = "/{apartCode}/answers/{qnaId}")
    public ResponseEntity<CommonResponse<WriteQnaAnswerResponse>> writeAnswer(
            @NotNull @PathVariable String apartCode,
            @NotNull @PathVariable Long qnaId,
            @CheckCurrentAdmin CurrentAdmin currentAdmin,
            @Valid @RequestBody WriteQnaAnswerRequest request
    ) {
        var command = qnaMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS_INSERT, qnaFacade.writeAnswer(currentAdmin.id(), qnaId, apartCode, command));
    }

    @CheckApartType
    @PostMapping(value = "/{apartCode}/status/{qnaId}")
    public ResponseEntity<CommonResponse<Void>> changeStatus(
            @NotNull @PathVariable String apartCode,
            @NotNull @PathVariable Long qnaId,
            @CheckCurrentAdmin CurrentAdmin currentAdmin,
            @NotNull(message = "처리 상태가 누락되었습니다.") @RequestParam ProcessingStatus status
    ) {
        qnaFacade.changeStatus(currentAdmin.id(), qnaId, apartCode, status);
        return CommonResponse.success(SuccessCode.SUCCESS_UPDATE);
    }
}
