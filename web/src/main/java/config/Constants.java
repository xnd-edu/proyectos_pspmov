package config;

public final class Constants {
    private Constants() {}

    public static final String TEMPLATE_PREFIX = "/WEB-INF/templates/";
    public static final String TEMPLATE_SUFFIX = ".html";
    public static final String TEMPLATE_MODE = "HTML";
    public static final String TEMPLATE_JUEGO = "juego";
    public static final String TEMPLATE_STATS = "estadisticas";
    public static final String TEMPLATE_END = "gameover";
    public static final String CHARACTER_ENCODING = "UTF-8";
    public static final String TEMPLATE_ENGINE_ATTR = "com.dam.TemplateEngine";
    public static final String CONTENT_TYPE = "text/html;charset=UTF-8";

    public static final String URL_HOME = "/web_war_exploded";
    public static final String URL_JUEGO = "/juego";
    public static final String URL_STATS = "/stats";

    public static final int MAX_INTENTOS = 5;
    public static final int MIN_NUMERO = 1;
    public static final int MAX_NUMERO = 100;

    public static final String MSG_INICIO = "¡Adivina el número entre %d y %d! Tienes %d intentos.";
    public static final String MSG_FUERA_RANGO = "El número debe estar entre %d y %d";
    public static final String MSG_GANADO = "¡Felicidades! Has adivinado el número %d en %d intentos.";
    public static final String MSG_PERDIDO = "¡Game Over! Te has quedado sin intentos. El número era %d";
    public static final String MSG_MAYOR = "El número secreto es MAYOR. Te quedan %d intentos.";
    public static final String MSG_MENOR = "El número secreto es MENOR. Te quedan %d intentos.";
    public static final String MSG_NUMERO_INVALIDO = "Por favor, introduce un número válido.";
}
