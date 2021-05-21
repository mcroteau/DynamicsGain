package go.web;

import com.google.gson.Gson;
import dynamics.gain.model.Location;
import dynamics.gain.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OrganizationController {

    Gson gson = new Gson();

    @Autowired
    LocationService locationService;

    @GetMapping(value="/locations/{uri}")
    public String index(ModelMap modelMap,
                        @PathVariable String uri){
        return locationService.index(uri, modelMap);
    }

    @GetMapping(value="/admin/locations/create")
    public String index(ModelMap modelMap){
        return locationService.create(modelMap);
    }

    @PostMapping(value="/admin/locations/save")
    protected String save(@ModelAttribute("location") Location location,
                          RedirectAttributes redirect){
        return locationService.save(location, redirect);
    }

    @GetMapping(value="/admin/locations")
    public String getProjects(ModelMap modelMap){
        return locationService.getLocations(modelMap);
    }

    @GetMapping(value="/admin/locations/edit/{id}")
    public String getEdit(ModelMap modelMap,
                              @PathVariable Long id){
        return locationService.getEdit(id, modelMap);
    }

    @PostMapping(value="/admin/locations/update")
    protected String update(@ModelAttribute("location") Location location,
                            RedirectAttributes redirect) throws Exception {
        return locationService.update(location, redirect);
    }

    @PostMapping(value="/admin/locations/delete/{id}")
    protected String delete(@PathVariable Long id,
                            RedirectAttributes redirect){
        return locationService.delete(id, redirect);
    }

}
