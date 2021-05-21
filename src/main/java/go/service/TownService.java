package go.service;

import eco.m1.annotate.Inject;
import eco.m1.annotate.Service;
import eco.m1.data.RequestData;
import go.model.Location;
import go.model.Town;
import go.repo.LocationRepo;
import go.repo.OrganizationRepo;
import go.repo.TownRepo;

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

    public String create(ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        return "town/create";
    }

    public String save(Town town, RedirectAttributes redirect) {
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

    public String getEdit(Long id, ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator()){
            return "redirect:/unauthorized";
        }

        Town town = townRepo.get(id);
        modelMap.put("town", town);

        return "town/edit";
    }

    public String update(Town town, RedirectAttributes redirect) {
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
        try {
            sitemapService.writeTowns(towns);
        }catch(Exception ex){
            ex.printStackTrace();
        }

        redirect.addFlashAttribute("message", "Successfully updated town");
        return "redirect:/admin/towns/edit/" + town.getId();
    }

    public String getTowns(ModelMap modelMap) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }

        if(!authService.isAdministrator()){
            return "redirect:/";
        }

        List<Town> towns = townRepo.getList();
        modelMap.put("towns", towns);

        return "town/list";
    }

    public String delete(Long id, RedirectAttributes redirect) {
        if(!authService.isAuthenticated()){
            return "redirect:/";
        }
        if(!authService.isAdministrator()){
            return "redirect:/unauthorized";
        }

        List<Location> locations = locationRepo.getList(id);
        for(Location location: locations){
            dailyRepo.deleteCounts(location.getId());
        }
        locationRepo.deleteLocations(id);
        townRepo.delete(id);
        redirect.addFlashAttribute("message", "Successfully deleted town.");

        return "redirect:/admin/towns";
    }

}
