/**
 * NextDB.net
 * @author Brent Hamby
 */
package net.nextdb.client;

import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Examples {

    private static Connection conn = new Connection("nextdb-example","SAMPLEDB");

    /**
     * simple insert example that calls query example in callback
     */
    public void insert(){
        System.out.println("***************************************");
        System.out.println("INSERT:");
	  Insert insert = new Insert("TEST_TABLE");
	  HashMap params = new HashMap();
	  params.put("name", "Jim Kirk");
	  params.put("rank", "Captian");
	  params.put("serial_number", "2839423487");
	  insert.setParams(params);
	  conn.execute(insert,(new Callback(){
		public void handleResponse(JSONObject json, Error e) {
		    if(e!=null){
			  System.out.println(e.getMessage());
		    }else{
                    try{
                        System.out.println(json.toString(4));
                        // call to run query
                        queryAfterInsert();
                    }catch(JSONException err){
                        err.printStackTrace();
                    }
                }
            }
        }));        
    }


    /**
     * simple query example that calls update example in callback
     */
    public void queryAfterInsert(){
        System.out.println("***************************************");
        System.out.println("QUERY:");
        Query query = new Query("TEST_TABLE_QUERY");
        conn.execute(query, (new Callback() {
            public void handleResponse(JSONObject json, Error e) {
                if (e != null) {
                    System.out.println(e.getMessage());
                } else {
                    try {
                        System.out.println(json.toString(4));
                        
                        // get first row
                        String rowId = ((JSONObject) ((JSONObject) ((JSONArray) json.get("rs")).get(0)).get("TEST_TABLE")).get("PK").toString();

                        // update first row
                        update(rowId);
                    } catch (JSONException err) {
                        err.printStackTrace();
                    }
                }
            }
        }));
    }

    /**
     * simple update example that calls query example
     */
    public void update(String rowId){
        System.out.println("***************************************");
        System.out.println("UPDATE:");
        Update update = new Update("TEST_TABLE",rowId);
	  HashMap params = new HashMap();
	  params.put("name", "James T. Kirk");
	  params.put("rank", "Admiral");
	  params.put("serial_number", "23847892374");
	  update.setParams(params);
        conn.execute(update, (new Callback(){
            public void handleResponse(JSONObject json, Error e) {
                if(e!=null){
                    System.out.println(e.getMessage());
                }else{
                    try{
                        System.out.println(json.toString(4));
                        queryAfterUpdate();
                    }catch(JSONException err){
                        err.printStackTrace();
                    }
                }
            }
        }));

    }

    /**
     * simple query example that calls delete example in callback
     */
    public void queryAfterUpdate(){
        System.out.println("***************************************");
        System.out.println("QUERY:");
        Query query = new Query("TEST_TABLE_QUERY");
        conn.execute(query, (new Callback() {
            public void handleResponse(JSONObject json, Error e) {
                if (e != null) {
                    System.out.println(e.getMessage());
                } else {
                    try {
                        System.out.println(json.toString(4));

                        // get first row
                        String rowId = ((JSONObject) ((JSONObject) ((JSONArray) json.get("rs")).get(0)).get("TEST_TABLE")).get("PK").toString();

                        // update first row
                        delete(rowId);
                        
                    } catch (JSONException err) {
                        err.printStackTrace();
                    }
                }
            }
        }));
    }

    /**
     * simple delete example
     */
    public void delete(String rowId){
        System.out.println("***************************************");
        System.out.println("DELETE:");
        Delete delete = new Delete("TEST_TABLE",rowId);
        conn.execute(delete, (new Callback(){
            public void handleResponse(JSONObject json, Error e) {
                if(e!=null){
                    System.out.println(e.getMessage());
                }else{
                    try{
                        System.out.println(json.toString(4));
                    }catch(JSONException err){
                        err.printStackTrace();
                    }
                }
            }
        }));

    }

    public static void main(String args[]){
        Examples examples = new Examples();
        examples.insert();
    }
}
