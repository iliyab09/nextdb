/**
 * NextDB.net
 * @author Brent Hamby
 */
package net.nextdb.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONObject;

public class Async implements Runnable {

    private String request;
    private Callback callback;

    /**
     * request (full GET url) and callback for handling response
     * @param request
     * @param callback
     */
    public Async(String request, Callback callback) {
        this.request = request;
        this.callback = callback;
    }

    private void send() {
        try {
            URL url = new URL(request);
            URLConnection conn = url.openConnection();

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            String json = sb.toString();
            // remove callback wrap
            json = json.substring(9, json.length() - 1);
            JSONObject jsonObj = new JSONObject(json);

            // check for errors
            if(jsonObj.has("ApplicationException")){
                Error error = new Error(((JSONObject)jsonObj.get("ApplicationException")).getString("message"));
                callback.handleResponse(null, error);
                return;
            } else if(jsonObj.has("InternalException")){
                Error error = new Error(((JSONObject)jsonObj.get("InternalException")).getString("message"));
                callback.handleResponse(null, error);
                return;
            } else {
                callback.handleResponse(jsonObj, null);
                return;
            }            
        } catch (Exception e) {
            Error error = new Error(e.getMessage());
            callback.handleResponse(null, error);
            e.printStackTrace();
        } finally {
            
        }
    }
    public void run() {
        send();
    }
}