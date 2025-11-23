package com.jigit.backend.user.presentation;

import com.jigit.backend.global.exception.ErrorResponse;
import com.jigit.backend.user.presentation.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * API documentation interface for authentication operations
 */
@Tag(name = "Authentication", description = "사용자 인증 API")
public interface AuthControllerDocs {

    @Operation(
            summary = "회원가입",
            description = "새로운 사용자를 등록합니다. 비밀번호는 bcrypt로 해시화되어 저장됩니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원가입 성공",
                    content = @Content(
                            schema = @Schema(implementation = SignupResponse.class),
                            examples = @ExampleObject(
                                    name = "회원가입 성공",
                                    value = """
                                            {
                                              "userId": 1
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "유효하지 않은 입력값",
                                            value = """
                                                    {
                                                      "code": "INVALID_PASSWORD",
                                                      "message": "Invalid Password",
                                                      "path": "/api/auth/signup",
                                                      "timestamp": "2025-01-24T10:30:00"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "409", description = "중복된 사용자명",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "중복된 사용자명",
                                    value = """
                                            {
                                              "code": "DUPLICATE_USERNAME",
                                              "message": "Duplicate Username",
                                              "path": "/api/auth/signup",
                                              "timestamp": "2025-01-24T10:30:00"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "서버 내부 오류",
                                    value = """
                                            {
                                              "code": "INTERNAL_SERVER_ERROR",
                                              "message": "Internal Server Error",
                                              "path": "/api/auth/signup",
                                              "timestamp": "2025-01-24T10:30:00"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/signup")
    ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request);

    @Operation(
            summary = "로그인",
            description = "사용자 인증 후 JWT 토큰을 발급합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공",
                    content = @Content(
                            schema = @Schema(implementation = LoginResponse.class),
                            examples = @ExampleObject(
                                    name = "로그인 성공",
                                    value = """
                                            {
                                              "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiaWF0IjoxNTE2MjM5MDIyLCJleHAiOjE1MTYzMjU0MjJ9.4Adcj0MhB_VZ7p5L5K8x0Q8Z3zW8ZqJ8Zy5X2Z3QZ3Q",
                                              "userId": 1
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "잘못된 인증 정보",
                                    value = """
                                            {
                                              "code": "INVALID_CREDENTIALS",
                                              "message": "Invalid Credentials",
                                              "path": "/api/auth/login",
                                              "timestamp": "2025-01-24T10:30:00"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "토큰 생성 실패",
                                    value = """
                                            {
                                              "code": "TOKEN_GENERATION_FAILED",
                                              "message": "Token Generation Failed",
                                              "path": "/api/auth/login",
                                              "timestamp": "2025-01-24T10:30:00"
                                            }
                                            """
                            )
                    )
            )
    })
    @PostMapping("/login")
    ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request);

    @Operation(
            summary = "토큰 검증",
            description = "JWT 토큰의 유효성을 검증하고 사용자 정보를 반환합니다."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 검증 완료",
                    content = @Content(
                            schema = @Schema(implementation = ValidateResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "유효한 토큰",
                                            value = """
                                                    {
                                                      "isValid": true,
                                                      "userId": 1
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "유효하지 않은 토큰",
                                            value = """
                                                    {
                                                      "isValid": false,
                                                      "userId": null
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(
                                    name = "서버 내부 오류",
                                    value = """
                                            {
                                              "code": "INTERNAL_SERVER_ERROR",
                                              "message": "Internal Server Error",
                                              "path": "/api/auth/validate",
                                              "timestamp": "2025-01-24T10:30:00"
                                            }
                                            """
                            )
                    )
            )
    })
    @GetMapping("/validate")
    ResponseEntity<ValidateResponse> validateToken(@RequestHeader("Authorization") String authorizationHeader);
}
