package mobilebank.ru.mobilebank;

/**
 * Created by Dron on 08.05.2017.
 */

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Json implements Runnable{

    DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
    private final String baseUrl = new String("http://sample-env.3w4cahnz3j.us-west-2.elasticbeanstalk.com/api");
    public String url = null;
    JSONObject result = null;

    public void run()
    {
        try {
            result = getJson(baseUrl + url);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public JSONObject getJson(String url) throws ParseException{

        //DefaultHttpClient   httpclient = new DefaultHttpClient(new BasicHttpParams());
        HttpPost httppost = new HttpPost(baseUrl + url);
        // Depends on your web service
        httppost.setHeader("Content-type", "application/json");

        InputStream inputStream = null;
        String result = null;
        try {
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            inputStream = entity.getContent();
            // json is UTF-8 by default
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();

            String line = null;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            result = sb.toString();
        } catch (Exception e) {
            // Oops
        }
        finally {
            try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
        }
        JSONParser parser = new JSONParser();
        JSONObject jObject = (JSONObject) parser.parse(result);
        return jObject;

    }

}

