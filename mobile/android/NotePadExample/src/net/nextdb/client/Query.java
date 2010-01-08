/**
 * NextDB.net
 * @author Brent Hamby
 */
package net.nextdb.client;

import java.util.HashMap;

/**
 * Query data
 */
public class Query {

    private String name;
    private HashMap params;
    private String startAfterPK="0";
    private int pageSize=100000;
    private String startAfterValue=null;
    
    public Query() {
    }
    public Query(String name) {
	  this.name = name;
    }
    public Query(String name, HashMap params) {
	  this.name = name;
	  this.params = params;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the params
     */
    public HashMap getParams() {
        return params;
    }

    /**
     * @param params the params to set
     */
    public void setParams(HashMap params) {
        this.params = params;
    }

    /**
     * @return the startAfterPK
     */
    public String getStartAfterPK() {
        return startAfterPK;
    }

    /**
     * @param startAfterPK the startAfterPK to set
     */
    public void setStartAfterPK(String startAfterPK) {
        this.startAfterPK = startAfterPK;
    }

    /**
     * @return the startAfterValue
     */
    public String getStartAfterValue() {
        return startAfterValue;
    }

    /**
     * @param startAfterValue the startAfterValue to set
     */
    public void setStartAfterValue(String startAfterValue) {
        this.startAfterValue = startAfterValue;
    }
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
    

}
