/**
 * NextDB.net
 * @author Brent Hamby
 */
package net.nextdb.client;

import java.util.HashMap;
/**
 * Insert data
 */
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
     * @return the relationshipName
     */
    public String getRelationshipName() {
        return relationshipName;
    }

    /**
     * @param relationshipName the relationshipName to set
     */
    public void setRelationshipName(String relationshipName) {
        this.relationshipName = relationshipName;
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

}
