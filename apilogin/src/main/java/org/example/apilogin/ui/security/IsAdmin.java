package org.example.apilogin.ui.security;

import org.example.apilogin.common.Constantes;
import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize(Constantes.PREAUTHORIZE_ROLE_ADMIN)
public @interface IsAdmin {
}
