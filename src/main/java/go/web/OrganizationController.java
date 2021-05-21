package go.web;

import eco.m1.annotate.Http;
import eco.m1.annotate.Inject;
import go.model.Organization;

@Http
public class OrganizationController {

    @Inject
    OrganizationService organizationService;

    @GetMapping(value="/locations/{uri}")
    public String index(ModelMap modelMap,
                        @PathVariable String uri){
        return organizationService.index(uri, modelMap);
    }

    @GetMapping(value="/admin/locations/create")
    public String index(ModelMap modelMap){
        return locationService.create(modelMap);
    }

    @PostMapping(value="/admin/locations/save")
    protected String save(@ModelAttribute("location") Location location,
                          RedirectAttributes redirect){
        return organizationService.save(location, redirect);
    }

    @GetMapping(value="/admin/locations")
    public String getProjects(ModelMap modelMap){
        return locationService.getLocations(modelMap);
    }

    @GetMapping(value="/admin/locations/edit/{id}")
    public String getEdit(ModelMap modelMap,
                              @PathVariable Long id){
        return organizationService.getEdit(id, modelMap);
    }

    @PostMapping(value="/admin/locations/update")
    protected String update(@ModelAttribute("location") Location location,
                            RedirectAttributes redirect) throws Exception {
        return organizationService.update(location, redirect);
    }

    @PostMapping(value="/admin/locations/delete/{id}")
    protected String delete(@PathVariable Long id,
                            RedirectAttributes redirect){
        return organizationService.delete(id, redirect);
    }

}
