package org.sparta.springintroduction.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.sparta.springintroduction.entity.UserRoleEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    // Header KEY 값
    public static final String ACCESS_TOKEN_HEADER = "Access";
    public static final String REFRESH_TOKEN_HEADER = "Refresh";
    // 사용자 권한 값의 KEY
    public static final String AUTHORIZATION_KEY = "auth";
    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    public static final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; // 60분
    public static final long REFLESH_TOKEN_TIME = 24 * 60 * 60 * 1000L; // 1일

    @Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
    private String secretKey; // application.properties에 설정한 secretKey 가져온다.
    private Key key; // 최근 이걸로 jwt를 관리한다고 함.
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() { // 생성자가 호출된 뒤 이 메서드가 실행된다.
        byte[] bytes = Base64.getDecoder().decode(secretKey); // 한 번 인코딩 되어있기 때문에 디코딩
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰 생성
    public String createToken(String category, String username, UserRoleEnum role, Long expiredMs) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username) // 사용자 식별자값(ID)
                        .claim("category", category)
                        .claim(AUTHORIZATION_KEY, role) // 사용자 권한
                        .setExpiration(new Date(date.getTime() + expiredMs)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // header 에서 JWT 가져오기
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(ACCESS_TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) { // 공백이 아니며 Bearer로 시작
            return bearerToken.substring(7); // "Bearer "가 7자임
        }
        return null;
    }

    /*
    UnsupportedJwtException : jwt가 예상하는 형식과 다른 형식이거나 구성
    MalformedJwtException : 잘못된 jwt 구조
    ExpiredJwtException : JWT의 유효기간이 초과
    SignatureException : JWT의 서명실패(변조 데이터)
     */

    // 토큰 검증
    public void validateToken(String token) {
        try {
            if(!StringUtils.hasText(token)) {
                throw new JwtException("토큰이 유효하지 않습니다.");
            }
            token = token.replace(BEARER_PREFIX, "");
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            // 토큰이 필요한 API 요청에서 토큰을 전달하지 않았거나 정상 토큰이 아닐 때
        } catch (SecurityException | MalformedJwtException | SignatureException | UnsupportedJwtException |
                 IllegalArgumentException | ExpiredJwtException e) {
            throw new JwtException("토큰이 유효하지 않습니다.");
        }
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        token = token.replace(BEARER_PREFIX, "");
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}