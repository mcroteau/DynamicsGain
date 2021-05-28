package go.web;

import com.stripe.model.Source;
import eco.m1.annotate.Http;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Json;
import eco.m1.annotate.verbs.Get;
import eco.m1.annotate.verbs.Post;
import eco.m1.data.RequestData;
import go.Spirit;
import go.model.OwnershipRequest;
import go.service.BasicService;
import go.support.Web;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

@Http
public class BasicHandler {

    @Inject
    BasicService basicService;

    @Get(value="/")
    public String index(HttpServletRequest req,
                        HttpServletResponse resp,
                        RequestData data){
        return basicService.index();
    }

    @Get(value="/home")
    public String home(HttpServletRequest req,
                       HttpServletResponse resp,
                       RequestData data){
        return basicService.home(data);
    }

    @Json
    @Get(value="/home/map/data")
    public String mapData(HttpServletRequest req,
                       HttpServletResponse resp,
                       RequestData data){
        return basicService.mapData(data);
    }

    @Get(value="/towns")
    public String towns(HttpServletRequest req,
                        HttpServletResponse resp,
                        RequestData data){
        return basicService.towns(data);
    }

    @Get(value="/organizations")
    public String organizations(HttpServletRequest req,
                                HttpServletResponse resp,
                                RequestData data){
        return basicService.organizations(data);
    }

    @Get(value="/about")
    public String about(HttpServletRequest req,
                        HttpServletResponse resp,
                        RequestData data){
        return "/pages/basic/about.jsp";
    }

    @Get(value="/cost")
    public String cost(HttpServletRequest req,
                       HttpServletResponse resp,
                       RequestData data){ return "/pages/basic/cost.jsp"; }

    @Get(value="/developers")
    public String developers(HttpServletRequest req,
                             HttpServletResponse resp,
                             RequestData data){ return "/pages/basic/developers.jsp"; }

    @Get(value="/privacy")
    public String privacy(HttpServletRequest req,
                          HttpServletResponse resp,
                          RequestData data){
        return "/pages/basic/privacy.jsp";
    }

    @Get("/ownership")
    public String takeOwnership(HttpServletRequest req,
                         HttpServletResponse resp,
                         RequestData data){
        return basicService.takeOwnership(req, data);
    }

    @Post("/ownership")
    public String ownership(HttpServletRequest req,
                         HttpServletResponse resp,
                         RequestData data){
        OwnershipRequest ownershipRequest = (OwnershipRequest) Web.hydrate(req, OwnershipRequest.class);
        if(!Spirit.isValidMailbox(ownershipRequest.getEmail())){
            
        }

        return basicService.ownership(req, data);
    }

}
