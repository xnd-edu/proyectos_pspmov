package org.example.apilogin.common;

public final class Constantes {

    public static final String MSG_ERROR_QR_CODE = "Error generating 2FA QR code";

    private Constantes() {}

    public static final String APP_NAME = "API Renos";

    public static final String URL = "http://localhost:8080";

    public static final String SPRING_JWT_SECRET = "${application.security.jwt.secret-key}";
    public static final String SPRING_JWT_EXPIRATION = "${application.security.jwt.expiration}";
    public static final String SPRING_JWT_REFRESH_EXPIRATION = "${application.security.jwt.refresh-token.expiration}";
    public static final String SPRING_2FA_CODE_EXPIRATION = "${application.security.jwt.pretoken.expiration}";

    public static final String HEADER_X_PRETOKEN = "X-PreToken";

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
    public static final String MSG_REGISTRO_SIN_ACTIVACION_EXITOSO = "Te has registrado exitosamente.";
    public static final String MSG_USERNAME_YA_EXISTE = "El username ya existe";
    public static final String MSG_CREDENCIALES_INVALIDAS = "Credenciales inválidas.";
    public static final String MSG_CUENTA_ACTIVADA_EXITOSAMENTE = "¡Cuenta activada exitosamente!";
    public static final String MSG_CUENTA_YA_ACTIVADA = "La cuenta ya está activada.";
    public static final String MSG_ERROR_ACTIVACION = "Error al activar la cuenta. El código puede ser inválido o ya ha expirado.";
    public static final String MSG_USUARIO_NO_ENCONTRADO = "No se encontró un usuario con el nombre de usuario proporcionado.";
    public static final String MSG_CODIGO_ACTIVACION_NO_ENCONTRADO = "No se encontró un usuario con el código de activación proporcionado.";
    public static final String MSG_CODIGO_ACTIVACION_EXPIRADO = "El código de activación ha expirado.";
    public static final String MSG_RENO_NO_ENCONTRADO = "Reno con ID %d no encontrado";
    public static final String MSG_TOKEN_NO_ENCONTRADO = "Token no encontrado";
    public static final String MSG_ERR_PUBLIC_KEY = "Error al procesar la clave pública del usuario.";
    public static final String MSG_ERROR_COMPARTIDO_NO_ENCONTRADO = "No se encontró un secreto compartido.";
    public static final String MSG_SECRETO_NO_ENCONTRADO = "Secreto no encontrado";
    public static final String MSG_CLAVE_PUBLICA_NO_ENCONTRADA = "Clave pública no encontrada para el usuario";
    public static final String MSG_CERTIFICAR_CLAVE_PUBLICA_ERROR = "Error al certificar la clave pública del usuario: %s";

    public static final String EMAIL_SUBJECT_ACTIVACION = "Activa tu cuenta - Código de activación";
    public static final String EMAIL_SUBJECT_2FA = "Código de verificación 2FA";
    public static final String EMAIL_ERROR_ENVIO = "Error al enviar el email de activación";
    public static final String CHARSET_UTF8 = "UTF-8";

    public static final String API_ADMIN_RENOS = "/api/admin/renos";
    public static final String API_USER_RENOS = "/api/user/renos";
    public static final String API_LOGIN = "/login";
    public static final String API_LOGOUT = "/logout";
    public static final String API_ENABLE_2FA = "/2fa/enable";
    public static final String API_VERIFY_2FA = "/2fa/verify";
    public static final String API_CONFIRM_2FA = "/2fa/confirm";
    public static final String API_REGISTER = "/register";
    public static final String API_REGISTER_NO_ACTIVATION = "/register/noactivation";

    public static final String API_ACTIVAR_CUENTA = "/activar";
    public static final String API_BY_ID = "/{id}";
    public static final String API_RENO_FILTRAR = "/filtrar";
    public static final String API_JWT = "/token";
    public static final String API_JWT_REFRESH = "/refresh";

    public static final String API_SECRETS = "/api/secrets";
    public static final String API_SECRETS_SHARE = "/api/secrets/share";
    public static final String API_PUBLIC_KEYS = "/api/public-keys";
    public static final String API_BY_USERNAME = "/{username}";

