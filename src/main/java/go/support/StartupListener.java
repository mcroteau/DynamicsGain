package go.support;

import go.service.StartupService;
import eco.m1.M1;
import eco.m1.Q;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class StartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {

            ServletContext context = event.getServletContext();

            String[] resources = new String[]{ "assets" };
            String[] propertiesFiles = new String[]{ "dev.props" };

            M1 m1 = new M1.Injector()
                    .withPropertyFiles(propertiesFiles)
                    .withWebEnabled(true)
                    .withContext(context)
                    .withWebResources(resources)
                    .withDataEnabled(true)
                    .inject();

            StartupService startupService = (StartupService) m1.getBean("startupservice");
            startupService.init();

            Q.set(m1.getBeans());

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("session ended...");
    }

}