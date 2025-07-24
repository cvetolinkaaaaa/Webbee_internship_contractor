package com.webbee.contractor.utils;

import com.webbee.contractor.security.model.TokenAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

/**
 * Сервис для получения текущего пользователя из JWT токена.
 */
@Service
public class UserIdService {

    /**
     * Получает ID текущего пользователя из JWT токена.
     */
    public String getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof TokenAuthentication tokenAuth) {
            Long userId = tokenAuth.getTokenData().getId();
            return userId != null ? userId.toString() : "system";
        }
        return authentication != null ? authentication.getName() : "system";

    }

}
