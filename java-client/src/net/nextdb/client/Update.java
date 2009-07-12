/**
 * NextDB.net
 * @author Brent Hamby
 */
package net.nextdb.client;

import java.util.HashMap;

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

    public HashMap getParams() {
        return params;
    }

    public void setParams(HashMap params) {
        this.params = params;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }

    public String getTableName() {
        return tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
