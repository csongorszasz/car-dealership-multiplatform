package edu.bbte.idde.scim2304.web.servlet;

import com.github.jknack.handlebars.Template;
import edu.bbte.idde.scim2304.web.template.HandlebarsTemplateFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(LoginServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.info("GET /login");

        // if already logged in, redirect to home page
        if (req.getSession().getAttribute("username") != null) {
            resp.sendRedirect("home");
            return;
        }

        Template template = HandlebarsTemplateFactory.getTemplate("login");
        template.apply(null, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.info("POST /login");

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if ("admin".equals(username) && "password1".equals(password)) {
            req.getSession().setAttribute("username", username);
            resp.sendRedirect("home");
        } else {
            resp.sendRedirect("login");
        }
    }
}
