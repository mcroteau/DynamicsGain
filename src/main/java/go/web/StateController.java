package go.web;

import eco.m1.annotate.Http;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Variable;
import eco.m1.annotate.verbs.Get;
import eco.m1.data.RequestData;
import go.model.Organization;
import go.model.State;
import go.model.Town;
import go.repo.StateRepo;
import go.repo.TownRepo;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Http
public class StateController {

    @Inject
    StateRepo stateRepo;

    @Inject
    TownRepo townRepo;

    @Get(value="/states/{{uri}}")
    public String index(HttpServletRequest req,
                        HttpServletResponse resp,
                        RequestData data,
                        @Variable String uri) throws Exception {

        String decodedUri = URLDecoder.decode(uri, StandardCharsets.UTF_8.toString());

        State state = stateRepo.get(decodedUri);
        List<Town> towns = townRepo.getList(state.getId());

        Long sum = new Long(0);
        for(Town town : towns){
            sum += town.getCount();
        }
        String count = NumberFormat.getInstance(Locale.US).format(sum);

        data.put("count", count);
        data.put("state", state);
        data.put("towns", towns);

        return "/pages/state/index.jsp";
    }

}
