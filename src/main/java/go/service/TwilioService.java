package go.service;

import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import com.twilio.rest.api.v2010.account.Message;
import eco.m1.annotate.Prop;
import eco.m1.annotate.Service;

@Service
public class TwilioService {

    @Prop("twilio.sid")
    String sid;

    @Prop("twilio.token")
    String token;

    @Prop("twilio.phone")
    String twilioPhone;

    public boolean send(String phone, String body){

        try {

            Twilio.init(sid, token);
            Message.creator(new PhoneNumber(phone),
                    new PhoneNumber(twilioPhone),
                    body).create();

        }catch(Exception ex){
            ex.printStackTrace();
            return false;
        }

        return true;

    }
}
