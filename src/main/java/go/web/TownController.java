package go.web;

import eco.m1.annotate.Http;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Variable;
import eco.m1.annotate.verbs.Get;
import eco.m1.annotate.verbs.Post;
import eco.m1.data.RequestData;
import go.service.TownService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

@Http
public class TownController {

    @Inject
    TownService townService;

    @Get(value="/towns/{{uri}}")
    public String index(HttpServletRequest req,
                        HttpServletResponse resp,
                        RequestData data,
                        @Variable String uri){
        System.out.println("uri " + uri);
        return townService.index(uri, data);
    }

    @Get(value="/admin/towns/create")
    public String index(HttpServletRequest req,
                        HttpServletResponse resp,
                        RequestData data){
        return townService.create(data);
    }

    @Post(value="/admin/towns/save")
    public String save(HttpServletRequest req,
                       HttpServletResponse resp,
                       RequestData data){
        return townService.save(req, data);
    }

    @Get(value="/admin/towns")
    public String getProjects(HttpServletRequest req,
                              HttpServletResponse resp,
                              RequestData data){
        return townService.getTowns(data);
    }

    @Get(value="/admin/towns/edit/{{id}}")
    public String getEdit(HttpServletRequest req,
                          HttpServletResponse resp,
                          RequestData data,
                          @Variable Long id){
        return townService.getEdit(id, data);
    }

    @Post(value="/admin/towns/update/{{id}}")
    public String update(HttpServletRequest req,
                         HttpServletResponse resp,
                         RequestData data,
                         @Variable Long id){
        return townService.update(id, req, data);
    }

    @Post(value="/admin/towns/delete/{{id}}")
    public String delete(HttpServletRequest req,
                         HttpServletResponse resp,
                         RequestData data,
                         @Variable Long id){
        return townService.delete(id, data);
    }

}
