package go.web;

import com.google.gson.Gson;
import eco.m1.annotate.Http;
import eco.m1.annotate.Inject;
import eco.m1.annotate.Json;
import eco.m1.annotate.Variable;
import eco.m1.annotate.verbs.Get;
import eco.m1.annotate.verbs.Post;
import eco.m1.data.RequestData;
import go.service.DonationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Http
public class DonationHandler {

    Gson gson = new Gson();

    @Inject
    DonationService donationService;

    @Get(value="/donate")
    public String index(HttpServletRequest req,
                        HttpServletResponse resp,
                        RequestData data){
        return donationService.index(data);
    }

    @Get(value="/donate/{{id}}")
    public String organization(HttpServletRequest req,
                           HttpServletResponse resp,
                           RequestData data,
                           @Variable Long id){
        return donationService.organization(id, data);
    }

    @Get(value="/donation/cleanup")
    public String cleanup(){
        return donationService.cleanup();
    }

    @Json
    @Post(value="/donation/make")
    public String make(HttpServletRequest req,
                         HttpServletResponse resp,
                         RequestData data){
        return gson.toJson(donationService.make(data, req));
    }

    @Json
    @Post(value="/donation/cancel/{{subscriptionId}}")
    public String cancel(HttpServletRequest req,
                         HttpServletResponse resp,
                         RequestData data,
                         @Variable String subscriptionId){
        return donationService.cancel(subscriptionId);
    }

//    @Json
//    @Post(value="/donation/cancel/{{locationId}}/{{subscriptionId}}")
//    public String cancel(HttpServletRequest req,
//                         HttpServletResponse resp,
//                         RequestData data,
//                         @Variable Long locationId,
//                         @Variable String subscriptionId){
//        return donationService.cancel(subscriptionId);
//    }

    @Get(value="/donation/momentum")
    public String momentum(HttpServletRequest req,
                           HttpServletResponse resp,
                           RequestData data){
        return donationService.momentum(data);
    }

}
