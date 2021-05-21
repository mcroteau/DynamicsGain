package go.service;

import eco.m1.annotate.Inject;
import eco.m1.annotate.Service;
import eco.m1.data.RequestData;
import go.Spirit;
import go.model.Organization;
import go.model.Town;
import go.repo.OrganizationRepo;
import go.repo.TownRepo;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class TownService {

    @Inject
    TownRepo townRepo;

    @Inject
    OrganizationRepo organizationRepo;

    @Inject
    AuthService authService;

//    @Inject
//    SitemapService sitemapService;

    public String index(String uri, RequestData data) {
        Town town = townRepo.get(uri);
        List<Organization> organizations = organizationRepo.getList(town.getId());

        String count = NumberFormat.getInstance(Locale.US).format(town.getCount());

        data.put("count", count);
        data.put("town", town);
        data.put("organizations", organizations);

        return "town/index";
    }

    public String create() {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Spirit.SUPER_ROLE)){
            return "[redirect]/";
        }
        return "town/create";
    }

    public String save(HttpServletRequest req, RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Spirit.SUPER_ROLE)){
            return "[redirect]/";
        }

        String name = req.getParameter("name");

        if(name == null || name.equals("")){
            data.put("message", "Please give your town a name...");
            return "[redirect]/admin/towns/create";
        }

        Long stateId = Long.parseLong(req.getParameter("stateId"));

        Town town = new Town();
        town.setName(name);
        town.setStateId(stateId);

        townRepo.save(town);

        return "[redirect]/admin/towns";
    }

    public String getEdit(Long id, RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Spirit.SUPER_ROLE)){
            return "[redirect]/";
        }

        Town town = townRepo.get(id);
        data.put("town", town);

        return "town/edit";
    }

    public String update(Long id, HttpServletRequest req, RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Spirit.SUPER_ROLE)){
            return "[redirect]/";
        }

        Town town = townRepo.get(id);
        String name = req.getParameter("name");

        if(name == null ||
            name.equals("")){
            data.put("message", "Please give your town a name...");
            return "[redirect]/admin/towns/edit/" + town.getId();
        }

        Long stateId = Long.parseLong(req.getParameter("stateId"));
        Long count = Long.parseLong(req.getParameter("count"));

        town.setName(name);
        town.setStateId(stateId);
        town.setCount(count);

        townRepo.update(town);

        List<Town> towns = townRepo.getList();

//        try {
//            sitemapService.writeTowns(towns);
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }

        data.put("message", "Successfully updated town");
        return "[redirect]/admin/towns/edit/" + town.getId();
    }

    public String getTowns(RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }

        if(!authService.isAdministrator() &&
                !authService.hasRole(Spirit.SUPER_ROLE)){
            return "[redirect]/";
        }

        List<Town> towns = townRepo.getList();
        data.put("towns", towns);

        return "town/list";
    }

    public String delete(Long id, RequestData data) {
        if(!authService.isAuthenticated()){
            return "[redirect]/";
        }
        if(!authService.isAdministrator() &&
                !authService.hasRole(Spirit.SUPER_ROLE)){
            return "[redirect]/";
        }

        organizationRepo.deleteOrganizations(id);
        townRepo.delete(id);
        data.put("message", "Successfully deleted town.");

        return "[redirect]/admin/towns";
    }

}
