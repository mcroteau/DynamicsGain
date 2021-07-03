package go.service;

import eco.m1.annotate.Inject;
import eco.m1.annotate.Service;
import eco.m1.data.RequestData;
import go.model.State;
import go.model.Town;
import go.repo.StateRepo;
import go.repo.TownRepo;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Service
public class StateService {

    @Inject
    AuthService authService;

    @Inject
    StateRepo stateRepo;

    @Inject
    TownRepo townRepo;

    public String index(String uri, RequestData reqData) throws Exception{
        String decodedUri = URLDecoder.decode(uri, StandardCharsets.UTF_8.toString());

        State state = stateRepo.get(decodedUri);
        List<Town> towns = townRepo.getList(state.getId());

        Long sum = Long.valueOf(0);
        for(Town town : towns){
            sum += town.getCount();
        }
        String count = NumberFormat.getInstance(Locale.US).format(sum);

        reqData.put("count", count);
        reqData.put("state", state);
        reqData.put("towns", towns);

        return "/pages/state/index.jsp";
    }

    public String list(RequestData reqData) {
        if(!authService.isAuthenticated() &&
                !authService.isAdministrator()){
            return "[redirect]/";
        }

        List<State> states = stateRepo.getList();
        for(State state: states){
            List<Town> towns = townRepo.getList(state.getId());
            state.setTowns(towns);
        }
        reqData.put("states", states);
        return "/pages/state/list.jsp";
    }
}
