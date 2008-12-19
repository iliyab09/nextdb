// connection

var conn = new net.nextdb.Connection("accountNameXXX","databaseNameXXX");


// this is a check to make sure that the account has been configured.  Once it is configured you can comment out this block
if(conn.companyName=="accountNameXXX" && conn.schema=="databaseNameXXX"){
	alert("before you can use this system you need to enter your own account info to make a connection to the NextDB.net hosted AJAX Database.\n\nPlease see the README.txt for the project.")
}


// user object

var userObj;

// global

var uploader;

// standard shortcuts
function byId(x){return document.getElementById(x);}

var util=net.nextdb.Util;

function validate(obj){
	for(var x in obj){
		if(obj[x]==""){
			return false;
		}
	}
	return true;
}

function init(){

	// there might be params in the URL to read a certain blog

	var query = window.location.search.substring(1);
	if(query){
		var pos = query.indexOf('=');
		var e = query.substring(0,pos);
		var b = query.substring(pos+1);
		if (e && b) {
			viewBlog(e, b);
			return;
		}
	}

	// other wise check to see if there are cookies and proceed to login

	byId('login').style.display='block';
	byId("navlinks").style.display="none";
	var login = util.readCookie("login");
	var pwd = util.readCookie("pwd");
	byId('emailfield').value=util.readCookie("login") || "";
	byId('passwordfield').value= util.readCookie("pwd") || "";

}

function createAccount(){

	//parse out the forms values to an object
	var user = net.nextdb.Util.formParser(byId("createAccount"));

	//validate that the form is complete
	if(!validate(user)){
		byId("createAccountError").innerHTML="please fill out all form fields";
		setCaptcha();
		return false;

		//validate the passwords match
	}else if(user.password!=user.password2){
		byId("createAccountError").innerHTML="passwords do not match";
		setCaptcha();
		return false;

		//validate the passwords match
	}else if(!util.validateEmail(user.email)){
		byId("createAccountError").innerHTML="invalid email address";
		setCaptcha();
		return false;

	}else{
		clearErrors();
	}
	var insert = new net.nextdb.Insert("USER");
	insert.setParameters(user);
	conn.executeInsert(insert,
		function(rowId,error){
			if(error){
				byId("createAccountError").innerHTML=error.getMessage();
			} else {
				byId('login').style.display='block';
				byId('createAccount').style.display='none';
				byId('emailfield').value=user.email;
				byId('passwordfield').value=user.password;
			}
		}
	);
	return false;
}
function login(){
	//parse out the forms values to an object
	if(!byId("emailfield").value || !byId("passwordfield").value){
		byId("loginError").innerHTML="please enter login and password";
		return;
	}

	var query = new net.nextdb.Query("LOGIN");
	query.setParameters({"email":byId("emailfield").value,"pwd":byId("passwordfield").value});
	conn.executeQuery(query,
		function(user,error){
			if(error){
				byId("loginError").innerHTML=(error.getMessage());
			} else {
				if(user.length==1){
					byId('login').style.display='none';
					byId('createAccount').style.display='none';
					byId('welcome').style.display='block';
					userObj=user[0].USER;
					byId('userinfo').innerHTML=userObj.first_name+" "+
						userObj.last_name+" "+userObj.email;
					clearErrors();
					welcome();
					if(byId("rememberLogin").checked){
						util.createCookie("login",byId("emailfield").value,365);
						util.createCookie("pwd",byId("passwordfield").value,365);
					}
				}else{
					byId("loginError").innerHTML="invalid login";
				}
			}
		}
	);
}

/*
 * blog app code below
 */

// global reference to the YUI editor
var myEditor;

// variables for maintaining state
var bName;
var bKey;
var bEntryKey;

function welcome(){
	byId("navlinks").style.display="block";
	getBlogs();
}

function getBlogs(){
	var query = new net.nextdb.Query("GET_USER_BLOGS");
	var odd='odd';
	query.setPageSize(1000);
	var args={ email : userObj.email , password : userObj.password };
	query.setParameters(args);
	conn.executeQuery(query,
		function(rows,error){

			// building the table as a string to be set as innerHTML

			var results="<table cellspacing='0px' cellpadding='4px'><tr><td colspan='5'>My Blogs</td></tr>";
			for(var i=0;i<rows.length;i++){
				var pub = rows[i].USER_BLOG["public"]==1?'checked':'';
				odd=(odd=='odd')?'even':'odd';
				results+="<tr class='"+odd+"'><td><div class='blogname'>"+rows[i].USER_BLOG.name+"</div></td>"+
					"<td>public<input type='checkbox' class='checkbox' "+pub+" onclick='toggleBlogPublic(this,\""+rows[i].USER_BLOG.PK+"\")'>"+
					"<td><a href='#' onclick='selectBlog(\""+rows[i].USER_BLOG.PK+"\",\""+rows[i].USER_BLOG.name+"\"); getBlogEntries();'>select</a></td>"+
					"<td><a href='#' onclick='selectBlog(\""+rows[i].USER_BLOG.PK+"\",\""+rows[i].USER_BLOG.name+"\"); cleanBlogEditor();'>new entry</a></td>"+
					"<td><a href='#' onclick='deleteBlog(\""+rows[i].USER_BLOG.PK+"\")';>delete</a></td>"+
					"</tr>";
			}
			results+="</table>";
			byId("myblogs").innerHTML=results;
		}
	);
}

