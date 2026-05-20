package com.neusoft.elderlycare.vo;

public record UserProfile(
        Long id,
        String username,
        String nickname,
        String roleCode
) {
}
