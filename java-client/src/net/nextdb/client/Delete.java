/**
 * NextDB.net
 * @author Brent Hamby
 */
package net.nextdb.client;

/**
 * Delete data
 */
public class Delete {

    private String tableName;
    private String rowId;

    public Delete() {
    }

    public Delete(String tableName) {
        this.tableName = tableName;
    }

    public Delete(String tableName, String rowId) {
        this.tableName = tableName;
        this.rowId = rowId;
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

}
