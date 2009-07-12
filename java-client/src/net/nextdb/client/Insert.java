/**
 * NextDB.net
 * @author Brent Hamby
 */
package net.nextdb.client;

import java.util.HashMap;

public class Insert {
    private String tableName;
    private HashMap params;
    private String relationshipName;
    private String rowId;

    public Insert() {
    }

    public Insert(String tableName) {
        this.tableName = tableName;
    }

    public Insert(String tableName, HashMap params) {
        this.tableName = tableName;
        this.params = params;
    }

    public HashMap getParams() {
        return params;
    }

    public void setParams(HashMap params) {
        this.params = params;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public void setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName;
    }

    public String getRowId() {
        return rowId;
    }

    public void setRowId(String rowId) {
        this.rowId = rowId;
    }
    
}
