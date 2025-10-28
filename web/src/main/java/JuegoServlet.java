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

@WebServlet("/juego")
public class JuegoServlet extends HttpServlet  {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();

        // Capturar el nombre de usuario si viene en el parámetro
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
            juego.reiniciar();
            mensaje = juego.getMensaje();
        } else {
            String intentoStr = req.getParameter("intento");
            try {
                int intento = Integer.parseInt(intentoStr);
                mensaje = juego.intentar(intento);
            } catch (NumberFormatException e) {
                mensaje = "Por favor, introduce un número válido.";
            }
        }

        renderizarJuego(req, resp, juego, mensaje);
    }

    private AdivinaNumero obtenerOCrearJuego(HttpSession session) {
        AdivinaNumero juego = (AdivinaNumero) session.getAttribute("juego");
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

        ctx.setVariable("juego", juego);
        ctx.setVariable("mensaje", mensaje);
        ctx.setVariable("usuario", usuario);

        resp.setContentType(Constants.CONTENT_TYPE);
        ((TemplateEngine)getServletContext().getAttribute(Constants.TEMPLATE_ENGINE_ATTR))
                .process(Constants.TEMPLATE_JUEGO, ctx, resp.getWriter());
    }
}