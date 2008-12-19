
Welcome to the open source Ajax Blog Project.

This uses NextDB.net and YUI to build a fully CRUD enabled, secure and serverless blog system.

To use this system you need to get an alpha account from NextDB.net.  It is easy and free to sign up.

With your NextDB.net alpha account you need to follow these steps:

		1. login to the admin pages with you username and password

		2. create a new database by clicking the create database link, you can name it whatever you like.

		3. Next you need to IMPORT the database template for this project, the easiest way is to paste this link into the field on the first page of the admin interface titled: "Import Database via a URL of a shared NextDB data model."

			http://www.nextdb.net/nextdb/service/brenthamby/demo1/EXPORT

		4. Finally you need to add your accountName and the name of the database you created into the first line of code on the demo-blog.js:

			// connection

			var conn = new net.nextdb.Connection("accountXXX","databaseXXX");

		5. At this point you should have an fully function blog system that you can edit and extend.
	
If you make changes to the data model for the blog, please export the data model for others to use.  You can export the datamodel using the export link on the first screen of the admin pages.

The format for sharing your datamodel is:

	http://www.nextdb.net/nextdb/service/YOUR_ACCOUNT_NAME/YOUR_DATABASE_NAME/EXPORT

But remember you need to export it using the admin pages to enable the sharing permission.

Enjoy coding,

NextDB.net Team
