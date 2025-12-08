package org.example.apilogin.common;

public final class Constantes {

    private Constantes() {}

    public static final String URL = "http://localhost:8080";

    public static final String SESSION_ATTR_USUARIO = "usuario";

    public static final String MSG_LOGIN_EXITOSO = "Login exitoso";
    public static final String MSG_LOGOUT_EXITOSO = "Logout exitoso";
    public static final String MSG_CREDENCIALES_INVALIDAS = "Credenciales inválidas";
    public static final String MSG_DEBE_INICIAR_SESION = "Debe iniciar sesión";
    public static final String MSG_CUENTA_ACTIVADA_EXITOSAMENTE = "Cuenta activada exitosamente";
    public static final String MSG_ACCESO_DENEGADO = "Acceso denegado";
    public static final String MSG_RENO_NO_ENCONTRADO = "Reno con ID %d no encontrado";

    public static final String API_BASE_RENOS = "api/renos";
    public static final String API_LOGIN = "/login";
    public static final String API_LOGOUT = "/logout";
    public static final String API_REGISTER = "/register";
    public static final String API_ACTIVAR_CUENTA = "/activar";
    public static final String API_RENO_BY_ID = "/{id}";
    public static final String API_RENO_FILTRAR = "/filtrar";
    public static final String API_RENO_UPDATE = "/update/{id}";
    public static final String API_RENO_DELETE = "/delete/{id}";

    public static final String TABLE_USUARIOS = "usuarios";
    public static final String TABLE_RENOS = "renos";
}