    public static final String API_SIMULATION = "/api/simulation";
    public static final String API_DECRYPT_SECRET = "/decrypt-secret/{id}";
    public static final String API_SIMULATION_CREATE_SECRET = "/create-secret";
    public static final String API_SIMULATION_SHARE_SECRET  = "/share-secret";

    public static final String PREAUTHORIZE_ROLE_ADMIN = "hasRole('ADMIN')";

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
    public static final String TABLE_TOKENS = "tokens";

    public static final String SHARED_SECRETS_TABLE = "shared_secrets";
    public static final String SHARED_SECRET_ID_COLUMN = "secret_id";
    public static final String SHARED_OWNER_ID_COLUMN = "owner_id";
    public static final String SHARED_WITH_ID_COLUMN = "shared_with_id";
    public static final String SHARED_ENCRYPTED_DATA_COLUMN = "encrypted_data";
    public static final String SHARED_ENCRYPTED_KEY_COLUMN = "encrypted_key";
    public static final String SHARED_IV_COLUMN = "iv";
    public static final String SHARED_CREATED_AT_COLUMN = "created_at";

    public static final String VAULT_SECRETS_TABLE = "vault_secrets";
    public static final String SECRET_USER_ID_COLUMN = "user_id";
    public static final String SECRET_ENCRYPTED_DATA_COLUMN = "encrypted_data";
    public static final String SECRET_IV_COLUMN = "iv";
    public static final String SECRET_SALT_COLUMN = "salt";
    public static final String SECRET_CREATED_AT_COLUMN = "created_at";

    public static final String USER_PUBLIC_KEYS_TABLE = "user_public_keys";
    public static final String USER_PUBLIC_KEY_USER_ID_COLUMN = "user_id";
    public static final String USER_PUBLIC_KEY_COLUMN = "public_key";
    public static final String USER_PUBLIC_KEY_SERVER_SIGNATURE_COLUMN = "server_signature";
    public static final String USER_PUBLIC_KEY_CREATED_AT_COLUMN = "created_at";

    public static final String JWT_CLAIM_AUTH = "auth";
    public static final String JWT_CLAIM_USER_ID = "userId";
    public static final String JWT_LOG_ERROR_FIRMA = "Error al generar la clave de firma JWT: Algoritmo {} no encontrado";
    public static final String JWT_ALGORITHM_SHA512 = "SHA-512";
    public static final String JWT_ALGORITHM_AES = "AES";
    public static final int JWT_KEY_SIZE = 64;
    public static final String PENDING2FA = "pending2FA";

    public static final int JWT_BEARER_PREFIX_LENGTH = 7;
    public static final String JWT_HEADER_AUTHORIZATION = "Authorization";
    public static final String JWT_TOKEN_TYPE_BEARER = "Bearer";
    public static final String JWT_ROLE_PREFIX = "ROLE_";
    public static final String JWT_REFRESH_TOKEN_INVALIDO = "Refresh token inválido o expirado";
    public static final String JWT_USER_ID_INVALID_TYPE = "userId claim is not a number: %s";
    public static final String JWT_NO_ROL_ENCONTRADO = "No se encontró rol en las authorities";

    public static final String QUERY_REVOKE_TOKEN = "UPDATE TokenEntity t SET t.revoked = true WHERE t.token = :token";

    public static final String AES_ALGORITHM = "AES";
    public static final String AES_TRANSFORMATION = "AES/GCM/NoPadding";
    public static final int AES_KEY_SIZE = 256;
    public static final int GCM_TAG_LENGTH = 128;
    public static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256";
    public static final int PBKDF2_ITERATIONS = 65536;
    public static final String RSA_ALGORITHM = "RSA";
    public static final int RSA_KEY_SIZE = 2048;
    public static final int IV_SIZE = 12; // Tamaño recomendado para GCM
    public static final int SALT_SIZE = 16; // Tamaño común para salting
    public static final String OAEP_PADDING = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    public static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    public static final String SERVER_SIGNATURE_MESSAGE = "UserId:%d|PublicKey:%s|CreatedAt:%s";
    public static final String PRIVATE_KEY_FILE = "server_private.key";
    public static final String PUBLIC_KEY_FILE = "server_public.key";
    public static final String PRIVATE_KEY_FILE_NOT_FOUND = "No se encontró el archivo server_private.key en resources";
    public static final String ERROR_LEER_CLAVE_PRIVADA = "Error al leer la clave privada del servidor: %s";
}
