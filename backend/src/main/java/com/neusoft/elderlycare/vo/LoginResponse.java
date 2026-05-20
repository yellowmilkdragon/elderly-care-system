package com.neusoft.elderlycare.vo;

import java.util.List;

public record LoginResponse(
        String token,
        UserProfile profile,
        List<String> permissions
) {
}
