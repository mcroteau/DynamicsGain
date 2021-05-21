package go.service;

import eco.m1.annotate.Inject;
import eco.m1.annotate.Service;
import eco.m1.data.RequestData;
import go.model.Location;
import go.model.Organization;
import go.model.Town;
import go.repo.LocationRepo;
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
        List<Location> locations = locationRepo.getList(town.getId());

        long sum = 0;
        for(Location location: locations){
            sum = sum + location.getCount();
        }
        String count = NumberFormat.getInstance(Locale.US).format(sum);

        modelMap.put("count", count);
        modelMap.put("town", town);
        modelMap.put("locations", locations);

        return "town/index";
    }

    public String create() {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        return "town/create";
    }

    public String save(HttpServletRequest req, RequestData data) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        if(town.getName().equals("")){
            redirect.addFlashAttribute("message", "Please give your web town a name...");
            return "redirect:/admin/towns/create";
        }

        townRepo.save(town);
        return "redirect:/admin/towns";
    }

    public String getEdit(Long id, RequestData data) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator()){
            return "redirect:/unauthorized";
        }

        Town town = townRepo.get(id);
        data.put("town", town);

        return "town/edit";
    }

    public String update(HttpServletRequest req, RequestData data) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator()){
            return "redirect:/";
        }

        if(town.getName().equals("")){
            redirect.addFlashAttribute("message", "Please give your web town a name...");
            return "redirect:/admin/towns/edit/" + town.getId();
        }

        townRepo.update(town);

        List<Town> towns = townRepo.getList();

//        try {
//            sitemapService.writeTowns(towns);
//        }catch(Exception ex){
//            ex.printStackTrace();
//        }

        redirect.addFlashAttribute("message", "Successfully updated town");
        return "redirect:/admin/towns/edit/" + town.getId();
    }

    public String getTowns(RequestData data) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        if(!authService.isAdministrator()){
            return "redirect:/";
        }

        List<Town> towns = townRepo.getList();
        data.put("towns", towns);

        return "town/list";
    }

    public String delete(Long id, RequestData data) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator()){
            return "redirect:/unauthorized";
        }

        organizationRepo.deleteLocations(id);
        townRepo.delete(id);
        data.put("message", "Successfully deleted town.");

        return "redirect:/admin/towns";
    }

}
