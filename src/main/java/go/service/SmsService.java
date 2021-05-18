package go.service;

import com.plivo.api.Plivo;
import com.plivo.api.models.message.MessageCreateResponse;
import eco.m1.annotate.Prop;
import eco.m1.annotate.Service;

import java.util.Collections;

@Service
public class SmsService {

    @Prop("business")
    String business;

    @Prop("plivo.api.phone")
    String apiPhone;

    @Prop("plivo.notification.phone")
    String notificationPhone;

    @Prop("plivo.api.key")
    String apiKey;

    @Prop("plivo.secret.key")
    String secretKey;

    public boolean validate(String phone){
        try{

            Plivo.init(apiKey, secretKey);
            MessageCreateResponse message = com.plivo.api.models.message.Message.creator(
                    apiPhone, Collections.singletonList("+1" + phone), business +" Setup complete!")
                    .create();

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean send(String phones, String notification){
        try{
            System.out.println("apikeys + " + apiKey + "::::" + secretKey + " p:'" + phones + "' : '" + apiPhone + "'");
            Plivo.init(apiKey, secretKey);
            MessageCreateResponse message = com.plivo.api.models.message.Message.creator(
                    apiPhone.trim(), Collections.singletonList(phones), notification)
                    .create();

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean support(String notification){
        try{
            System.out.println("apikeys + '" + apiKey + "'::::'" + secretKey + "' : '" + apiPhone + "'");

            Plivo.init(apiKey, secretKey);
            MessageCreateResponse message = com.plivo.api.models.message.Message.creator(
                    apiPhone, Collections.singletonList(notificationPhone), notification)
                    .create();

        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}