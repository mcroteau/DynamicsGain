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

            String[] resources = new String[]{ "assets", "sitemaps" };
            String[] propertiesFiles = new String[]{"app.props"};

            M1 m1 = new M1.Injector()
                    .asEmbedded(true)
                    .withPropertyFiles(propertiesFiles)
                    .withWebEnabled(true)
                    .withContext(context)
                    .withWebResources(resources)
                    .withDataEnabled(true)
                    .inject();

            Q.set(m1.getBeans());

            StartupService startupService = (StartupService) m1.getBean("startupservice");
            startupService.init();


        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("session ended...");
    }

}
