<%@ page import="java.lang.StringBuffer" %>
<%@ page import="java.net.HttpURLConnection" %>
<%@ page import="java.net.URL" %>
<%@ page import="java.io.InputStream" %>
<%
String resp="";
if(request.getParameter("_escaped_fragment_")!=null){
    try {
        String url = "http://nextdb.net/nextdb/rest/newdev/NEW_BLOG/BLOG_ENTRY";
        //System.out.println(url);
        StringBuffer sb = new StringBuffer(500);
        URL href = new URL(url);
        HttpURLConnection hc = (HttpURLConnection) href.openConnection();
        String ua="Mozilla/4.0 (compatible; MSIE 6.0; WINDOWS; .NET CLR 1.1.4322)";
        hc.setRequestProperty("user-agent", ua);
        hc.setRequestMethod("GET");
        hc.connect();
        InputStream is = hc.getInputStream();
        int i;
        while ( (i = is.read() ) != -1 ) {
            char c = (char) i;
            sb.append(c);
        }
        is.close();
        hc.disconnect();
        resp =  new String(sb);
    } catch (Exception e) {
        resp = "Error:  " + e.toString() ;
    }
}
%>
<html>
    <head>
        <title>Brent Hamby</title>

        <meta name="fragment" content="!">

        <link rel="stylesheet" type="text/css" href="blog-assets/menu/assets/skins/sam/menu.css" />
        <link rel="stylesheet" type="text/css" href="blog-assets/button/assets/skins/sam/button.css" />
        <link rel="stylesheet" type="text/css" href="blog-assets/fonts/fonts-min.css" />
        <link rel="stylesheet" type="text/css" href="blog-assets/container/assets/skins/sam/container.css" />
        <link rel="stylesheet" type="text/css" href="blog-assets/editor/assets/skins/sam/editor.css" />
        <link rel="stylesheet" type="text/css" href="blog-assets/blog.css" />

    </head>

    <body class="yui-skin-sam">
        <%=resp%>
        <div class="nextdb_page_container">

            <input type="button" id="nextdb_login_btn" value="login">

            <div class="nextdb_page_header">
                <div id="nextdb_login_panel"></div>
            </div>

            <div class="nextdb_page_banner">
                <center>
                    <div id="main_page_title">{ 'brent' : 'hamby' }</div>
                </center>
            </div>

            <center>

                <div class="nextdb_page_body">

                    <div id="nextdb_blog_view" style="display:block">
                        <div id="nextdb_blog_search">
                            <div id="nextdb_blog_search_form">
                                <input type="text" id="nextdb_blog_search_term"><input type="button" id="nextdb_blog_search_btn">
                            </div>
                        </div>
                        <br style="clear:both"/>
                        <div id="nextdb_blog_entries" style="display:block">
                            <center><img alt="loading..." src='blog-assets/img/blue.gif' height='100px' width='100px'/></center>
                        </div>

                        <div id="nextdb_blog_entries_right_panel" style="display:block">
                            <div id="nextdb_about_title">about</div>
                            <div id="nextdb_about_panel">
                                My name is Brent Hamby and this is my personal
                                and often neglected blog where I talk about
                                software technology and other random things.
                                <br/>
                                <br/>
                                This blog was built using the NextDB.net JavaScript
                                API in conjunction with jQuery and YUI tools.
                                This is a pure 100% frontend application.
                                <br/>
                                <br/>
                            </div>

                            <div id="nextdb_about_title">rides</div>
                            <div id="nextdb_about_panel">
                                These are some recent bike rides which I track:
                                <div id="rides"></div>
                                <br/>
                                <br/>
                            </div>


                            <div id="nextdb_about_title">links</div>
                            <div id="nextdb_about_panel">
                                Where's Brent?:
                                <br/>
                                <a href="http://www.brenthamby.com/m">www.brenthamby.com/m/</a>
                                <br/>
                                <br/>
                                The NextDB.net project:
                                <br/>
                                <a href="http://www.nextdb.net">www.nextdb.net</a>
                                <br/>
                                <br/>
                                NextDB.net JavaScript API
                                <br/>
                                (used to build this blog):
                                <br/>
                                <a href="http://www.nextdb.net/docs">www.nextdb.net/docs</a>
                                <br/>
                                <br/>
                                jQuery JavaScript API
                                <br/>
                                (used to build this blog):
                                <br/>
                                <a href="http://www.jquery.com">www.jquery.com</a>
                                <br/>
                                <br/>
                                YUI JavaScript API
                                <br/>
                                (used to build this blog):
                                <br/>
                                <a href="http://developer.yahoo.com/yui/">developer.yahoo.com/yui/</a>
                                <br/>
                                <br/>
                            </div>

                        </div>

                    </div>


                    <div id="nextdb_blog_editor" style="display:none">


                        <div id="nextdb_blog_editor_loading">
                            <center><img alt="loading..." src='blog-assets/img/circle.gif' height='100px' width='100px'/></center>
                        </div>

                        <div id="nextdb_blog_editor_loaded" style="display:none">
                            <form method="post" action="#" id="form1">
                                <div id="blog_title_panel">
                                    Title : <input type="text" id="blog_title"/><span id="blog_editor_messages"></span>
                                </div>
                                <textarea id="editor" name="editor" rows="20" cols="75">
                                </textarea>
                            </form>
                            <input type="button" id="submit_entry_btn" value="insert post">
                            <input type="button" id="cancel_entry_btn" value="cancel post">
                        </div>
                    </div>

                </div>

            </center>

        </div>
        <script type="text/javascript" src="blog-assets/blog.js"></script>
        <script type="text/javascript">
            $(function(){
                main.init();
            });
        </script>
    </body>

</html>
