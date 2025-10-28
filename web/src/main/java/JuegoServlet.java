import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/juego")
public class JuegoServlet extends HttpServlet  {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var contador = 1;
        if (req.getSession().getAttribute("contador") != null) {
            contador = (Integer)req.getSession().getAttribute("contador");
        }

        resp.getWriter().write("Hola "+contador);

        req.getSession().setAttribute("contador", contador+1);




    }
}