function getBlogEntries(){

	var query = new net.nextdb.Query("GET_BLOG_ENTRIES");
	var odd='odd';
	query.setPageSize(1000);

	var args={ email : userObj.email , password : userObj.password , name : bName };

	query.setParameters(args);
	conn.executeQuery(query, function(rows,error){
		var results="<table cellspacing='0px' cellpadding='4px'><tr><td colspan='4'>Blog Entries for '"+bName+"'</td></tr>";
		for(var i=0;i<rows.length;i++){
			odd=(odd=='odd')?'even':'odd';
			var pub = rows[i].BLOG_ENTRY["public"]==1?'checked':'';
			results+="<tr class='"+odd+"'><td>"+rows[i].BLOG_ENTRY.title+"</td>"+
				"<td>public<input type='checkbox' class='checkbox' "+pub+" onclick='toggleBlogEntryPublic(this,\""+rows[i].BLOG_ENTRY.PK+"\")'>"+
				"<td><a href='#' onclick='populateBlogEditor(\""+rows[i].BLOG_ENTRY.title+"\",\""+escape(rows[i].BLOG_ENTRY.body)+"\",\""+rows[i].BLOG_ENTRY.PK+"\")';>update</a></td>"+
				"<td><a href='#' onclick='deleteBlogEntry(\""+rows[i].BLOG_ENTRY.PK+"\")';>delete</a></td>"+
				"</tr>";
		}
		results+="</table></blockquote>";
		byId("myblogentries").innerHTML=results;
		byId("rightPanel").style.display="block";
	}
);
}


function submitBlogEntry(){
	// update or insert
	if (bEntryKey)
		updateBlogEntry();
	else
		createBlogEntry();
	bEntryKey=undefined;
}

function updateBlogEntry(){

	myEditor.saveHTML();
	var update = new net.nextdb.Update("BLOG_ENTRY");
	var args = { title : byId("blogtitle").value , body : byId("blogbody").value };
	update.setParameters(args);
	update.setRowId(bEntryKey);
	conn.executeUpdate(update,
		function(key,error){
			if(error){
				alert(error.message);
			} else {
				getBlogEntries();
				cancelBlogEntry();
			}
		}
	);
}

function createBlogEntry(){
	if (byId("blogtitle").value == "") {
		alert("please enter a title.");
		return;
	}
	myEditor.saveHTML();
	var insert = new net.nextdb.Insert("BLOG_ENTRY");
	var args = { title : byId("blogtitle").value,body : byId("blogbody").value , created : "now" , "public" : "0" }
	insert.setParameters(args);
	insert.setRelationship(bKey,"BLOG_ENTRIES");
	conn.executeInsert(insert,
		function(rowId,error){
			if(error){
				alert(error.message);
			} else {
				getBlogEntries();
				cancelBlogEntry();
			}
		}
	);
}

function selectBlog(key,name){
	bName=name;
	bKey=key;
}

function setEditor(content){
	// initialize the editor
	if (!myEditor) {
		byId("edit").style.display="block";
		myEditor = new YAHOO.widget.SimpleEditor('blogbody', {
			height: '250px',
			width: '700px',
			dompath: true //Turns on the bar at the bottom
		});
		myEditor.render();
		myEditor.on('afterRender', function(){
			if(content){
				byId("blogbody").value=unescape(content);
				myEditor.setEditorHTML(unescape(content));
			}
		}
	);
		return;
	} else {
		if (content) {
			byId("blogbody").value=unescape(content);
			myEditor.setEditorHTML(unescape(content));
		} else {
			byId("blogbody").value="";
			myEditor.setEditorHTML("");
		}
	}
}
function cleanBlogEditor(){
	bEntryKey=undefined;
	byId("welcome").style.display="none";
	byId("edit").style.display="block";
	byId("blogtitle").value="";
	byId("blogbody").value="";
	setEditor();
}
function populateBlogEditor(title,body,key){
	bEntryKey=key;
	byId("welcome").style.display="none";
	byId("edit").style.display="block";
	byId("blogtitle").value=title;
	setEditor(unescape(body));
}
function cancelBlogEntry(){
	byId("welcome").style.display="block";
	byId("edit").style.display="none";
	byId("blogtitle").value="";
	byId("blogbody").value="";
}

