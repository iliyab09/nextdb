/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.nextdb.android.notepad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NoteEdit extends Activity {
    
    private EditText mTitleText;
    private EditText mBodyText;
    private String mPK;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_edit);
        
        mTitleText = (EditText) findViewById(R.id.title);
        mBodyText = (EditText) findViewById(R.id.body);
        
        Button confirmButton = (Button) findViewById(R.id.confirm);
        
        mPK = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String title = extras.getString("title");
            String body = extras.getString("body");
            mPK = extras.getString("PK");
            
            if (title != null) {
                mTitleText.setText(title);
            }
            if (body != null) {
                mBodyText.setText(body);
            }
        }
        
        confirmButton.setOnClickListener(new View.OnClickListener() {
            
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                
                bundle.putString("title", mTitleText.getText().toString());
                bundle.putString("body", mBodyText.getText().toString());
                if (mPK != null) {
                    bundle.putString("PK", mPK);
                }
                
                Intent mIntent = new Intent();
                mIntent.putExtras(bundle);
                setResult(RESULT_OK, mIntent);
                finish();
            }
            
        });
    }
}
