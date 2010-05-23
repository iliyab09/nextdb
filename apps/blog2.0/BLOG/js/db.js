

db = {

    conn : new net.nextdb.Connection('newdev','NEW_BLOG'),
//    conn : new net.nextdb.Connection('adamhuntercaldwell.com','SITE'),
    init : function(){
        var localServer = false;
        if(localServer){
            net.nextdb.CONSTANTS.url="http://localhost:8080/nextdb/service";
            net.nextdb.CONSTANTS.restUrl="http://localhost:8080/nextdb/rest";
            net.nextdb.CONSTANTS.uploadUrl="http://localhost:8080/nextdb/upload";
            net.nextdb.CONSTANTS.captchaUrl="http://localhost:8080/nextdb/captcha";
        }else{
            net.nextdb.CONSTANTS.url="http://nextdb.net/nextdb/service";
            net.nextdb.CONSTANTS.restUrl="http://nextdb.net/nextdb/rest";
            net.nextdb.CONSTANTS.uploadUrl="http://nextdb.net/nextdb/upload";
            net.nextdb.CONSTANTS.captchaUrl="http://nextdb.net/nextdb/captcha";
        }
    },

    getPosts : function(callback){
        var query = new net.nextdb.Query("GET_BLOG_ENTRIES");
        query.setParameters({login:"jman"});
        query.setStartAfterValue("now")
        db.conn.executeQuery(query,callback);
    },
    getPostsWithSearchTerm : function(term,callback){
        var query = new net.nextdb.Query("GET_BLOG_ENTRIES_WITH_SEARCH");
        query.setParameters({login:"jman",searchTerm:term});
        query.setStartAfterValue("now")
        db.conn.executeQuery(query,callback);
    },
    getAuthPostsWithSearchTerm : function(term,callback){
        var query = new net.nextdb.Query("GET_AUTH_BLOG_ENTRIES_WITH_SEARCH");
        query.setParameters({PK:account.user.PK,searchTerm:term});
        query.setStartAfterValue("now")
        db.conn.executeQuery(query,callback);
    },
    getAuthPosts : function(callback){
        var query = new net.nextdb.Query("GET_AUTH_BLOG_ENTRIES");
        query.setParameters({PK:account.user.PK});
        query.setStartAfterValue("now")
        db.conn.executeQuery(query,callback);
    },
    deletePost:function(key,callback){
        var del = new net.nextdb.Delete("BLOG_ENTRY");
        del.setRowId(key);
        db.conn.executeDelete(del,
            function(error){
                if(error){
                    alert(error.toString());
                } else {
                    callback();
                }
            });
    },
    getPost : function(pId,callback){
        var query = new net.nextdb.Query("GET_BLOG_ENTRY");
        query.setParameters({id:pId});
        db.conn.executeQuery(query,callback);
    },
    getPostCommentForm : function(key, div, callback){
        var insertForm = new net.nextdb.html.InsertForm(db.conn,'BLOG_ENTRY_COMMENT');
        insertForm.orderFields(['full_name','email','comment']);
        insertForm.validation({email:net.nextdb.validate.EMAIL});
        insertForm.insertAndLink(key,"BLOG_ENTRY_COMMENTS");
        insertForm.expandFields(['comment']);
        insertForm.afterRenderCallback(function(){
            $("input[value='cancel']").remove();
            $("input").unbind("keypress");
            $("input[name='name']").focus();
            $("input").keypress(function(e){
                if(e.which==13){
                    $("input[value='submit']").click();
                    return false;
                }
            });
            $("input[type='text']").css("border","1px solid #448EC4").css("width","200px").css("padding","8px");
            $("textarea").css("border","1px solid #448EC4").css("width","200px").css("padding","8px");
        });
        insertForm.interceptor(function(p){
            p.timestamp="now";
        });
        insertForm.insertCallback(callback);
        insertForm.render(div);
        return insertForm;
    },
    updatePost : function(PK,params,callback){
        var update = new net.nextdb.Update("BLOG_ENTRY");
        update.setParameters( params );
        update.setRowId(PK);
        db.conn.executeUpdate(update,callback);
    },
    countComments : function(pId,callback){
        var query = new net.nextdb.Query("COUNT_BLOG_ENTRY_COMMENTS");
        query.setParameters({id:pId});
        db.conn.executeQuery(query,callback);

    },
    getPostComments : function(pId,callback){
        var query = new net.nextdb.Query("GET_BLOG_ENTRY_COMMENTS");
        query.setParameters({id:pId});
        db.conn.executeQuery(query,callback);

    },
    login : function(callback, div){
        div.innerHTML="";
        var queryForm = new net.nextdb.html.QueryForm(db.conn,'LOGIN');
        queryForm.orderFields(['login','password']);
        queryForm.obscureFields(["password"]);
        queryForm.afterRenderCallback(function(){
            //$("input[value='cancel']").remove();
            //$("#loading_spinner").remove();
            $("#"+queryForm.errorId).parent().next().append($("#"+queryForm.errorId));
            $("input[name='login']").focus();
            $("input").keypress(function(e){
                if(e.which==13){
                    $("input[value='submit']").click();
                }
            });
            $("input[value='cancel']").click(function(){
                $(".nextdb_page_header").slideUp("slow");
                $("input[value='cancel']").unbind("click");
                $("#nextdb_login_btn").show();
            });
        });
        queryForm.layout(net.nextdb.html.Form.HORIZONTAL_LAYOUT);
        queryForm.defaultValues({login:net.nextdb.Util.readCookie("login"),password:net.nextdb.Util.readCookie("password")});
        queryForm.queryCallback(callback);
        queryForm.render(div);
        return queryForm;
    },
    insertPost : function(params,callback){
        var insert = new net.nextdb.Insert("BLOG_ENTRY");
        insert.setRelationship(new net.nextdb.Relationship("USER_BLOG_ENTRIES",account.user.PK)); 
        insert.setParameters(params)
        db.conn.executeInsert(insert,callback);
    }
}

