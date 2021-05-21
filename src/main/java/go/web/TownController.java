package go.web;

import eco.m1.annotate.Http;
import eco.m1.annotate.Inject;
import go.model.Town;
import go.service.TownService;

@Http
public class TownController {

    @Inject
    TownService townService;

    @GetMapping(value="/towns/{uri}")
    public String index(ModelMap modelMap,
                        @PathVariable String uri){
        return townService.index(uri, modelMap);
    }

    @GetMapping(value="/admin/towns/create")
    public String index(ModelMap modelMap){
        return townService.create(modelMap);
    }

    @PostMapping(value="/admin/towns/save")
    public String save(@ModelAttribute("town") Town town,
                          RedirectAttributes redirect){
        return townService.save(town, redirect);
    }

    @GetMapping(value="/admin/towns")
    public String getProjects(ModelMap modelMap){
        return townService.getTowns(modelMap);
    }

    @GetMapping(value="/admin/towns/edit/{id}")
    public String getEdit(ModelMap modelMap,
                          @PathVariable Long id){
        return townService.getEdit(id, modelMap);
    }

    @PostMapping(value="/admin/towns/update")
    public String update(@ModelAttribute("town") Town town,
                            RedirectAttributes redirect){
        return townService.update(town, redirect);
    }

    @PostMapping(value="/admin/towns/delete/{id}")
    public String delete(@PathVariable Long id,
                            RedirectAttributes redirect){
        return townService.delete(id, redirect);
    }

}
