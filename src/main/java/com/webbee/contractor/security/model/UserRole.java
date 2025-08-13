package com.webbee.contractor.security.model;

/**
 * Перечисление ролей пользователей в системе.
 * @author Evseeva Tsvetolina
 */
public enum UserRole {

    /**
     * Может просматривать, но не редактировать справочную информацию.
     */
    USER,

    /**
     * Может все что USER + просматривать контрагентов с country = RUS.
     */
    CONTRACTOR_RUS,

    /**
     * Может все что USER + полный доступ к сервису contractor.
     */
    CONTRACTOR_SUPERUSER,

    /**
     * Полный доступ к сервисy и contractor.
     */
    SUPERUSER,

    /**
     * Полный доступ к сервису auth, но без доступа contractor.
     */
    ADMIN

}
