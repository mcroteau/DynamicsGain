package go.support;

import go.Spirit;
import go.service.StartupService;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.scan.StandardJarScanner;

import java.io.File;

public class TomcatServer {


    public static void main(String[] args) throws Exception {

        String baseDir = ".";
        String webappDirLocation = "src/main/webapp/";
        Tomcat tomcat = new Tomcat();

        tomcat.setPort(8080);

        tomcat.setBaseDir("tomcat.8080");
        tomcat.getHost().setAppBase(baseDir);
        tomcat.enableNaming();

        StandardContext ctx = (StandardContext) tomcat.addWebapp("/a", new File(webappDirLocation).getAbsolutePath());
        ctx.setJarScanner(new StandardJarScanner());

        ClassLoader classLoader = Spirit.class.getClassLoader();
        ctx.setParentClassLoader(classLoader);

        File additionWebInfClasses = new File("output/classes");
        WebResourceRoot resources = new StandardRoot(ctx);
        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes",
                additionWebInfClasses.getAbsolutePath(), "/"));
        ctx.setResources(resources);

        File webXml = new File(webappDirLocation + "WEB-INF/web.xml");
        ctx.setDefaultWebXml(webXml.getAbsolutePath());

        tomcat.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                new StartupService().shutdown();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }));

        tomcat.getServer().await();

    }

}
