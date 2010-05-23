main = {

    init : function(){

        if(main.gup("POSTID")){
            var token = (unescape(main.gup("POSTID")));
            db.init();
            ui.init(token);
        } else {
            db.init();
            ui.init();
        }
//        $("#rides").css("padding-left","5px").html("<center><img src='blog-assets/img/blue.gif' style='padding-top:15px;'></center>");
//        setTimeout(function(){
//            var query = new net.nextdb.Query('GET_SAFE_GROUPS');
//            query.setParameters( {
//                ph : "5104215232"
//            } );
//            query.setPageSize(5)
//            var conn = new net.nextdb.Connection('newdev','TRACK');
//            conn.executeQuery(query, function(rows,e){
//                var html="";
//                for(var i=0;i<rows.length;i++){
//                    html+="<br/><a href='http://brenthamby.com/m/?TOKEN="+rows[i].USER_GROUP.token+"'>"+rows[i].USER_GROUP.name+"</a>";
//                }
//                $("#rides").html(html).slideDown();
//            });
//        },2000);
    },

    gup :function( name ){
        name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
        var regexS = "[\\?&]"+name+"=([^&#]*)";
        var regex = new RegExp( regexS );
        var results = regex.exec( window.location.href );
        if( results == null )
            return false;
        else
            return results[1];
    }

}





