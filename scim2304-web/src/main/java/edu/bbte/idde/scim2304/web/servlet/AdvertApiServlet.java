package edu.bbte.idde.scim2304.web.servlet;

import edu.bbte.idde.scim2304.backend.model.Advert;
import edu.bbte.idde.scim2304.backend.repository.exceptions.RepositoryException;
import edu.bbte.idde.scim2304.backend.service.ServiceFactory;
import edu.bbte.idde.scim2304.backend.service.exceptions.FailedCreateException;
import edu.bbte.idde.scim2304.backend.service.exceptions.NotFoundException;
import edu.bbte.idde.scim2304.web.exception.CustomServletException;
import edu.bbte.idde.scim2304.web.om.MessageResponse;
import edu.bbte.idde.scim2304.web.om.ObjectMapperFactory;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


@WebServlet("/adverts")
public class AdvertApiServlet extends HttpServlet {
    private static final Logger LOG = LoggerFactory.getLogger(AdvertApiServlet.class);

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        LOG.info("Initializing AdvertServlet");
    }

    private long parseIdParam(HttpServletRequest req) throws CustomServletException {
        if (req.getParameter("id") == null) {
            throw new CustomServletException(400, "Missing id parameter");
        }
        try {
            return Long.parseLong(req.getParameter("id"));
        } catch (NumberFormatException e) {
            throw new CustomServletException(400, "Invalid id parameter");
        }
    }

    private Advert getAdvertById(Long id) throws CustomServletException {
        try {
            return ServiceFactory.getAdvertService().findAdvertById(id);
        } catch (NotFoundException e) {
            throw new CustomServletException(404, "Advert not found");
        } catch (RepositoryException e) {
            throw new CustomServletException(500, "Internal server error");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.info("GET /adverts");

        resp.setHeader("Content-Type", "application/json");

        // check if "id" request parameter is present
        if (req.getParameter("id") == null) {
            // return all adverts
            try {
                var adverts = ServiceFactory.getAdvertService().findAllAdverts();
                resp.setStatus(200);
                ObjectMapperFactory.getObjectMapper().writeValue(resp.getOutputStream(), adverts);
            } catch (RepositoryException | IOException e) {
                throw new CustomServletException(500, "Internal server error");
            }
            return;
        }

        Long id = parseIdParam(req);
        var advert = getAdvertById(id);
        resp.setStatus(200);
        try {
            ObjectMapperFactory.getObjectMapper().writeValue(resp.getOutputStream(), advert);
        } catch (IOException e) {
            throw new CustomServletException(500, "Internal server error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.info("POST /adverts");

        resp.setHeader("Content-Type", "application/json");

        // try to parse the request body as JSON and create an Advert object from it
        Advert advert;
        try {
            advert = ObjectMapperFactory.getObjectMapper().readValue(req.getInputStream(), Advert.class);
        } catch (IOException e) {
            throw new CustomServletException(400, "Invalid request body");
        }

        try {
            ServiceFactory.getAdvertService().createAdvert(advert);
            resp.setStatus(201);
            ObjectMapperFactory.getObjectMapper().writeValue(resp.getOutputStream(), advert);
        } catch (RepositoryException | FailedCreateException | IOException e) {
            throw new CustomServletException(500, "Internal server error");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.info("DELETE /adverts");

        resp.setHeader("Content-Type", "application/json");

        // check if "id" request parameter is present
        if (req.getParameter("id") == null) {
            throw new CustomServletException(400, "Missing id parameter");
        }

        Long id = parseIdParam(req);
        try {
            // delete the advert specified by id
            ServiceFactory.getAdvertService().deleteAdvert(id);
            resp.setStatus(200);
            ObjectMapperFactory.getObjectMapper().writeValue(resp.getOutputStream(),
                    new MessageResponse("Advert deleted"));
        } catch (NotFoundException e) {
            throw new CustomServletException(404, "Advert not found");
        } catch (RepositoryException | IOException e) {
            throw new CustomServletException(500, "Internal server error");
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.info("PUT /adverts");

        resp.setHeader("Content-Type", "application/json");

        // check if "id" request parameter is present
        if (req.getParameter("id") == null) {
            throw new CustomServletException(400, "Missing id parameter");
        }

        // try to parse the request body as JSON and create an Advert object from it
        Advert advert;
        try {
            advert = ObjectMapperFactory.getObjectMapper().readValue(req.getInputStream(), Advert.class);
        } catch (IOException e) {
            throw new CustomServletException(400, "Invalid request body");
        }

        Long id = parseIdParam(req);
        try {
            // update the advert specified by id
            ServiceFactory.getAdvertService().updateAdvert(id, advert);
            resp.setStatus(200);
            ObjectMapperFactory.getObjectMapper().writeValue(resp.getOutputStream(),
                    new MessageResponse("Advert updated"));
        } catch (NotFoundException e) {
            throw new CustomServletException(404, "Advert not found");
        } catch (RepositoryException e) {
            throw new CustomServletException(500, "Internal server error");
        }
    }
}
