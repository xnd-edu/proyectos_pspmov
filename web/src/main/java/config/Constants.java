package config;

public final class Constants {
    private Constants() {}

    public static final String TEMPLATE_PREFIX = "/WEB-INF/templates/";
    public static final String TEMPLATE_SUFFIX = ".html";
    public static final String TEMPLATE_MODE = "HTML";
    public static final String TEMPLATE_JUEGO = "juego";
    public static final String TEMPLATE_STATS = "estadisticas";
    public static final String CHARACTER_ENCODING = "UTF-8";
    public static final String TEMPLATE_ENGINE_ATTR = "com.dam.TemplateEngine";
    public static final String CONTENT_TYPE = "text/html;charset=UTF-8";

    public static final int MAX_INTENTOS = 5;
    public static final int MIN_NUMERO = 1;
    public static final int MAX_NUMERO = 100;
}
