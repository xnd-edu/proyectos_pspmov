package org.example.apilogin.common;

public final class Constantes {

    private Constantes() {}

    public static final String SESSION_ATTR_USUARIO = "usuario";

    public static final String MSG_LOGIN_EXITOSO = "Login exitoso";
    public static final String MSG_LOGOUT_EXITOSO = "Logout exitoso";
    public static final String MSG_CREDENCIALES_INVALIDAS = "Credenciales inválidas";

    public static final String API_BASE_RENOS = "api/renos";
    public static final String API_LOGIN = "/login";
    public static final String API_LOGOUT = "/logout";
    public static final String API_RENO_BY_ID = "/{id}";
    public static final String API_RENO_FILTRAR = "/filtrar";
    public static final String API_RENO_UPDATE = "/update/{id}";
    public static final String API_RENO_DELETE = "/delete/{id}";

    public static final String TABLE_USUARIOS = "usuarios";
    public static final String TABLE_RENOS = "renos";
}

