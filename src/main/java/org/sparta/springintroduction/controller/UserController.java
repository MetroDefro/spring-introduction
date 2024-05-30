package org.sparta.springintroduction.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sparta.springintroduction.dto.LoginRequestDto;
import org.sparta.springintroduction.dto.SignupRequestDto;
import org.sparta.springintroduction.dto.UserInfoDto;
import org.sparta.springintroduction.entity.UserRoleEnum;
import org.sparta.springintroduction.jwt.JwtUtil;
import org.sparta.springintroduction.security.UserDetailsImpl;
import org.sparta.springintroduction.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sprig-introduction")
@Slf4j
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/user/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        userService.signup(requestDto);
        return ResponseEntity.ok("회원 가입에 성공하였습니다.");
    }

    @PostMapping("/user/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequestDto requestDto) {
        HttpHeaders headers = userService.login(requestDto);
        return ResponseEntity.ok()
                .headers(headers)
                .body("로그인에 성공하였습니다.");
    }

    // 회원 관련 정보 받기
    @GetMapping("/user-info")
    @ResponseBody
    public UserInfoDto getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String username = userDetails.getUser().getUsername();
        UserRoleEnum role = userDetails.getUser().getRole();
        boolean isAdmin = (role == UserRoleEnum.ADMIN);

        return new UserInfoDto(username, isAdmin);
    }

    @PostMapping("/user/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {

        //get refresh token
        String refreshtoken = request.getHeader(JwtUtil.REFRESH_TOKEN_HEADER);

        if (refreshtoken == null) {
            return new ResponseEntity<>("refresh token이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        // 유효성 체크
        try {
            jwtUtil.validateToken(refreshtoken);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        String username = (String) jwtUtil.getUserInfoFromToken(refreshtoken).get("sub");
        UserRoleEnum role = UserRoleEnum.valueOf((String) jwtUtil.getUserInfoFromToken(refreshtoken).get("auth"));

        // 엑세스 톸큰 새로 만들어 헤더에 등록
        String newAccess = jwtUtil.createToken(JwtUtil.ACCESS_TOKEN_HEADER, username, role, 600000L);
        response.setHeader(JwtUtil.ACCESS_TOKEN_HEADER, newAccess);

        return new ResponseEntity<>("access token 발급 성공", HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<String> handleException(IllegalArgumentException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<String> handleException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(Objects.requireNonNull(e.getFieldError()).getDefaultMessage(), HttpStatus.BAD_REQUEST);
    }
}