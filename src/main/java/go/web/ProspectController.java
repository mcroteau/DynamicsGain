package go.web;

import go.service.ProspectService;
import eco.m1.annotate.Http;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Variable;
import eco.m1.annotate.verbs.Get;
import eco.m1.annotate.verbs.Post;
import eco.m1.data.RequestData;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Http
public class ProspectController {

    @Inject
    ProspectService prospectService;

    @Get("/prospects")
    public String searchScreen(HttpServletRequest req,
                            HttpServletResponse resp,
                            RequestData data) throws Exception{
        return prospectService.searchScreen(data, req);
    }

    @Get("/prospects/search")
    public String getProspects(HttpServletRequest req,
                              HttpServletResponse resp,
                              RequestData data){
        return prospectService.getProspects(data, req);
    }

    @Get("/prospects/{{id}}")
    public String index(HttpServletRequest req,
                        HttpServletResponse resp,
                        RequestData data,
                        @Variable Long id){
        return prospectService.index(id, data);
    }

    @Get("/prospects/create")
    public String index(HttpServletRequest req,
                        HttpServletResponse resp,
                        RequestData data){
        return prospectService.create(data);
    }

    @Post("/prospects/save")
    public String save(HttpServletRequest req,
                       HttpServletResponse resp,
                       RequestData data){
        return prospectService.save(data, req);
    }

    @Get("/prospects/edit/{{id}}")
    public String getEdit(HttpServletRequest req,
                          HttpServletResponse resp,
                          RequestData data,
                          @Variable Long id){
        return prospectService.getEdit(id, data);
    }

    @Post("/prospects/update")
    public String update(HttpServletRequest req,
                         HttpServletResponse resp,
                         RequestData data){
        return prospectService.update(data, req);
    }

    @Post("/prospects/delete/{{id}}")
    public String delete(HttpServletRequest req,
                         HttpServletResponse resp,
                         RequestData data,
                         @Variable Long id){
        req.setAttribute("test", "mark");
        return prospectService.delete(id, data);
    }

    @Get("/prospects/history/{{id}}")
    public String history(HttpServletRequest req,
                              HttpServletResponse resp,
                              RequestData data,
                              @Variable Long id){
        return prospectService.history(id, data);
    }

    @Get("/prospects/activity/add/{{id}}")
    public String addActivity(HttpServletRequest req,
                        HttpServletResponse resp,
                        RequestData data,
                        @Variable Long id){
        return prospectService.addActivity(id, data);
    }

    @Post("/prospects/activity/save/{{id}}")
    public String saveActivity(HttpServletRequest req,
                              HttpServletResponse resp,
                              RequestData data,
                              @Variable Long id){
        return prospectService.saveActivity(id, data, req);
    }

    @Get("/prospects/activity/edit/{{id}}")
    public String editActivity(HttpServletRequest req,
                              HttpServletResponse resp,
                              RequestData data,
                              @Variable Long id){
        return prospectService.editActivity(id, data);
    }

    @Post("/prospects/activity/update/{{id}}")
    public String updateActivity(HttpServletRequest req,
                                 HttpServletResponse resp,
                                 RequestData data,
                                 @Variable Long id){
        return prospectService.updateActivity(id, data, req);
    }

    @Post("/prospects/activity/delete/{{id}}")
    public String deleteActivity(HttpServletRequest req,
                                 HttpServletResponse resp,
                                 RequestData data,
                                 @Variable Long id){
        return prospectService.deleteActivity(id, data, req);
    }

    @Post("/prospects/activity/complete/{{id}}")
    public String completeActivity(HttpServletRequest req,
                             HttpServletResponse resp,
                             RequestData data,
                             @Variable Long id){
        return prospectService.completeActivity(id, data, req);
    }

    @Post("/prospects/effort/save/{{id}}")
    public String saveEffort(HttpServletRequest req,
                               HttpServletResponse resp,
                               RequestData data,
                               @Variable Long id){
        return prospectService.saveEffort(id, data, req);
    }

    @Post("/prospects/effort/stop/{{id}}")
    public String stopEffort(HttpServletRequest req,
                             HttpServletResponse resp,
                             RequestData data,
                             @Variable Long id){
        return prospectService.stopEffort(id, data, req);
    }

    @Get("/prospects/notes/edit/{{id}}")
    public String editNotes(HttpServletRequest req,
                               HttpServletResponse resp,
                               RequestData data,
                               @Variable Long id){
        return prospectService.editNotes(id, data);
    }

    @Post("/prospects/notes/update/{{id}}")
    public String updateNotes(HttpServletRequest req,
                             HttpServletResponse resp,
                             RequestData data,
                             @Variable Long id){
        return prospectService.updateNotes(id, data, req);
    }

}
