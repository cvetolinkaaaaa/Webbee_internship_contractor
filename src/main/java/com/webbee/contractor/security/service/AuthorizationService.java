package com.webbee.contractor.security.service;

import com.webbee.contractor.dto.ContractorSearchRequest;
import com.webbee.contractor.security.model.UserRole;
import com.webbee.contractor.security.model.TokenAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Сервис для проверки прав доступа.
 * @author Evseeva Tsvetolina
 */
@Service
public class AuthorizationService {

    /**
     * Проверяет, может ли текущий пользователь выполнять операции записи с контрагентами.
     */
    public boolean canModifyContractors() {

        Set<String> userRoles = getCurrentUserRoles();
        return hasAnyRole(userRoles, UserRole.SUPERUSER, UserRole.CONTRACTOR_SUPERUSER);

    }

    /**
     * Проверяет, может ли текущий пользователь просматривать справочную информацию.
     */
    public boolean canViewReferenceData() {

        Set<String> userRoles = getCurrentUserRoles();
        if (hasRole(userRoles, UserRole.ADMIN) && !hasRole(userRoles, UserRole.SUPERUSER)) {
            return false;
        }
        return hasAnyRole(userRoles,
                UserRole.USER, UserRole.CONTRACTOR_RUS,
                UserRole.CONTRACTOR_SUPERUSER, UserRole.SUPERUSER);

    }

    /**
     * Проверяет, может ли текущий пользователь редактировать справочную информацию.
     */
    public boolean canEditReferenceData() {

        Set<String> userRoles = getCurrentUserRoles();
        return hasAnyRole(userRoles, UserRole.CONTRACTOR_SUPERUSER, UserRole.SUPERUSER);

    }

    /**
     * Проверяет, может ли текущий пользователь выполнять поиск контрагентов.
     */
    public boolean canSearchContractors() {

        Set<String> userRoles = getCurrentUserRoles();
        if (hasRole(userRoles, UserRole.ADMIN) && !hasRole(userRoles, UserRole.SUPERUSER)) {
            return false;
        }
        return hasAnyRole(userRoles,
                UserRole.CONTRACTOR_RUS, UserRole.CONTRACTOR_SUPERUSER, UserRole.SUPERUSER);

    }

    /**
     * Применяет фильтрацию по правам доступа к запросу поиска контрагентов.
     */
    public ContractorSearchRequest applyContractorAccessFilter(ContractorSearchRequest request) {

        Set<String> userRoles = getCurrentUserRoles();
        if (hasAnyRole(userRoles, UserRole.SUPERUSER, UserRole.CONTRACTOR_SUPERUSER)) {
            return request;
        }
        if (hasRole(userRoles, UserRole.CONTRACTOR_RUS) &&
            !hasAnyRole(userRoles, UserRole.CONTRACTOR_SUPERUSER, UserRole.SUPERUSER)) {
            request.setCountry("RUS");
        }
        return request;

    }

    /**
     * Получает роли текущего аутентифицированного пользователя.
     */
    private Set<String> getCurrentUserRoles() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof TokenAuthentication) {
            return authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toSet());
        }
        return Set.of();

    }

    /**
     * Проверяет, имеет ли пользователь указанную роль.
     */
    private boolean hasRole(Set<String> userRoles, UserRole role) {

        return userRoles.contains("ROLE_" + role.name()) || userRoles.contains(role.name());

    }

    /**
     * Проверяет, имеет ли пользователь любую из указанных ролей.
     */
    private boolean hasAnyRole(Set<String> userRoles, UserRole... roles) {

        for (UserRole role : roles) {
            if (hasRole(userRoles, role)) {
                return true;
            }
        }
        return false;

    }

}
