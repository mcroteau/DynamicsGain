package go.service;

import eco.m1.annotate.Inject;
import eco.m1.annotate.Service;
import eco.m1.data.RequestData;
import go.model.Organization;
import go.model.State;
import go.model.Town;
import go.repo.OrganizationRepo;
import go.repo.StateRepo;
import go.repo.TownRepo;

import java.text.NumberFormat;
import java.util.*;

@Service
public class BasicService {

    @Inject
    TownRepo townRepo;

    @Inject
    StateRepo stateRepo;

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

    public String home(RequestData data){
        Map<String, String> map = new HashMap<>();
        List<State> states = stateRepo.getList();
        long total = 0;
        for(State state : states){
            List<Town> towns = townRepo.getList(state.getId());
            Long sum = new Long(0);
            for(Town town: towns){
                sum += town.getCount();
            }
            String value = sum.toString();
            if(value.length() >= 4){
                value = value.substring(3, value.length() -1);
            }
            map.put(state.getAbbreviation(), value);
            total += sum;
        }

        String count = NumberFormat.getInstance(Locale.US).format(total);

        data.put("count", count);
        data.put("states", states);
        data.put("map", map);

        return "/pages/home.jsp";
    }

    public String states(RequestData data){
        List<State> states = stateRepo.getList();
        long total = 0;
        for(State state : states){
            List<Town> towns = townRepo.getList(state.getId());
            long sum = 0;
            for(Town town: towns){
                sum += town.getCount();
            }
            state.setCount(sum);
            total += sum;
        }

        String count = NumberFormat.getInstance(Locale.US).format(total);

        data.put("count", count);
        data.put("states", states);

        return "basic/towns";
    }


    public String towns(RequestData data){
        List<Town> towns = townRepo.getList();
        long sum = 0;
        for(Town town: towns){
            if(!town.getCountZero().equals(""))
                sum += town.getCount();
        }

        String count = NumberFormat.getInstance(Locale.US).format(sum);

        data.put("count", count);
        data.put("towns", towns);
        return "basic/towns";
    }

    public String organizations(RequestData data){
        List<Town> organizations = new ArrayList();
        List<Town> towns = townRepo.getList();

        long count = 0;
        for(Town town: towns){
            List<Organization> townOrganizations = organizationRepo.getList(town.getId());
            town.setOrganizations(townOrganizations);
            organizations.add(town);
            count += town.getCount();
        }

        data.put("count", count);
        data.put("organizations", organizations);
        return "basic/organizations";
    }

}
