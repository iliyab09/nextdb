/**
 * NextDB.net
 */
package net.nextdb.android.notepad;

import java.util.HashMap;

import net.nextdb.client.BasicCallback;
import net.nextdb.client.Connection;
import net.nextdb.client.Delete;
import net.nextdb.client.Insert;
import net.nextdb.client.Query;
import net.nextdb.client.Update;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class NextDBAdapter {
    
    private Connection conn;
    
    public NextDBAdapter(Connection conn) {
        this.conn = conn;
    }
    
    /**
     * holds reference to the results
     */
    private JSONArray rs;
    
    /**
     * get a certain value from the result set
     * 
     * @param rowIndex
     * @param columnName
     * @return
     * @throws JSONException
     */
    public String get(int rowIndex, String columnName) throws JSONException {
        return ((JSONObject) ((JSONObject) rs.get(rowIndex)).get("NOTES"))
                .getString(columnName);
    }
    
    public int getResultsLength() {
        return rs.length();
    }
    
    /**
     * create / insert (C)RUD
     * 
     * @param params
     * @return
     * @throws JSONException
     */
    public String createNote(HashMap<String, String> params)
            throws JSONException {
        Log.i("INFO", "createNote");
        Insert insert = new Insert("NOTES");
        insert.setParams(params);
        // generic callback
        BasicCallback callback = new BasicCallback();
        // calling server
        conn.execute(insert, callback);
        JSONObject json = callback.getJson();
        return ((String) json.get("message"));
    }
    
    /**
     * read / query C(R)UD
     * 
     * @param phoneNumber
     * @throws JSONException
     */
    public void fetchAllNotes(String phoneNumber) throws JSONException {
        Log.i("INFO", "fetchAllNotes");
        Query query = new Query("FETCH_ALL_NOTES");
        // set phone number into the query to retrieve this users notes
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("MSISDN", phoneNumber);
        query.setParams(params);
        // generic callback
        BasicCallback callback = new BasicCallback();
        // calling server
        conn.execute(query, callback);
        JSONObject json = callback.getJson();
        this.rs = ((JSONArray) json.get("rs"));
    }
    
    /**
     * update CR(U)D
     * 
     * @param params
     * @param PK
     * @return
     * @throws JSONException
     */
    public String updateNote(HashMap<String, String> params, String PK)
            throws JSONException {
        Update update = new Update("NOTES", PK);
        update.setParams(params);
        BasicCallback callback = new BasicCallback();
        // calling server
        conn.execute(update, callback);
        JSONObject json = callback.getJson();
        return ((String) json.get("message"));
    }
    
    /**
     * delete CRU(D)
     * 
     * @param PK
     * @return
     * @throws JSONException
     */
    public String deleteNote(String PK) throws JSONException {
        Log.i("INFO", "fetchAllNotes");
        Delete delete = new Delete("NOTES", PK);
        // generic callback
        BasicCallback callback = new BasicCallback();
        conn.execute(delete, callback);
        JSONObject json = callback.getJson();
        return ((String) json.get("message"));
    }
    
    public Connection getConn() {
        return conn;
    }
    
    public void setConn(Connection conn) {
        this.conn = conn;
    }
}
