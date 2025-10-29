import config.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;

@WebServlet(Constants.URL_JUEGO)
public class JuegoServlet extends HttpServlet  {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        String usuario = req.getParameter("usuario");
        if (usuario != null && !usuario.isBlank()) {
            session.setAttribute("usuario", usuario);
        }

        AdivinaNumero juego = obtenerOCrearJuego(session);
        renderizarJuego(req, resp, juego, juego.getMensaje());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        AdivinaNumero juego = obtenerOCrearJuego(session);

        String accion = req.getParameter("accion");
        String usuario =  req.getParameter("usuario");
        if (usuario != null && !usuario.isBlank()) {
            session.setAttribute("usuario", usuario);
        }

        String mensaje;

        if ("reiniciar".equals(accion)) {
            juego = new AdivinaNumero();
            session.setAttribute("juego", juego);
            mensaje = juego.getMensaje();
        } else {
            String intentoStr = req.getParameter("intento");
            try {
                int intento = Integer.parseInt(intentoStr);
                boolean estabaTerminado = juego.isJuegoTerminado();
                mensaje = juego.intentar(intento);

                if (!estabaTerminado && juego.isJuegoTerminado()) {
                    Estadisticas estadisticas = (Estadisticas) session.getAttribute("estadisticas");
                    if (estadisticas != null) {
                        estadisticas.agregarPartida(juego);
                    }
                }
            } catch (NumberFormatException e) {
                mensaje = Constants.MSG_NUMERO_INVALIDO;
            }
        }

        renderizarJuego(req, resp, juego, mensaje);
    }

    private AdivinaNumero obtenerOCrearJuego(HttpSession session) {
        AdivinaNumero juego = (AdivinaNumero) session.getAttribute("juego");
        Estadisticas estadisticas = (Estadisticas) session.getAttribute("estadisticas");

        if (estadisticas == null) {
            estadisticas = new Estadisticas((String) session.getAttribute("usuario"));
            session.setAttribute("estadisticas", estadisticas);
        }

        if (juego == null) {
            juego = new AdivinaNumero();
            session.setAttribute("juego", juego);
        }

        return juego;
    }

    private void renderizarJuego(HttpServletRequest req, HttpServletResponse resp,
                                  AdivinaNumero juego, String mensaje) throws IOException {
        var webExchange = JakartaServletWebApplication.buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext ctx = new WebContext(webExchange);

        HttpSession session = req.getSession();
        String usuario = (String) session.getAttribute("usuario");

        String instrucciones = "Tienes " + juego.getMaxIntentos() + " intentos para adivinar el número secreto.";

        ctx.setVariable("juego", juego);
        ctx.setVariable("mensaje", mensaje);
        ctx.setVariable("usuario", usuario);
        ctx.setVariable("instrucciones", instrucciones);

        resp.setContentType(Constants.CONTENT_TYPE);

        String template = juego.isJuegoTerminado() ? Constants.TEMPLATE_END : Constants.TEMPLATE_JUEGO;

        ((TemplateEngine)getServletContext().getAttribute(Constants.TEMPLATE_ENGINE_ATTR))
                .process(template, ctx, resp.getWriter());
    }
}