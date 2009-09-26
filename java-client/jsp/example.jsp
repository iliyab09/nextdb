<%@page import="net.nextdb.client.*"%>
<%@page import="org.json.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <title>NextDB.net JSP Page</title>
    </head>
    <body>
        <%
        Connection conn = new Connection("YOURACCOUNT","YOURDATABASE");
        conn.setAsync(false);
        Query query = new Query("YOUR_QUERY");
        BasicCallback callback = new BasicCallback();
        conn.execute(query, callback);
        JSONObject json = callback.getJson();
        JSONArray rs =  ((JSONArray) json.get("rs"));
        for(int i=0;i<rs.length();i++){%>
            <b>
            <%= ((JSONObject)((JSONObject)rs.get(i)).get("SOME_TABLE")).getString("some_column") %>
            </b>
            <hr/>
            <%= ((JSONObject)((JSONObject)rs.get(i)).get("SOME_TABLE")).getString("some_other_column") %>
        <%}%>
    </body>
</html>