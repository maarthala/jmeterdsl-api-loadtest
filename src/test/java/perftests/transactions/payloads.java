package perftests.transactions;

import org.json.simple.JSONObject;
import perftests.utils.*;

public class payloads {

    public static String newEntity() {
        String template = "newEntity.json";
        try {
            String content = utils.payload(template);        
            JSONObject obj = utils.jsonstrToMap(content); 
            String id = utils.randString();
            obj.put("name", id);
            return obj.toString(); 
        } catch (java.io.IOException | org.json.simple.parser.ParseException e) {
            throw new RuntimeException("Failed to load payload template: " + template, e);
        }
    }
    
}