function toggleBlogPublic(ele,key){
	var update = new net.nextdb.Update("USER_BLOG");
	var args = {};
	args["public"]=(ele.checked?"1":"0");
	update.setParameters(args);
	update.setRowId(key);
	conn.executeUpdate(update,
		function(key,error){
			if(error){
				alert(error.getMessage());
			}
		}
	);
}
function toggleBlogEntryPublic(ele,key){
	var update = new net.nextdb.Update("BLOG_ENTRY");
	var checked =(ele.checked?"1":"0");
	var args = { "public" : checked } ;
	update.setParameters(args);
	update.setRowId(key);
	conn.executeUpdate(update,
		function(key,error){
			if(error){
				alert(error.getMessage());
			}
		}
	);
}


function createBlog(){
	if (byId("blogname").value == "") {
		alert("please enter a name");
		return;
	}
	var insert = new net.nextdb.Insert("USER_BLOG");
	var args = { name : byId("blogname").value , created : "now" , "public" : "0" };
	insert.setParameters(args);
	insert.setRelationship(userObj.PK,"USER_BLOGS");
	conn.executeInsert(insert,
		function(rowId,error){
			if(error){
				alert(error.message);
			} else {
				getBlogs();
				byId("blogname").value="";
			}
		}
	);
}

function deleteBlog(key){
	var del = new net.nextdb.Delete("USER_BLOG");
	del.setRowId(key);
	conn.executeDelete(del,
	function(error){
			if(error){
				alert(error.getMessage());
			} else {
				getBlogs();
			}
		}
	);
}

function deleteBlogEntry(key){
	var del = new net.nextdb.Delete("BLOG_ENTRY");
	del.setRowId(key);
	conn.executeDelete(del,
	function(error){
			if(error){
				alert(error.getMessage());
			} else {
				getBlogEntries();
			}
		}
	);
}

function getAllBlogs(){
	var query = new net.nextdb.Query("GET_ALL_BLOGS");
	var odd='odd';
	query.setPageSize(1000);
	conn.executeQuery(query, function(rows,error){
			var results="<table cellspacing='0px' cellpadding='4px'><tr><td><b>USER NAME</b></td><td><b>BLOG NAME</b></td><td></td></tr>";
			for(var i=0;i<rows.length;i++){
				odd=(odd=='odd')?'even':'odd';
				results+="<tr class='"+odd+"'><td>"+rows[i].USER.first_name+" "+rows[i].USER.last_name+"</td><td>"+rows[i].USER_BLOG.name+"</td><td><a href='#' onclick='viewBlog(\""+rows[i].USER.email+"\",\""+rows[i].USER_BLOG.name+"\")';>view</a></td></tr>";
			}
			results+="</table>";
			byId("welcome").style.display="none";
			byId("allblogslist").innerHTML=results;
			byId("allblogs").style.display="block";
		}
	);
}

function viewBlog(email,blogName){
	byId("allblogs").style.display="none";
	byId("viewblog").style.display="block";
	byId("welcome").style.display="none";

	var query = new net.nextdb.Query("GET_PUBLIC_BLOG_ENTRIES");
	var args = {"name" : blogName, email : email };
	query.setParameters(args);
	query.setPageSize(1000);
	query.startAfterValue="now";
	conn.executeQuery(query,
		function(rows,error){
			var results="<center>"
			for(var i=0;i<rows.length;i++){
				var created = net.nextdb.Util.convertFromServerDateFormat(rows[i].BLOG_ENTRY.created).toString();
				results+="<div class='viewblog'><h3>"+rows[i].BLOG_ENTRY.title+"</h3>  <div class='date'>"+created+"</div><br/><br/>"+rows[i].BLOG_ENTRY.body+"</div><hr class='viewbloghr'>";
			}
			byId("viewblog").innerHTML=results;
		}
	);
}

function setCaptcha(){
	byId("captchaPic").src="img/ajax-loader-square.gif";
	byId("captchaPic").src = net.nextdb.CONSTANTS.captchaUrl+"?height=80&width=175";
	byId("captchaPic").style.border="1px solid #333333";
}

function clearErrors(){
	byId("loginError").innerHTML="&nbsp;";
	byId("createAccountError").innerHTML="&nbsp;";
}