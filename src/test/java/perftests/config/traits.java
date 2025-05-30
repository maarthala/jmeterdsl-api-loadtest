package perftests.config;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.util.UUID;
import org.json.simple.parser.ParseException;
import perftests.utils.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

public class traits   {
    public static Dotenv dotenv = Dotenv.configure().load();
    public static String BASE_URL = dotenv.get("BASE_URL");
    public static String AUTH_URL = dotenv.get("AUTH_URL");
    public static String CLIENT_ID = dotenv.get("CLIENT_ID");
    public static String CLIENT_SECRET = dotenv.get("CLIENT_SECRET");

    public static String SLACK_CHANNEL = dotenv.get("SLACK_CHANNEL");
    public static String SLACK_TOKEN =dotenv.get("SLACK_TOKEN");
    public static int SLACK_NOTIFY  = Integer.parseInt(dotenv.get("SLACK_NOTIFY"));

    public static String TEST_ID =  UUID.randomUUID().toString();
    public static String PROJECT  = dotenv.get("PROJECT");
    public static String RESULT_DIR  = dotenv.get("RESULT_DIR");

    public static String DATA_FILE_INDEX  = dotenv.get("DATA_FILE_INDEX");



    public static String REDIS_HOST  = dotenv.get("REDIS_HOST");
    public static String REDIS_USER  = dotenv.get("REDIS_USER");
    public static String REDIS_PASSWORD  = dotenv.get("REDIS_PASSWORD");

    public static String S3_BUCKET  = dotenv.get("S3_BUCKET");
    public static int S3_UPLOAD  = Integer.parseInt(dotenv.get("S3_UPLOAD"));


    public static int  USERS  = Integer.parseInt(dotenv.get("USERS"));
    public static int RAMPUP_DURATION_IN_SEC = Integer.parseInt(dotenv.get("RAMPUP_DURATION_IN_SEC"));
    public static int TEST_DURATION_IN_SEC = Integer.parseInt(dotenv.get("TEST_DURATION_IN_SEC"));
    
    public static Jedis jedis;

    public static Jedis jedisConnect() {

        try {
            jedis = new Jedis(REDIS_HOST, 6379);
            jedis.auth(REDIS_PASSWORD);
            return jedis;
        } catch (JedisException e) {
            System.out.println("Failed to connect to Redis: " + e.getMessage());
            if (jedis != null) {
                jedis.close();
            }
            return null;
        }
    }
    public static String getToken()  throws IOException {
        try {
            String token = utils.access_token();
            return token;
        } catch (ParseException e) {}
        return "NA";
    }

}
