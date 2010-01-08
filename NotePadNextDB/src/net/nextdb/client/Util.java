/**
 * NextDB.net
 * @author Brent Hamby
 */
package net.nextdb.client;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This class is used to build out the URL for the various NextDB services.
 * @see Query
 * @see Insert
 * @see Update
 * @see Delete
 */
public class Util {

    /**
     * internally used for generating start of URL
     * @param conn
     * @param service
     * @return url
     */
    private static StringBuffer baseUrl(Connection conn, String service) {
        StringBuffer url = new StringBuffer(conn.getHost());
        url.append("/");
        url.append(conn.getAccountName());
        url.append("/");
        url.append(conn.getDatabaseName());
        url.append("/");
        url.append(service + "?callback=response&");
        return url;
    }

    /**
     * Query url builder
     * @param conn
     * @param query
     * @return url
     */
    public static String buildURL(Connection conn, Query query) {
        StringBuffer url = baseUrl(conn, "EXECUTE_QUERY");
        url.append("name=" + query.getName() + "&");
        url.append("startAfterPK=" + query.getStartAfterPK() + "&");
        url.append("pageSize=" + query.getPageSize() + "&");
        if(query.getStartAfterValue()!=null)
        	url.append("startAfterValue=" + query.getStartAfterValue() + "&");

        if (query.getParams() != null) {
            Set set = query.getParams().entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                String name = (String) me.getKey();
                String value = (String) me.getValue();
                url.append(java.net.URLEncoder.encode("${" + name + "}") +
                        "=" + java.net.URLEncoder.encode(value) + "&");
            }
        }
        url.append("id=23");
        System.out.println(url.toString());
        return url.toString();
    }

    /**
     * Insert url builder
     * @param conn
     * @param insert
     * @return url
     */
    public static String buildURL(Connection conn, Insert insert) {
        StringBuffer url = baseUrl(conn, "INSERT");
        url.append("_table=" + insert.getTableName() + "&");
        if (insert.getRelationshipName() != null && insert.getRowId() != null) {
            url.append("_relationshipName=" + insert.getRelationshipName() + "&");
            url.append("_FK=" + java.net.URLEncoder.encode(insert.getRowId()) + "&");
        }
        if (insert.getParams() != null) {
            Set set = insert.getParams().entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                String name = (String) me.getKey();
                String value = (String) me.getValue();
                url.append(name + "=" + java.net.URLEncoder.encode(value) + "&");
            }
        }
        url.append("id=23");
        return url.toString();
    }

    /**
     * Update url builder
     * @param conn
     * @param update
     * @return url
     */
    public static String buildURL(Connection conn, Update update) {
        StringBuffer url = baseUrl(conn, "UPDATE");
        url.append("_table=" + update.getTableName() + "&");
        url.append("_PK=" + java.net.URLEncoder.encode(update.getRowId()) + "&");
        if (update.getParams() != null) {
            Set set = update.getParams().entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                String name = (String) me.getKey();
                String value = (String) me.getValue();
                url.append(name + "=" + java.net.URLEncoder.encode(value) + "&");
            }
        }
        url.append("_id=23");
        System.out.println(url.toString());
        return url.toString();
    }

    /**
     * Delete url builder
     * @param conn
     * @param delete
     * @return url
     */
    public static String buildURL(Connection conn, Delete delete) {
        StringBuffer url = baseUrl(conn, "DELETE");
        url.append("table=" + delete.getTableName() + "&");
        url.append("PK=" + java.net.URLEncoder.encode(delete.getRowId()) + "&");
        url.append("id=23");
        System.out.println(url.toString());
        return url.toString();
    }

    /**
     * not being used (for j2me)
     * @param sUrl
     * @return url
     */
    public static String urlEncode(String sUrl) {
        int i = 0;
        String urlOK = "";
        while (i < sUrl.length()) {
            if (sUrl.charAt(i) == '<') {
                urlOK = urlOK + "%3C";
            } else if (sUrl.charAt(i) == '/') {
                urlOK = urlOK + "%2F";
            } else if (sUrl.charAt(i) == '>') {
                urlOK = urlOK + "%3E";
            } else if (sUrl.charAt(i) == ' ') {
                urlOK = urlOK + "%20";
            } else if (sUrl.charAt(i) == ':') {
                urlOK = urlOK + "%3A";
            } else if (sUrl.charAt(i) == '-') {
                urlOK = urlOK + "%2D";
            } else {
                urlOK = urlOK + sUrl.charAt(i);
            }
            i++;
        }
        return (urlOK);
    }
}
