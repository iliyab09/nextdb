/**
 * NextDB.net
 * @author Brent Hamby
 */
package net.nextdb.client;

public class Connection {

    private String accountName;
    private String databaseName;
    private String host = "http://www.nextdb.net/nextdb/service";

    /**
     * account and database names
     * @param accountName
     * @param databaseName
     */
    public Connection(String accountName, String databaseName) {
        this.accountName = accountName;
        this.databaseName = databaseName;
    }

    /**
     * execute query (spawns a new thread)
     * @param query
     * @param callback
     */
    public void execute(Query query, Callback callback) {
        Async runner = new Async(Util.buildURL(this, query), callback);
        Thread thread = new Thread(runner);
        thread.start();
    }

    /**
     * execute insert (spawns a new thread)
     * @param insert
     * @param callback
     */
    public void execute(Insert insert, Callback callback) {
        Async runner = new Async(Util.buildURL(this, insert), callback);
        Thread thread = new Thread(runner);
        thread.start();
    }

    /**
     * execute update (spawns a new thread)
     * @param update
     * @param callback
     */
    public void execute(Update update, Callback callback) {
        Async runner = new Async(Util.buildURL(this, update), callback);
        Thread thread = new Thread(runner);
        thread.start();
    }

    /**
     * execute delete (spawns a new thread)
     * @param delete
     * @param callback
     */
    public void execute(Delete delete, Callback callback) {
        Async runner = new Async(Util.buildURL(this, delete), callback);
        Thread thread = new Thread(runner);
        thread.start();
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
