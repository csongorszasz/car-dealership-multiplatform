package edu.bbte.idde.scim2304.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@WebFilter(urlPatterns = {"/home"})
public class AuthenticationFilter extends HttpFilter {
    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        LOG.info("{} {}", req.getMethod(), req.getRequestURI());

        HttpSession session = req.getSession();
        if (session.getAttribute("username") == null) {
            LOG.warn("Unauthorized access, redirecting to login page");
            var rd = req.getRequestDispatcher("/login");
            rd.forward(req, res);
            return;
        }

        chain.doFilter(req, res);
    }
}
