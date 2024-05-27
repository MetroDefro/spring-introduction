package org.sparta.springintroduction.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.sparta.springintroduction.dto.LoginRequestDto;
import org.sparta.springintroduction.entity.UserRoleEnum;
import org.sparta.springintroduction.jwt.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/sprig-introduction/user/login");
    }

    // 로그인 시도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
            // 인증 처리 메서드. 인증 토큰 전해준다.
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    // 로그인 성공시 JWT 생성
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        // 유저네임, 권한 가져와서
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        // 토큰 생성해서 헤더에 넣음.
        String accesstoken = jwtUtil.createToken(JwtUtil.ACCESS_TOKEN_HEADER, username, role, JwtUtil.ACCESS_TOKEN_TIME);
        String refreshtoken = jwtUtil.createToken(JwtUtil.REFRESH_TOKEN_HEADER,username, role, JwtUtil.REFLESH_TOKEN_TIME);

        response.addHeader(JwtUtil.ACCESS_TOKEN_HEADER, accesstoken);
        response.addHeader(JwtUtil.REFRESH_TOKEN_HEADER, refreshtoken);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print("로그인에 성공하였습니다.");
    }

    // 로그인 실패시
    // 로그인 시, 전달된 username과 password 중 맞지 않는 정보가 있을 때
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        response.setStatus(400);
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print("회원을 찾을 수 없습니다.");
    }

}