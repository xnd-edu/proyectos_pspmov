package org.example.apilogin.ui.interceptor;

import org.example.apilogin.domain.model.Rol;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequiresAuth {
    Rol rol() default Rol.USER;
}
