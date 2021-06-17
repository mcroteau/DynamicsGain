package go.service;

import com.redfin.sitemapgenerator.WebSitemapGenerator;
import eco.m1.annotate.Service;
import go.model.Organization;
import go.model.Town;
import okhttp3.HttpUrl;

import javax.servlet.ServletContext;
import java.io.File;
import java.util.List;

@Service
public class SitemapService {

    public static final String BASE = "https://www.dynamicsgain.xyz/";
    public static final String TOWNS_SITEMAP = "/sitemaps/towns/";
    public static final String ORGANIZATIONS_SITEMAP = "/sitemaps/organizations/";

    public boolean writeOrganizations(List<Organization> organizations, ServletContext cntx) throws Exception {
        WebSitemapGenerator wsg = WebSitemapGenerator.builder(BASE, new File(getPath(ORGANIZATIONS_SITEMAP, cntx))).build();

        for (Organization organization: organizations) {
            String url = HttpUrl.parse(BASE)
                    .newBuilder()
                    .addPathSegment("organizations")
                    .addPathSegment(organization.getUri())
                    .build()
                    .toString();
            wsg.addUrl(url);
        }
        wsg.write();
        return true;
    }

    public boolean writeTowns(List<Town> towns, ServletContext cntx) throws Exception {
        WebSitemapGenerator wsg = WebSitemapGenerator.builder(BASE, new File(getPath(TOWNS_SITEMAP, cntx))).build();

        for (Town town: towns) {
            String url = HttpUrl.parse(BASE)
                    .newBuilder()
                    .addPathSegment("towns")
                    .addPathSegment(town.getTownUri())
                    .build()
                    .toString();
            wsg.addUrl(url);
        }
        wsg.write();
        return true;
    }

    private String getPath(String sitemap, ServletContext context){
        try {
            return context.getRealPath(".") + sitemap;
        }catch(Exception ex){ex.printStackTrace();}
        return "";
    }

}
