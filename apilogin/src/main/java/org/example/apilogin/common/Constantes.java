package org.example.apilogin.common;

public final class Constantes {

    public static final String MSG_ERROR_QR_CODE = "Error generating 2FA QR code";

    private Constantes() {}

    public static final String APP_NAME = "API Renos";

    public static final String URL = "http://localhost:8080";

    public static final String SESSION_ATTR_USUARIO = "usuario";
    public static final String SESSION_ATTR_2FA_PENDING_USER_ID = "2fa_pending_user_id";

    public static final String MSG_LOGIN_EXITOSO = "Login exitoso";
    public static final String MSG_LOGOUT_EXITOSO = "Logout exitoso";
    public static final String MSG_2FA_ENABLED = "Escanea el código QR con tu aplicación de autenticación 2FA, o ingresa el código manualmente.";
    public static final String MSG_2FA_CONFIRMED = "2FA ha sido habilitado exitosamente en tu cuenta.";
    public static final String MSG_2FA_REQUIRED = "Se requiere verificación 2FA";
    public static final String MSG_2FA_YA_ACTIVADO = "Ya tienes 2FA activado en tu cuenta.";
    public static final String MSG_2FA_EMAIL_SENT = "Se ha enviado un código de verificación a tu correo electrónico.";
    public static final String MSG_METODO_2FA_NO_SOPORTADO = "Método de 2FA no soportado. Usa 'TOTP' o 'MAIL'.";
    public static final String MSG_CODIGO_2FA_EXPIRADO = "El código 2FA ha expirado. Solicita uno nuevo.";
    public static final String MSG_NO_PROCESO_2FA = "No hay ningún proceso de 2FA pendiente.";
    public static final String MSG_REGISTRO_EXITOSO = "Te has registrado exitosamente. Por favor, revisa tu correo para activar tu cuenta.";
    public static final String MSG_USERNAME_YA_EXISTE = "El username ya existe";
    public static final String MSG_CREDENCIALES_INVALIDAS = "Credenciales inválidas.";
    public static final String MSG_DEBE_INICIAR_SESION = "Debe iniciar sesión";
    public static final String MSG_CUENTA_ACTIVADA_EXITOSAMENTE = "¡Cuenta activada exitosamente!";
    public static final String MSG_CUENTA_NO_ACTIVADA = "La cuenta no está activada.";
    public static final String MSG_CUENTA_YA_ACTIVADA = "La cuenta ya está activada.";
    public static final String MSG_ERROR_ACTIVACION = "Error al activar la cuenta. El código puede ser inválido o ya ha expirado.";
    public static final String MSG_USUARIO_NO_ENCONTRADO = "No se encontró un usuario con el nombre de usuario proporcionado.";
    public static final String MSG_CODIGO_ACTIVACION_NO_ENCONTRADO = "No se encontró un usuario con el código de activación proporcionado.";
    public static final String MSG_CODIGO_ACTIVACION_EXPIRADO = "El código de activación ha expirado.";
    public static final String MSG_ACCESO_DENEGADO = "Acceso denegado";
    public static final String MSG_RENO_NO_ENCONTRADO = "Reno con ID %d no encontrado";

    public static final String EMAIL_SUBJECT_ACTIVACION = "Activa tu cuenta - Código de activación";
    public static final String EMAIL_SUBJECT_2FA = "Código de verificación 2FA";
    public static final String EMAIL_ERROR_ENVIO = "Error al enviar el email de activación";
    public static final String CHARSET_UTF8 = "UTF-8";

    public static final String API_BASE_RENOS = "api/renos";
    public static final String API_LOGIN = "/login";
    public static final String API_LOGOUT = "/logout";
    public static final String API_ENABLE_2FA = "/2fa/enable";
    public static final String API_VERIFY_2FA = "/2fa/verify";
    public static final String API_CONFIRM_2FA = "/2fa/confirm";
    public static final String API_REGISTER = "/register";
    public static final String API_ACTIVAR_CUENTA = "/activar";
    public static final String API_RENO_BY_ID = "/{id}";
    public static final String API_RENO_FILTRAR = "/filtrar";
    public static final String API_RENO_UPDATE = "/update/{id}";
    public static final String API_RENO_DELETE = "/delete/{id}";

    public static final String API_PATH_PATTERN = "/api/**";

    public static final String TEMPLATE_EMAIL_ACTIVACION = "email-activacion";
    public static final String TEMPLATE_EMAIL_2FA = "email-2fa";
    public static final String TEMPLATE_ACTIVACION_RESULTADO = "activacion-resultado";

    public static final String PARAM_USUARIO = "usuario";
    public static final String PARAM_CODIGO_ACTIVACION = "codigoActivacion";
    public static final String PARAM_CODIGO_2FA = "codigo2FA";
    public static final String PARAM_URL_ACTIVACION = "urlActivacion";
    public static final String PARAM_EXITOSO = "exitoso";
    public static final String PARAM_MENSAJE = "mensaje";

    public static final String TABLE_USUARIOS = "usuarios";
    public static final String TABLE_RENOS = "renos";
}

