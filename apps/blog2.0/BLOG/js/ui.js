ui = {

    insertLogout : function(){
        $(".nextdb_page_container").append('<a id="nextdb_logout_link" href="JavaScript:void(0)" style="position:absolute;top:3px;right:20px;color:#fff">logout</a>');
        $("#nextdb_logout_link").click(function(){
            net.nextdb.Util.eraseCookie('user');
            window.location='index.html'
            return;
        });
    },

    loading : "<center><img src='blog-assets/img/blue.gif' height='100px' width='100px'></center>",

    init : function(postId){

        // set up UI event handlers
        
        var serialUser = net.nextdb.Util.readCookie("user");
        //alert(serialUser)
        if (serialUser){
            account.user={PK:serialUser}
            //net.nextdb.Util.createCookie("user", serialUser, 1);
            $("#nextdb_login_btn").val("post");
            ui.insertLogout();
            $("#nextdb_login_btn").click(ui.showEditor);
        } else {
            // login button handler
            $("#nextdb_login_btn").click(function(){
                $("#nextdb_login_btn").hide();
                $(".nextdb_page_header").slideDown("slow");
                var form = db.login(function(rows,error){

                    if(error){
                        form.message(error.getMessage());
                    } else if (rows.length===0){
                        form.message("Wrong login / password combo, try again...");
                    } else {
                        net.nextdb.Util.createCookie("login", $("input[name='login']").val(),365);
                        net.nextdb.Util.createCookie("password", $("input[name='password']").val(),365);
                        net.nextdb.Util.createCookie("user", rows[0].USER.PK, 1);
                        account.user = rows[0].USER;
                        $(".nextdb_page_header").slideUp("slow");
                        $("#nextdb_login_panel").remove();
                        
                        $("#nextdb_login_btn").unbind("click");
                        $("#nextdb_login_btn").val("post");
                        $("#nextdb_login_btn").click(ui.showEditor);
                        $("#nextdb_login_btn").fadeIn();
                        ui.insertLogout();
                        ui.showPosts();
                    }

                }, $("#nextdb_login_panel")[0]);

            });

        }

        if(postId){
            //for now
            $("#nextdb_login_btn").remove();
            ui.showPost(postId);
            return;
        } else {
            ui.showPosts();
        }

        // sumbit new post handler
        $("#nextdb_blog_search_btn").click(ui.handleSearchClick);

        $("#nextdb_blog_search_term").keypress(function(e){
                if(e.which==13){
                    $("#nextdb_blog_search_btn").click();
                }
            });
        // sumbit new post handler
        $("#submit_entry_btn").click(ui.handleEntryClick);
        // cancel button handler
        $("#cancel_entry_btn").click(function(){
            $("#nextdb_blog_editor").hide();
            $("#nextdb_blog_view").show();
        });
    },

    handleSearchClick : function(){
        if($("#nextdb_blog_search_term").val())
            ui.showPosts($("#nextdb_blog_search_term").val())
    },

    handleEntryClick : function(){

        editor.instance.saveHTML();

        if($("#blog_title").val()==""  || $("#editor").val()==""){
            $("#blog_editor_messages").html("<i> please enter both title and body </i>");
            return;
        }
        $("#nextdb_blog_editor").hide();
        $("#nextdb_blog_view").show();
        var params = {
            title : $("#blog_title").val(),
            body : $("#editor").val(),
            timestamp : "now",
            id : (Math.random()*100000)
        };
        db.insertPost(params, function(PK, error){
            ui.showPosts();
        });
    },
    handleUpdateClick : function(){
        editor.instance.saveHTML();

        if($("#blog_title").val()==""  || $("#editor").val()==""){
            $("#blog_editor_messages").html("<i> please enter both title and body </i>");
            return;
        }
        $("#nextdb_blog_editor").hide();
        $("#nextdb_blog_view").show();
        var params = {
            title : $("#blog_title").val(),
            body : $("#editor").val()
        };
        db.updatePost(ui.posts[ui.updateIndex].BLOG_ENTRY.PK,params, function(PK, error){
            if(error){
                alert(error);
                return;
            }
            ui.showPosts();
            $("#submit_entry_btn").click(ui.handleEntryClick);
        });
    },

    showEditor : function(i){
        // clear
        $("#blog_title").val("");
        $("#editor").val("");
        $("#blog_editor_messages").html("");
        if(editor.instance)
            editor.instance.setEditorHTML("");

        $("#nextdb_blog_editor").show();

        $("#nextdb_blog_view").hide();

        editor.init();

        // if i then it means update
        if(i && !i.type){
            $("#submit_entry_btn").unbind("click");
            $("#submit_entry_btn").click(ui.handleUpdateClick);
            i=parseInt(i);
            ui.updateIndex=i;
            var title = ui.posts[i].BLOG_ENTRY.title;
            var body = ui.posts[i].BLOG_ENTRY.body;
            $("#blog_title").val(title);
            $("#editor").val(body);
            // fixing strange race condition in YUI editor
            setTimeout(function(){
                editor.instance.setEditorHTML(body);
            },100)
        }

    },

    /*
     * all posts page with comments
     */


    showPosts : function(searchTerm){
        $("#nextdb_blog_entries").html(ui.loading);
        if(account.user){
            if(searchTerm){
                db.getAuthPostsWithSearchTerm(searchTerm,ui.renderPosts);
            }else{
                db.getAuthPosts(ui.renderPosts);
            }
        }else{
            if(searchTerm){
                db.getPostsWithSearchTerm(searchTerm,ui.renderPosts);
            }else{
                db.getPosts(ui.renderPosts);
            }
        }
    },
    countComments : function(id){
        db.countComments(id,function(rows,error){
            $("#count-"+Math.round(id)).empty().html(rows[0].count);
        });
    },
    posts : undefined,
    renderPosts : function(rows,error){
        if(error){
            alert(error.getMessage())
            return;
        }
        $("#nextdb_blog_search_form").show();

        ui.posts=rows;
        $("#nextdb_blog_entries").empty();
        var results="";

        for(var i=0;i<rows.length;i++){
            var created = net.nextdb.Util.convertFromServerDateFormat(rows[i].BLOG_ENTRY.timestamp).toLocaleString();
            results+="<div class='blog_entry'>"
            +"<div class='blog_title'><a href='"+window.location+"?POSTID="+rows[i].BLOG_ENTRY.id+"'>"+rows[i].BLOG_ENTRY.title+"</a>"
            +"<div class='blog_date'>"+created+"</div></div>"
            +"<div class='blog_body'>"+rows[i].BLOG_ENTRY.body+"</div>"
            +"<div class='blog_foot'>";
            if(account.user)
                results+="<span pk='"+rows[i].BLOG_ENTRY.PK+"' class='admin_controls'><a href='javascript:void(0);' class='update_link' onclick='ui.showEditor(\""+i+"\");'>update</a> | <a href='javascript:void(0);' class='delete_link'>delete</a> | </span>"
            results+="<a href='"+window.location+"?POSTID="+rows[i].BLOG_ENTRY.id+"'>read and post comments (<span id='count-"+Math.round(rows[i].BLOG_ENTRY.id)+"'><img border='0' class='small_circle' src='blog-assets/img/small-circle.gif'></span>)</a>"
            +"</div><br><br>"
            +"</div>";
            // adam
            setTimeout("ui.countComments("+rows[i].BLOG_ENTRY.id+")",(i*300));
        }
        $("#nextdb_blog_entries").append(results);

        // enable delete

        $(".delete_link").click(function(){
            var yes = confirm("are you sure you want to delete this post?");
            if(yes){
                db.deletePost($(this).parent().attr("pk"),function(){
                    ui.showPosts();
                });
            }
        });
    },



    /*
     * single post page with comments
     */


    showPost : function(id){
        $("#nextdb_blog_entries").html(ui.loading);
        db.getPost(id, ui.renderPost)
    },
    renderPost : function(rows,error){
        if(error){
            alert(error.getMessage())
            return
        }
        $("#nextdb_blog_search_form").hide();

        $("#nextdb_blog_entries").empty();
        var results="";

        for(var i=0;i<rows.length;i++){
            var created = net.nextdb.Util.convertFromServerDateFormat(rows[i].BLOG_ENTRY.timestamp).toLocaleString();
            results+="<div class='blog_entry'>"
            +"<div class='blog_title'><a href='#'>"+rows[i].BLOG_ENTRY.title+"</a>"
            +"<div class='blog_date'>"+created+"</div></div>"
            +"<div class='blog_body'>"+rows[i].BLOG_ENTRY.body+"</div>"
            +"<div class='blog_comments'><br><b>Comments:</b></div>"
            +"<div class='blog_comment_form'><br><b>leave a comment:</b></div>"
            +"</div>"
        }
        $("#nextdb_blog_entries").append(results);

        // make form

        db.getPostCommentForm(rows[0].BLOG_ENTRY.PK, $(".blog_comment_form")[0],function(key,error){
            dropComment("just now",$("input[name='full_name']").val(),$("textarea[name='comment']").val());
            $(".blog_comment_form").remove();
            
        });

        // get comments

        db.getPostComments(rows[0].BLOG_ENTRY.id,function(rows,error){
            for(var i=0;i<rows.length;i++){
                dropComment(net.nextdb.Util.convertFromServerDateFormat(rows[i].BLOG_ENTRY_COMMENT.timestamp).toLocaleString(),rows[i].BLOG_ENTRY_COMMENT.full_name,rows[i].BLOG_ENTRY_COMMENT.comment)
            }
        });

        // render comment
        
        function dropComment(timestamp,by,comment){
            results="<div class='blog_comment'>"
            +"<div class='blog_date'>"+timestamp+"</div>"
            +"<div>"+by+" said:<br/><br/>"+comment+"</div>"
            +"</div>"
            $(".blog_comments").append(results);
        }
    }
}

