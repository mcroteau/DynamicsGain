package go.service;

import com.google.gson.Gson;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Service;
import eco.m1.data.RequestData;
import go.model.Organization;
import go.model.State;
import go.model.Town;
import go.repo.OrganizationRepo;
import go.repo.StateRepo;
import go.repo.TownRepo;

import javax.servlet.http.HttpServletRequest;
import java.text.NumberFormat;
import java.util.*;

@Service
public class BasicService {

    Gson gson = new Gson();

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
            return "[redirect]/users/edit/" + id;
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
                state.setCountZero(NumberFormat.getInstance(Locale.US).format(sum));
            }
            total += sum;
        }

        String count = NumberFormat.getInstance(Locale.US).format(total);

        data.put("count", count);
        data.put("states", states);

        return "/pages/home.jsp";
    }

    public String mapData(RequestData data){
        Map<String, String> map = new HashMap<>();
        List<State> states = stateRepo.getList();

        for(State state : states){
            List<Town> towns = townRepo.getList(state.getId());
            Long sum = new Long(0);
            for(Town town: towns){
                sum += town.getCount();
            }
            String value = sum.toString();
            if(value.length() >= 4){
                if(sum > 0) {
                    value = value.substring(0, value.length() - 3).concat("K");
                }
            }
            if(sum > 0) {
                map.put(state.getAbbreviation(), value);
            }else{
                map.put(state.getAbbreviation(), "*");
            }
        }

        return gson.toJson(map);
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

        return "/pages/basic/towns.jsp";
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
        return "/pages/basic/towns.jsp";
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
        data.put("towns", towns);
        return "/pages/basic/organizations.jsp";
    }

    public String takeOwnership(HttpServletRequest req, RequestData data) {
        if(req.getParameter("id") != null &&
                !req.getParameter("id").equals("")) {
            Long id = Long.parseLong(req.getParameter("id"));
            Organization organization = organizationRepo.get(id);
            data.put("organization", organization);
        }
        List<Organization> organizations = organizationRepo.getList();
        data.put("organizations", organizations);
        return "/pages/basic/ownership.jsp";
    }

    public String ownership(HttpServletRequest req, RequestData data) {


        return "[redirect]/";
    }

}
