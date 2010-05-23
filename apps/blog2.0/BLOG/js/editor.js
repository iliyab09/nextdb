

editor = {

    load : function(){
        var scriptObj = document.createElement("script");
        scriptObj.setAttribute("type", "text/javascript");
        scriptObj.setAttribute("src", "blog-assets/blog-editor.js");
        // append to head
        document.getElementsByTagName("head").item(0).appendChild(scriptObj);

    },
    loadCallback : function(){
        editor.loaded=true;
        editor.init();
    },

    initialized : false,
    loaded : false,

    instance : undefined,

    init : function(){
        
        if(!editor.loaded){
            editor.load();
            return;
        }

        if(editor.initialized){

            return;
        } else {
            editor.initialized=true;
        }

        $("#nextdb_blog_editor_loading").remove();
        $("#nextdb_blog_editor_loaded").show();

        var Dom = YAHOO.util.Dom,
        Event = YAHOO.util.Event;

        var myConfig = {
            height: '400px',
            width: '800px',
            animate: true,
            dompath: true,
            focusAtStart: true
        };

        var state = 'off';
        YAHOO.log('Set state to off..', 'info', 'example');

        YAHOO.log('Create the Editor..', 'info', 'example');
        var myEditor = new YAHOO.widget.Editor('editor', myConfig);
        editor.instance = myEditor;
        myEditor.on('toolbarLoaded', function() {





            /** START
                     *  code config button
                     */
            var codeConfig = {
                type: 'push',
                label: 'Edit HTML Code',
                value: 'editcode'
            };
            //YAHOO.log('Create the (editcode) Button', 'info', 'example');
            this.toolbar.addButtonToGroup(codeConfig, 'insertitem');

            this.toolbar.on('editcodeClick', function() {
                var ta = this.get('element'),
                iframe = this.get('iframe').get('element');

                if (state == 'on') {
                    state = 'off';
                    this.toolbar.set('disabled', false);
                    YAHOO.log('Show the Editor', 'info', 'example');
                    YAHOO.log('Inject the HTML from the textarea into the editor', 'info', 'example');
                    this.setEditorHTML(ta.value);
                    if (!this.browser.ie) {
                        this._setDesignMode('on');
                    }

                    Dom.removeClass(iframe, 'editor-hidden');
                    Dom.addClass(ta, 'editor-hidden');
                    this.show();
                    this._focusWindow();
                } else {
                    state = 'on';
                    YAHOO.log('Show the Code Editor', 'info', 'example');
                    this.cleanHTML();
                    YAHOO.log('Save the Editors HTML', 'info', 'example');
                    Dom.addClass(iframe, 'editor-hidden');
                    Dom.removeClass(ta, 'editor-hidden');
                    this.toolbar.set('disabled', true);
                    this.toolbar.getButtonByValue('editcode').set('disabled', false);
                    this.toolbar.selectButton('editcode');
                    this.dompath.innerHTML = 'Editing HTML Code';
                    this.hide();
                }
                return false;
            }, this, true);

            /** END
                     *  code config button
                     */



            /** START
                     *  pretty print button
                     */
            var prettyConfig = {
                type: 'push',
                label: 'Insert Code Snippet',
                value: 'editcode'
            };
            YAHOO.log('Create the (editcode) Button', 'info', 'example');
            this.toolbar.addButtonToGroup(prettyConfig, 'textstyle');

            this.toolbar.on('prettyClick', function() {
                /*
            var ta = this.get('element'),
                iframe = this.get('iframe').get('element');

            if (state == 'on') {
                state = 'off';
                this.toolbar.set('disabled', false);
                YAHOO.log('Show the Editor', 'info', 'example');
                YAHOO.log('Inject the HTML from the textarea into the editor', 'info', 'example');
                this.setEditorHTML(ta.value);
                if (!this.browser.ie) {
                    this._setDesignMode('on');
                }

                Dom.removeClass(iframe, 'editor-hidden');
                Dom.addClass(ta, 'editor-hidden');
                this.show();
                this._focusWindow();
            } else {
                state = 'on';
                YAHOO.log('Show the Code Editor', 'info', 'example');
                this.cleanHTML();
                YAHOO.log('Save the Editors HTML', 'info', 'example');
                Dom.addClass(iframe, 'editor-hidden');
                Dom.removeClass(ta, 'editor-hidden');
                this.toolbar.set('disabled', true);
                this.toolbar.getButtonByValue('editcode').set('disabled', false);
                this.toolbar.selectButton('editcode');
                this.dompath.innerHTML = 'Editing HTML Code';
                this.hide();
            }*/
                return false;
            }, this, true);

            /** END
                     *  pretty print button
                     */


            /**
                     *  NextDB extensions to the image inserting tool
                     */
            this.toolbar.on('insertimageClick', function() {




                var nextdbContainer = document.getElementById("nextdb-image-loader");
                var nextdbProgress = document.getElementById("nextdb-image-loader-progress");
                nextdbContainer.innerHTML="";
                nextdbProgress.innerHTML="";
                nextdbContainer.style.display="block";
                nextdbProgress.style.display="block";
                var uploader = new net.nextdb.html.FileUpload(db.conn,"BLOG_PIC",
                    function(){
                        //console.log("start")
                        // start loading
                        nextdbProgress.style.display="block";
                    },
                    function(tempURL){
                        //console.log("end")
                        // end loading
                        nextdbProgress.style.display="none";
                        var insert = new net.nextdb.Insert("BLOG_PICS");
                        insert.setPermalinkHandler(function(permURL){
                            document.getElementById("editor_insertimage_url").focus();
                            document.getElementById("editor_insertimage_url").value=(permURL);
                            setTimeout(function(){
                                document.getElementById("editor_insertimage_url").blur();
                            },100);

                            $("#editor_insertimage_width").blur(function(){
                                document.getElementById("editor_insertimage_url").value=(permURL)+"/"+$("#editor_insertimage_width").val();
                            });
                        });

                        insert.map["pic"]=tempURL;
                        db.conn.executeInsert(insert, function(data,error){
                            if(error){
                                alert(error.getMessage());
                            }
                        });
                    }, function(progress){
                        //console.log("progress")
                        // progress loading
                        nextdbContainer.style.display='none';
                        var uploadedBytes = progress.rx;
                        var totalBytes = progress.totalRX;
                        var perct = (Math.round((uploadedBytes/totalBytes)*100))+"%";
                        nextdbProgress.innerHTML="progress : "+perct;
                    }
                    );
                nextdbContainer.appendChild(uploader.getElement());
                return true;
                
            }, this, true);









            this.on('cleanHTML', function(ev) {
                YAHOO.log('cleanHTML callback fired..', 'info', 'example');
                this.get('element').value = ev.html;
            }, this, true);

            this.on('afterRender', function() {
                var wrapper = this.get('editor_wrapper');
                wrapper.appendChild(this.get('element'));
                this.setStyle('width', '100%');
                this.setStyle('height', '100%');
                this.setStyle('visibility', '');
                this.setStyle('top', '');
                this.setStyle('left', '');
                this.setStyle('position', '');
                this.addClass('editor-hidden');
            }, this, true);
        }, myEditor, true);



        myEditor.render();


    }

}

