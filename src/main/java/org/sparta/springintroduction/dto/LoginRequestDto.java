package org.sparta.springintroduction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    @Pattern(regexp="[a-z0-9]{4,10}", message = "username은 최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 구성되어야 합니다.")
    @NotBlank(message = "username 공백일 수 없습니다.")
    private String username;

    @Pattern(regexp="[a-zA-Z0-9]{8,15}", message = "password는  최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9)로 구성되어야 합니다.")
    @NotBlank(message = "password 공백일 수 없습니다.")
    private String password;
}
