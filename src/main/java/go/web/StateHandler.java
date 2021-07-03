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
import go.service.StateService;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Http
public class StateHandler {

    @Inject
    StateService stateService;


    @Get("/admin/states")
    public String list(HttpServletRequest req,
                         HttpServletResponse resp,
                         RequestData reqData){
        return stateService.list(reqData);
    }

    @Get("/states/{{uri}}")
    public String index(HttpServletRequest req,
                        HttpServletResponse resp,
                        RequestData data,
                        @Variable String uri) throws Exception {
        return stateService.index(uri, data);
    }

}
