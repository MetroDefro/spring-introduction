package org.sparta.springintroduction.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.sparta.springintroduction.dto.LoginRequestDto;
import org.sparta.springintroduction.dto.SignupRequestDto;
import org.sparta.springintroduction.entity.User;
import org.sparta.springintroduction.entity.UserRoleEnum;
import org.sparta.springintroduction.jwt.JwtUtil;
import org.sparta.springintroduction.repository.UserRepository;
import org.sparta.springintroduction.security.UserDetailsImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            // DB에 이미 존재하는 username으로 회원가입을 요청할 때
            throw new IllegalArgumentException("중복된 username 입니다.");
        }

        // 사용자 ROLE 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = requestDto.toEntity(password, role);

        userRepository.save(user);
    }

    public HttpHeaders login(LoginRequestDto requestDto) {

        User user = this.userRepository.findByUsername(requestDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("회원을 찾을 수 없습니다."));
        if(!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("회원을 찾을 수 없습니다.");
        }

        // 인증 처리 메서드. 인증 토큰 전해준다.
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        requestDto.getUsername(),
                        requestDto.getPassword(),
                        null
                ));

        String username = ((UserDetailsImpl) auth.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) auth.getPrincipal()).getUser().getRole();

        // 토큰 생성해서 헤더에 넣음.
        String accesstoken = jwtUtil.createToken(JwtUtil.ACCESS_TOKEN_HEADER, username, role, JwtUtil.ACCESS_TOKEN_TIME);
        String refreshtoken = jwtUtil.createToken(JwtUtil.REFRESH_TOKEN_HEADER,username, role, JwtUtil.REFLESH_TOKEN_TIME);

        HttpHeaders headers = new HttpHeaders();
        headers.add(JwtUtil.ACCESS_TOKEN_HEADER, accesstoken);
        headers.add(JwtUtil.REFRESH_TOKEN_HEADER, refreshtoken);

        return headers;
    }
}
