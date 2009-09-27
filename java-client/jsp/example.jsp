<%@page import="net.nextdb.client.*"%>
<%@page import="org.json.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>NextDB.net JSP Page</title>
    </head>
    <body>
        <%
        Connection conn = new Connection("nextdb-example","SAMPLEDB");
        // make calls block (if you don't do this the page will render before the call returns)
        conn.setAsync(false);
        Query query = new Query("TEST_TABLE_QUERY");
        BasicCallback callback = new BasicCallback();
        conn.execute(query, callback);
        JSONObject json = callback.getJson();
        // see the JSON (got to jsonlint.com to pretty print and debug)
        %>This is the full JSON response:<p><PRE><%= json.toString().replaceAll("<", "&lt;").replaceAll(">", "&gt;") %></PRE><%
        JSONArray rs =  ((JSONArray) json.get("rs"));
        // loop throug results
        for(int i=0;i<rs.length();i++){%>
            <b>
            <%= ((JSONObject)((JSONObject)rs.get(i)).get("TEST_TABLE")).getString("name") %>
            </b>
            <hr/>
        <%}%>
    </body>
</html>