/**
 * NextDB.net
 * @author Brent Hamby
 */
package net.nextdb.client;

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
