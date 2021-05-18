package go.service;

import eco.m1.annotate.Prop;
import eco.m1.annotate.Service;

@Service
public class UxService {

    @Prop("business")
    String businessName;

    @Prop("mailbox")
    String businessEmail;

    public String getBusinessName(){
        return this.businessName;
    }

    public String getBusinessEmail(){
        return this.businessEmail;
    }
}
