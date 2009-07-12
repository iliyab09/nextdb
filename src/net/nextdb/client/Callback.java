/**
 * NextDB.net
 * @author Brent Hamby
 */
package net.nextdb.client;

import org.json.JSONObject;

public interface Callback {

    public void handleResponse(JSONObject json, Error e);

}
