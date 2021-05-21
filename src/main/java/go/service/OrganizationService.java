package go.service;

import eco.m1.annotate.Inject;
import eco.m1.annotate.Service;
import eco.m1.data.RequestData;
import go.Spirit;
import go.model.Organization;
import go.model.Town;
import go.repo.OrganizationRepo;
import go.repo.TownRepo;
import go.repo.UserRepo;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class OrganizationService {

    @Inject
    UserRepo userRepo;

    @Inject
    TownRepo townRepo;

    @Inject
    OrganizationRepo organizationRepo;

    @Inject
    AuthService authService;

    @Inject
    SmsService smsService;

    @Inject
    MailService mailService;

//    @Inject
//    SitemapService sitemapService;

    public String index(String uri, RequestData data) {
        Organization organization = organizationRepo.get(uri);
        data.put("organization", organization);
        return "organization/index";
    }

    public String create(RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Spirit.SUPER_ROLE)){
            return "[redirect]/";
        }
        List<Town> towns = townRepo.getList();
        data.put("towns", towns);
        return "organization/create";
    }

    public String getOrganizations(RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Spirit.SUPER_ROLE)){
            return "[redirect]/";
        }

        List<Organization> organizations = organizationRepo.getList();
        data.put("organizations", organizations);

        return "organization/list";
    }

    public String save(RequestData data, HttpServletRequest req) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Spirit.SUPER_ROLE)){
            return "[redirect]/";
        }

        String name = req.getParameter("name");
        Long townId = Long.parseLong(req.getParameter("townId"));
        String uri = req.getParameter("uri");

        if(name == null ||
                name.equals("")){
            data.put("message", "Please give your town a name...");
            return "[redirect]/admin/towns/create";
        }

        Organization organization = new Organization();
        organization.setName(name);
        organization.setTownId(townId);
        organization.setUri(uri);

        Organization savedOrganization = organizationRepo.save(organization);

        return "[redirect]/admin/organizations/edit/" + savedOrganization.getId();
    }

    public String getEdit(Long id, RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Spirit.SUPER_ROLE)){
            return "[redirect]/";
        }

        List<Town> towns = townRepo.getList();
        Organization organization = organizationRepo.get(id);

        data.put("towns", towns);
        data.put("organization", organization);

        return "organization/edit";
    }

    public String update(Long id, RequestData data, HttpServletRequest req) throws Exception {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Spirit.SUPER_ROLE)){
            return "[redirect]/";
        }

        Organization organization = organizationRepo.get(id);

        String name = req.getParameter("name");
        Long townId = Long.parseLong(req.getParameter("townId"));
        String description = req.getParameter("description");
        String uri = req.getParameter("uri");
        String latitude = req.getParameter("latitude");
        String longitude = req.getParameter("longitude");

        if(name == null || name.equals("")){
            data.put("message", "Please give your organization a name...");
            return "[redirect]/admin/organizations/edit/" + id;
        }

        organization.setName(name);
        organization.setTownId(townId);
        organization.setDescription(description);
        organization.setUri(uri);
        organization.setLatitude(latitude);
        organization.setLongitude(longitude);

//        List<Organization> organizations = organizationRepo.getList();
//        try {
//            sitemapService.writeOrganizations(organizations);
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }

        organizationRepo.update(organization);

        data.put("message", "Successfully updated organization");
        return "[redirect]/admin/organizations/edit/" + id;
    }

    public String delete(Long id, RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Spirit.SUPER_ROLE)){
            return "[redirect]/";
        }

        organizationRepo.delete(id);
        data.put("message", "Successfully deleted organization.");

        return "[redirect]/admin/organizations";
    }

}
