package edu.bbte.idde.scim2304.web.listener;

import edu.bbte.idde.scim2304.backend.service.ServiceFactory;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener
public class ContextListener implements ServletContextListener {
    private static final Logger LOG = LoggerFactory.getLogger(ContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        LOG.info("WAR deployed");
        ServiceFactory.getAdvertService().onInit();
    }

    @Override
    @SuppressWarnings("PMD.SystemPrintln")
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("WAR is going to be deleted");
        ServiceFactory.getAdvertService().onDestroy();
    }
}
