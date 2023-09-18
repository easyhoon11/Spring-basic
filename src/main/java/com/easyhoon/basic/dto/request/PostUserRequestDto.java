package com.easyhoon.basic.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostUserRequestDto {
    @Email
    @NotBlank
    private String email;
    @NotBlank
    @Length(min = 8)
    private String password;
    @NotBlank
    private String nickname;
    @NotBlank
    @Pattern(regexp = "^[0-9]{11,13}$")
    private String telNumber;
    @NotBlank
    private String address;
    private String addressDetail;
}
