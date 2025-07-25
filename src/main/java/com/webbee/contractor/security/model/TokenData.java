package com.webbee.contractor.security.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * Модель данных, извлеченных из JWT токена.
 * @author Evseeva Tsvetolina
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenData {

    private Long id;
    private String username;
    private String token;
    private List<? extends GrantedAuthority> authorities;

}
