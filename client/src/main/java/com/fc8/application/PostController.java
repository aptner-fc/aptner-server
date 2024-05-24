package com.fc8.application;

import com.fc8.annotation.CheckApartType;
import com.fc8.annotation.CheckCurrentMember;
import com.fc8.facade.PostFacade;
import com.fc8.platform.common.exception.code.SuccessCode;
import com.fc8.platform.common.response.CommonResponse;
import com.fc8.platform.dto.record.CurrentMember;
import com.fc8.platform.dto.request.WritePostRequest;
import com.fc8.platform.dto.response.WritePostResponse;
import com.fc8.platform.mapper.PostMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "소통 게시판 관련 API", description = "소통 게시판 관련 API 모음 입니다.")
@RestController
@RequestMapping(value = {"/v1/api/posts"})
@RequiredArgsConstructor
public class PostController {

    private final PostMapper postMapper;
    private final PostFacade postFacade;

    @Operation(summary = "소통 게시판 글 작성 API", description = "소통 게시판 글 작성 API 입니다.")
    @CheckApartType
    @PostMapping(value = "/{apartCode}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommonResponse<WritePostResponse>> write(
            @NotNull @PathVariable String apartCode,
            @CheckCurrentMember CurrentMember currentMember,
            @Valid @RequestPart(value = "request") WritePostRequest request,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        var command = postMapper.of(request);
        return CommonResponse.success(SuccessCode.SUCCESS, postFacade.write(currentMember.id(), apartCode, command, image));
    }

}
