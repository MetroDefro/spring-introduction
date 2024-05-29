package org.sparta.springintroduction.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.sparta.springintroduction.entity.LoginRecord;
import org.sparta.springintroduction.repository.LoginRecordRepository;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class LoginRecodeAspect {

    private final LoginRecordRepository loginRecordRepository;

    private final static String LOGIN_BEFORE = "로그인 수행 전";
    private final static String LOGIN_SUCCESS = "로그인 성공";
    private final static String LOGIN_FAIL = "로그인 실패";

    @Before("execution(* org.sparta.springintroduction.security.UserDetailsServiceImpl.loadUserByUsername(..))")
    public void loginBefore(JoinPoint joinPoint){
        String username = (String) joinPoint.getArgs()[0];

        LoginRecord loginRecord = LoginRecord.builder()
                                    .username(username)
                                    .type(LOGIN_BEFORE)
                                    .build();
        loginRecordRepository.save(loginRecord);

        log.info(LOGIN_BEFORE + ": " + username);
    }

    @AfterReturning("execution(* org.sparta.springintroduction.security.UserDetailsServiceImpl.loadUserByUsername(..))")
    public void loginAfterReturning(JoinPoint joinPoint){
        String username = (String) joinPoint.getArgs()[0];

        LoginRecord loginRecord = LoginRecord.builder()
                .username(username)
                .type(LOGIN_SUCCESS)
                .build();
        loginRecordRepository.save(loginRecord);

        log.info(LOGIN_SUCCESS + ": " + username);
    }

    @AfterThrowing("execution(* org.sparta.springintroduction.security.UserDetailsServiceImpl.loadUserByUsername(..))")
    public void loginAfterThrowing(JoinPoint joinPoint){
        String username = (String) joinPoint.getArgs()[0];

        LoginRecord loginRecord = LoginRecord.builder()
                .username(username)
                .type(LOGIN_FAIL)
                .build();
        loginRecordRepository.save(loginRecord);

        log.info(LOGIN_FAIL + ": " + username);
    }
}
