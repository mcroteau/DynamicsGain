package go.support;

import com.google.gson.Gson;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.stream.Collectors;

public class Web {

    public static Object hydrate(HttpServletRequest req, Class clazz){
        Object object =  null;
        try {
            object = clazz.getConstructor().newInstance();
            Field[] fields = clazz.getDeclaredFields();
            for(Field field : fields){

                System.out.println(req.getParameter("amount"));

                String value = req.getParameter(field.getName());
                System.out.println("value:" + value);
                if(value != null &&
                        !value.equals("")){

                    Type type = field.getType();
                    System.out.println("set me " + value + "::" + type.getTypeName());

                    if (type.getTypeName().equals("int") ||
                            type.getTypeName().equals("java.lang.Integer")) {
                        field.set(object, Integer.parseInt(value));
                    }
                    else if (type.getTypeName().equals("double") ||
                            type.getTypeName().equals("java.lang.Double")) {
                        field.set(object, Double.parseDouble(value));
                    }
                    else if (type.getTypeName().equals("float") ||
                            type.getTypeName().equals("java.lang.Float")) {
                        field.set(object, Float.parseFloat(value));
                    }
                    else if (type.getTypeName().equals("long") ||
                            type.getTypeName().equals("java.lang.Long")) {
                        field.set(object, Long.parseLong(value));
                    }
                    else if (type.getTypeName().equals("boolean") ||
                            type.getTypeName().equals("java.lang.Boolean")) {
                        field.set(object, Boolean.getBoolean(value));
                    }
                    else if (type.getTypeName().equals("java.math.BigDecimal")) {
                        field.set(object, new BigDecimal(value));
                    }
                    else if (type.getTypeName().equals("java.lang.String")) {
                        field.set(object, value);
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return object;
    }
}
