/**
 * NextDB.net
 * @author Brent Hamby
 */
package net.nextdb.client;

import java.util.HashMap;

public class Query {

    private String name;
    private HashMap params;
    private String startAfterPK="0";
    private String startAfterValue="now";
    
    public Query() {
    }
    public Query(String name) {
	  this.name = name;
    }
    public Query(String name, HashMap params) {
	  this.name = name;
	  this.params = params;
    }

    public String getName() {
	  return name;
    }

    public void setName(String name) {
	  this.name = name;
    }

    public String getStartAfterPK() {
	  return startAfterPK;
    }

    public void setStartAfterPK(String startAfterPK) {
	  this.startAfterPK = startAfterPK;
    }

    public String getStartAfterValue() {
	  return startAfterValue;
    }

    public void setStartAfterValue(String startAfterValue) {
	  this.startAfterValue = startAfterValue;
    }

    public HashMap getParams() {
	  return params;
    }

    public void setParams(HashMap params) {
	  this.params = params;
    }
}
