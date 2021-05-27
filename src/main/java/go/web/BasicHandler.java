package go.web;

import eco.m1.annotate.Http;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Json;
import eco.m1.annotate.verbs.Get;
import eco.m1.annotate.verbs.Post;
import eco.m1.data.RequestData;
import go.service.BasicService;
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

    @Get("/claim")
    public String claimView(HttpServletRequest req,
                         HttpServletResponse resp,
                         RequestData data){
        return basicService.claimView(req, data);
    }

    @Post("/claim")
    public String claim(HttpServletRequest req,
                         HttpServletResponse resp,
                         RequestData data){
        return basicService.claim(req, data);
    }

    @Post("/claim/note")
    public String claimNote(HttpServletRequest req,
                         HttpServletResponse resp,
                         RequestData data){
        return basicService.claimNote(req, data);
    }
}
