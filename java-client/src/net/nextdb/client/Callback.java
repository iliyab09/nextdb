/**
 * NextDB.net
 * @author Brent Hamby
 */
package net.nextdb.client;

import org.json.JSONObject;
/**
 * interface for handling responses from the server.
 */
public interface Callback {

    public void handleResponse(JSONObject json, Error e);

}
