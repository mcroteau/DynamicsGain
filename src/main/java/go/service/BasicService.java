package go.service;

import eco.m1.annotate.Inject;
import eco.m1.annotate.Service;
import eco.m1.data.RequestData;
import go.model.Organization;
import go.model.Town;
import go.repo.OrganizationRepo;
import go.repo.TownRepo;

@Service
public class BasicService {

    @Inject
    TownRepo townRepo;

    @Inject
    OrganizationRepo organizationRepo;

    @Inject
    AuthService authService;

    @Inject
    private SmsService smsService;

    @Inject
    private MailService mailService;


    public String index() {
        if(authService.isAuthenticated()){
            long id =  authService.getUser().getId();
            return "[redirect]/user/edit/" + id;
        }
        return "[redirect]/home";
    }

    public String home(ModelMap modelMap){
        List<Location> locations = locationRepo.getList();
        List<Town> towns = townRepo.getList();
        long sum = 0;
        for(Town town: towns){
            if(!town.getCountZero().equals(""))
                sum += town.getCount();
        }
        String count = NumberFormat.getNumberInstance(Locale.US).format(sum);

        modelMap.put("count", count);
        modelMap.put("towns", towns);
        modelMap.put("locations", locations);

        return "home";
    }

    public String towns(ModelMap modelMap){
        List<Town> towns = townRepo.getList();
        long sum = 0;
        for(Town town: towns){
            if(!town.getCountZero().equals(""))
                sum += town.getCount();
        }

        String count = NumberFormat.getInstance(Locale.US).format(sum);

        modelMap.put("count", count);
        modelMap.put("towns", towns);
        return "basic/towns";
    }

    public String organizations(RequestData data){
        List<Town> locations = new ArrayList();
        List<Town> towns = townRepo.getList();

        long count = 0;
        for(Town town: towns){
            List<Organization> townLocations = organizationRepo.getList(town.getId());
            town.setLocations(townLocations);
            locations.add(town);
            count += town.getCount();
        }

        modelMap.put("count", count);
        modelMap.put("locations", locations);
        return "basic/organizations";
    }

}
