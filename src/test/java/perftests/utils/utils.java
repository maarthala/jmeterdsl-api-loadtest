package perftests.utils;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import io.github.cdimascio.dotenv.Dotenv;
import java.io.IOException;
import java.lang.System;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.TimeZone;
import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import perftests.config.traits;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedDirectoryUpload;
import software.amazon.awssdk.transfer.s3.model.DirectoryUpload;
import software.amazon.awssdk.transfer.s3.model.UploadDirectoryRequest;

import static us.abstracta.jmeter.javadsl.JmeterDsl.*;

public class utils {

    static String payload_base_path = "./src/test/resources/payloads/";
    static String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    public static Dotenv dotenv = Dotenv.configure().load();



    public static void getEnv() {
        System.out.println(dotenv);
        dotenv.entries().forEach(entry -> {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        });
    }
        
    public static String payload(String  filename)  throws IOException{
        String content = new String(Files.readAllBytes(Paths.get(payload_base_path , filename)));
        return content;
    }

    public static String randString() throws IOException {
        Random random = new Random();
        StringBuilder sb = new StringBuilder(6);
        for (int i = 0; i < 6; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }
        String randomString = sb.toString();
        return randomString;

    }

    public static long randNanoSeconds() {
        Random random = new Random();
        int randInt = random.nextInt(Integer.MAX_VALUE);
        return (long) randInt * 1_000_000L;
    }

    public static long timeNowNano() {
        return System.nanoTime();
    }



public static JSONObject jsonstrToMap(String jsonStr) throws ParseException {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObj = (JSONObject) parser.parse(jsonStr);
            return jsonObj;
        } catch (ParseException e) {
            System.err.println("JSON parse error: " + e.getMessage());
            return null;
        }
    }


    public static void redisIoLPUSH(String keyName, String keyValue) {
        
        try {
            System.out.println("-------------------redis: Push");
            traits.jedis.lpush(keyName, keyValue);
        } catch (Exception e) {
            System.out.println("-------------------redis: Error" +  e);
        }
        
    }

    public static String redisIoRPOP(String keyname) {
        System.out.println("-------------------redis: Pop");
        String keyValue = traits.jedis.rpop(keyname);
        return keyValue;
    }


    public static String redisIoSPOP(String keyName) {
        System.out.println("-------------------redis: spop");
        return traits.jedis.spop(keyName);
    }


    public static String redisIoSRANDMEMBER(String keyName) {
        System.out.println("-------------------redis: srandmember");
        return traits.jedis.srandmember(keyName);
    }


    public static void redisIoSADD(String keyName, String keyValue) {
        try {
            System.out.println("-------------------redis: sadd");
            traits.jedis.sadd(keyName, keyValue);
        } catch (Exception e) {
            System.out.println("-------------------redis: Error" +  e);
        }
    }

    public static long timeNow() {
        Calendar date = Calendar.getInstance();
        long timeInSecs = date.getTimeInMillis();
        return timeInSecs;
    }

    public static Date addSecondsToTime(long timeInSecs , int secondsVar) {
        Date afterAdding = new Date(timeInSecs + (secondsVar * 60 * 1000));
        return afterAdding;

    }

    public static String timeToIsoString(Date dateVar) {
        TimeZone tz = TimeZone.getTimeZone("UTC");
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        df.setTimeZone(tz);
        String nowAsISO = df.format(dateVar);
        return nowAsISO;
    }
    

    public static String readStorageFile() throws ParseException, IOException {
        String sessionStoragePath = "../e2e/";
        String content = new String(Files.readAllBytes(Paths.get(sessionStoragePath , "storageState.json")));
        return content;
    }

    public static String access_token()  throws ParseException, IOException {
        JSONParser parser = new JSONParser();
        try {
            String content = readStorageFile();
            Object jsonObj = parser.parse(content);
            JSONObject jsonObject = (JSONObject) jsonObj;
            JSONArray origins =  (JSONArray)jsonObject.get("origins");
            JSONObject item = (JSONObject) origins.get(0);
            JSONArray localStorage =  (JSONArray)item.get("localStorage");
            Iterator items = localStorage.iterator();
            while (items.hasNext()) {
                String k = items.next().toString();
                Object kv  = parser.parse(k);
                JSONObject kvObj = (JSONObject) kv;
                String name = kvObj.get("name").toString();
                String value = kvObj.get("value").toString();

                value = value.replace("\"","");
                if ((name.toString()).equals("essToken" )) {
                    System.out.println("Token:"+  value);
                    return value;
                }
            }
        } catch (JSONException e) {

        }
        return "NA";
    }

    public static void  slack_notify(String testId)  throws IOException {
        String S3_link = traits.PROJECT + ": https://" + traits.S3_BUCKET +".s3.eu-north-1.amazonaws.com/" + testId  + "/index.html";
        Slack slack = Slack.getInstance();
        MethodsClient methods =  slack.methods(traits.SLACK_TOKEN);
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
            .channel(traits.SLACK_CHANNEL)
            .text(S3_link)
            .build();
        try {
            methods.chatPostMessage(request);
        } catch (SlackApiException e) {

        }
    }

    public static void s3_upload(String testId) throws IOException {
        S3TransferManager tm = S3TransferManager.create();
        String sourcePath = traits.RESULT_DIR + "/";
        String s3BucketPath = traits.S3_BUCKET;
        System.out.println("S3 Bucket:" + s3BucketPath);
        System.out.println("folder path:" + sourcePath);
        DirectoryUpload directoryUpload = tm.uploadDirectory(UploadDirectoryRequest.builder()
            .source(Paths.get(sourcePath))
            .bucket(s3BucketPath)
            .build()
            );
        CompletedDirectoryUpload completedDirectoryUpload = directoryUpload.completionFuture().join();
        completedDirectoryUpload.failedTransfers().forEach(fail ->
            System.out.println("failed to transfer "+ fail.toString()));
        System.out.println("Directory uploaded completed");
    }
}
