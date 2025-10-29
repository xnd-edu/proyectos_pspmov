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

@WebServlet(Constants.URL_STATS)
public class StatsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String usuario = (String) session.getAttribute("usuario");
        Estadisticas estadisticas = (Estadisticas) session.getAttribute("estadisticas");
        if (estadisticas == null) {
            estadisticas = new Estadisticas(usuario);
            session.setAttribute("estadisticas", estadisticas);
        }

        var webExchange = JakartaServletWebApplication
                .buildApplication(getServletContext())
                .buildExchange(req, resp);
        WebContext ctx = new org.thymeleaf.context.WebContext(webExchange);
        ctx.setVariable("estadisticas", estadisticas);
        resp.setContentType(Constants.CONTENT_TYPE);
        ((TemplateEngine) getServletContext().getAttribute(Constants.TEMPLATE_ENGINE_ATTR))
                .process(Constants.TEMPLATE_STATS, ctx, resp.getWriter());
    }
}
