package go.web;

import eco.m1.annotate.Http;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Variable;
import eco.m1.annotate.verbs.Get;
import eco.m1.annotate.verbs.Post;
import eco.m1.data.RequestData;
import go.service.OrganizationService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;

@Http
public class OrganizationHandler {

    @Inject
    OrganizationService organizationService;

    @Get(value="/organizations/{{uri}}")
    public String index(HttpServletRequest req,
                        HttpServletResponse resp,
                        RequestData data,
                        @Variable String uri){
        return organizationService.index(uri, data);
    }

    @Get(value="/admin/organizations/create")
    public String index(HttpServletRequest req,
                        HttpServletResponse resp,
                        RequestData data){
        return organizationService.create(data);
    }

    @Post(value="/admin/organizations/save")
    protected String save(HttpServletRequest req,
                          HttpServletResponse resp,
                          RequestData data){
        return organizationService.save(data, req);
    }

    @Get(value="/admin/organizations")
    public String getProjects(HttpServletRequest req,
                              HttpServletResponse resp,
                              RequestData data){
        return organizationService.getOrganizations(data);
    }

    @Get(value="/admin/organizations/edit/{{id}}")
    public String getEdit(HttpServletRequest req,
                          HttpServletResponse resp,
                          RequestData data,
                          @Variable Long id){
        return organizationService.getEdit(id, data);
    }

    @Post(value="/admin/organizations/update/{{id}}")
    protected String update(HttpServletRequest req,
                            HttpServletResponse resp,
                            RequestData data,
                            @Variable Long id) throws Exception {
        return organizationService.update(id, data, req);
    }

    @Post(value="/admin/organizations/delete/{{id}}")
    protected String delete(HttpServletRequest req,
                            HttpServletResponse resp,
                            RequestData data,
                            @Variable Long id){
        return organizationService.delete(id, data);
    }

    @Get("/ownership")
    public String takeOwnership(HttpServletRequest req,
                                HttpServletResponse resp,
                                RequestData data){
        return organizationService.takeOwnership(req, data);
    }

    @Post("/ownership")
    public String ownership(HttpServletRequest req,
                            HttpServletResponse resp,
                            RequestData data){
        return organizationService.ownership(req, data);
    }

    @Get("/admin/ownership/requests")
    public String requests(HttpServletRequest req,
                           HttpServletResponse resp,
                           RequestData data){
        return organizationService.requests(req, data);
    }

    @Post("/admin/ownership/requests/approve/{{id}}")
    public String approveRequest(HttpServletRequest req,
                           HttpServletResponse resp,
                           RequestData data,
                           @Variable Long id){
        return organizationService.approveRequest(id, data);
    }

    @Post("/admin/ownership/requests/activate/{{id}}")
    public String activateAccount(HttpServletRequest req,
                                HttpServletResponse resp,
                                RequestData data,
                                @Variable Long id){
        return organizationService.activateAccount(id, data, req, resp);
    }

}
