/**
 * NextDB.net
 * @author Brent Hamby
 */
package net.nextdb.client;

import java.util.HashMap;
/**
 * Updating data
 */
public class Update {

    private String tableName;
    private String rowId;
    private HashMap params;

    public Update() {
    }

    public Update(String tableName) {
        this.tableName = tableName;
    }

    public Update(String tableName, String rowId) {
        this.tableName = tableName;
        this.rowId = rowId;
    }

    public Update(String tableName, String rowId, HashMap params) {
        this.tableName = tableName;
        this.rowId = rowId;
        this.params = params;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return the rowId
     */
    public String getRowId() {
        return rowId;
    }

    /**
     * @param rowId the rowId to set
     */
    public void setRowId(String rowId) {
        this.rowId = rowId;
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

}
