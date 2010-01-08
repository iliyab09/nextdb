/**
 * NextDB.net
 * @author Brent Hamby
 */
package net.nextdb.client;

import android.util.Log;

/**
 * Connection takes Queries, Inserts, Updates and Deletes and runs them either
 * in threaded mode or serial mode.
 */
public class Connection {

    private String accountName;
    private String databaseName;
    private String host = "http://www.nextdb.net/nextdb/service";
    private boolean async = false;

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
     * execute query will spawn a new thread if called in async mode, otherwise the network request will block in the same calling thread.  Use async==true on mobile devices to keep the UI thread from block, use async==false for JSP pages and Servlets.
     * @param query
     * @param callback
     */
    public void execute(Query query, Callback callback) {
        Request request = new Request(Util.buildURL(this, query), callback);
        if (async) {
            Thread thread = new Thread(request);
            thread.start();
        } else {
            request.send();
        }
    }

    /**
     * execute insert will spawn a new thread if called in async mode, otherwise the network request will block in the same calling thread.  Use async==true on mobile devices to keep the UI thread from block, use async==false for JSP pages and Servlets.
     * @param insert
     * @param callback
     */
    public void execute(Insert insert, Callback callback) {
        Request request = new Request(Util.buildURL(this, insert), callback);
        Log.i("INFO",request.toString());
        if (async) {
            Thread thread = new Thread(request);
            thread.start();
        } else {
            request.send();
        }
    }

    /**
     * execute update will spawn a new thread if called in async mode, otherwise the network request will block in the same calling thread.  Use async==true on mobile devices to keep the UI thread from block, use async==false for JSP pages and Servlets.
     * @param update
     * @param callback
     */
    public void execute(Update update, Callback callback) {
        Request request = new Request(Util.buildURL(this, update), callback);
        if (async) {
            Thread thread = new Thread(request);
            thread.start();
        } else {
            request.send();
        }
    }

    /**
     * execute delete will spawn a new thread if called in async mode, otherwise the network request will block in the same calling thread.  Use async==true on mobile devices to keep the UI thread from block, use async==false for JSP pages and Servlets.
     * @param delete
     * @param callback
     */
    public void execute(Delete delete, Callback callback) {
        Request request = new Request(Util.buildURL(this, delete), callback);
        if (async) {
            Thread thread = new Thread(request);
            thread.start();
        } else {
            request.send();
        }
    }

    /**
     * @return the accountName
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * @param accountName the accountName to set
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * @return the databaseName
     */
    public String getDatabaseName() {
        return databaseName;
    }

    /**
     * @param databaseName the databaseName to set
     */
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the async
     */
    public boolean isAsync() {
        return async;
    }

    /**
     * this determines whether the network request is fired in the same thread
     * as a blocking call (good for use with Servlets & JSP pages).  Or whether
     * the network call is made in a separate thread, which is advisable for
     * clients such as clients where you do not want to make network calls in
     * the main UI thread-- J2ME and Android for instance.
     * default value is false
     * @param async the async to set
     */
    public void setAsync(boolean async) {
        this.async = async;
    }
}
