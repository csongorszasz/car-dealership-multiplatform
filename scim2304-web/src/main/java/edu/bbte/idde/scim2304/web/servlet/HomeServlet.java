package edu.bbte.idde.scim2304.web.servlet;

import com.github.jknack.handlebars.Template;
import edu.bbte.idde.scim2304.backend.service.ServiceFactory;
import edu.bbte.idde.scim2304.web.template.HandlebarsTemplateFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@WebServlet("/home")
public class HomeServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(HomeServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.info("GET /");

        var adverts = ServiceFactory.getAdvertService().findAllAdverts();

        Map<String, Object> model = new ConcurrentHashMap<>();
        model.put("adverts", adverts);

        // rendering
        Template template = HandlebarsTemplateFactory.getTemplate("index");
        template.apply(model, resp.getWriter());
    }
}
