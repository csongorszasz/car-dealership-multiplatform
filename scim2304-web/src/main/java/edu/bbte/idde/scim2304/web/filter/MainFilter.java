package edu.bbte.idde.scim2304.web.filter;

import edu.bbte.idde.scim2304.web.exception.CustomServletException;
import edu.bbte.idde.scim2304.web.om.MessageResponse;
import edu.bbte.idde.scim2304.web.om.ObjectMapperFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter(urlPatterns = {"/*"})
public class MainFilter extends HttpFilter {
    private static final Logger LOG = LoggerFactory.getLogger(MainFilter.class);

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
        throws IOException, ServletException {
        LOG.info("{} {}", req.getMethod(), req.getRequestURI());

        try {
            chain.doFilter(req, res);
        } catch (CustomServletException e) {
            LOG.error("Custom servlet exception: ", e);
            sendJsonError(res, e.getStatusCode(), e.getMessage());
        }
    }

    private void sendJsonError(HttpServletResponse res, int statusCode, String message) {
        res.setStatus(statusCode);
        res.setHeader("Content-Type", "application/json");
        try {
            ObjectMapperFactory.getObjectMapper().writeValue(res.getOutputStream(), new MessageResponse(message));
        } catch (IOException e) {
            LOG.error("Failed to write JSON response", e);
        }
    }
}
