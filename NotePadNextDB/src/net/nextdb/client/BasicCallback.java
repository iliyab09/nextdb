/**
 * NextDB.net
 * @author Brent Hamby
 */
package net.nextdb.client;

import org.json.JSONObject;
/**
 * Basic implementation for Callback, but flexible enough for most common uses.
 */
public class BasicCallback implements Callback{

    private JSONObject json;
    private Error error;

    public void handleResponse(JSONObject json, Error error){
        this.setJson(json);
        this.setError(error);
    }

    /**
     * @return the json
     */
    public JSONObject getJson() {
        return json;
    }

    /**
     * @param json the json to set
     */
    public void setJson(JSONObject json) {
        this.json = json;
    }

    /**
     * @return the error
     */
    public Error getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(Error error) {
        this.error = error;
    }
    
}
