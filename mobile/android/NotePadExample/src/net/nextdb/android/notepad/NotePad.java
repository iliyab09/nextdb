package net.nextdb.android.notepad;

import java.util.ArrayList;
import java.util.HashMap;

import net.nextdb.client.Connection;

import org.json.JSONException;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class NotePad extends ListActivity {
    
    private static final int ACTIVITY_CREATE = 0;
    private static final int ACTIVITY_EDIT = 1;
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private ProgressDialog loadingDialog = null;
    private NextDBAdapter nextDB = new NextDBAdapter(new Connection("brenthamby", "DROID_NOTES"));
    private String phoneNumber = "";
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes_list);
        
        if (nextDB.getConn().getAccountName().equals("brenthamby")) {
            Log.w("WARN","*****************************************");
            Log.w("WARN","You are accessing the example database.");
            Log.w("WARN","Be sure to get your own private account.");
            Log.w("WARN","Sign up today at http://www.nextdb.net");
            Log.w("WARN","for a free 30 day trial account");
            Log.w("WARN","*****************************************");
        }
        
        // get phone number as unique id for nextdb table
        Context context = getBaseContext();
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        this.phoneNumber = tm.getLine1Number();
        
        loadingDialog = ProgressDialog.show(this, null, "Fetching Notes", true);
        // NEW
        new FetchAllNotesTask().execute();
    }
    
    private void populateList() {
        loadingDialog.dismiss();
        ArrayList<String> titles = new ArrayList<String>();
        try {
            int length = nextDB.getResultsLength();
            for (int i = 0; i < length; i++) {
                titles.add(nextDB.get(i, "title"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> notes = new ArrayAdapter<String>(this,
                R.layout.notes_row, titles);
        Log.i("NOTES LENGTH", "NOTES LENGTH " + notes.getCount());
        setListAdapter(notes);
        registerForContextMenu(getListView());
    }
    
    public void refreshData() {
        new FetchAllNotesTask().execute();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0, INSERT_ID, 0, R.string.menu_insert);
        return true;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        switch (item.getItemId()) {
        case INSERT_ID:
            createNote();
            return true;
        }
        
        return super.onMenuItemSelected(featureId, item);
    }
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, DELETE_ID, 0, R.string.menu_delete);
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
        switch (item.getItemId()) {
        case DELETE_ID:
            String PK = null;
            try {
                PK = nextDB.get((int) info.id, "PK");
            } catch (JSONException e) {
                Log.e("ERROR", e.getMessage());
                e.printStackTrace();
            }
            if (PK != null) {
                loadingDialog = ProgressDialog.show(this, null, "Deleting",true);
                new DeleteNoteTask(PK).execute();
            }
        }
        return super.onContextItemSelected(item);
    }
    
    private void createNote() {
        Intent i = new Intent(this, NoteEdit.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        
        try {
            Intent i = new Intent(this, NoteEdit.class);
            i.putExtra("PK", nextDB.get(position, "PK"));
            i.putExtra("title", nextDB.get(position, "title"));
            i.putExtra("body", nextDB.get(position, "body"));
            startActivityForResult(i, ACTIVITY_EDIT);
        } catch (JSONException e) {
            Log.e("ERROR", e.getMessage());
            e.printStackTrace();
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Bundle extras = intent.getExtras();
        String title = extras.getString("title");
        String body = extras.getString("body");
        
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("title", title);
        params.put("body", body);
        
        switch (requestCode) {
        case ACTIVITY_CREATE:
            params.put("MSISDN", phoneNumber);
            params.put("timestamp", "now");
            loadingDialog = ProgressDialog.show(this, null, "Inserting", true);
            new CreateNoteTask(params).execute();
            break;
        case ACTIVITY_EDIT:
            String PK = extras.getString("PK");
            if (PK != null) {
                params.put("PK", PK);
                params.put("timestamp", "now");
                loadingDialog = ProgressDialog.show(this, null, "Updating",
                        true);
                new UpdateNoteTask(params).execute();
                break;
            }
        }
    }
    
    // inner AsyncTask classes below
    
    private class DeleteNoteTask extends AsyncTask<Void, Void, String> {
        private String PK;
        
        public DeleteNoteTask(String PK) {
            this.PK = PK;
        }
        // execute synchronous call in background thread.
        protected String doInBackground(Void... unused) {
            Log.i("INFO", "DeleteNoteTask doInBackground");
            String message = null;
            try {
                message = nextDB.deleteNote(PK);
            } catch (JSONException e) {
                Log.e("ERROR", e.getMessage());
                e.printStackTrace();
            }
            return message;
        }
        // report results to UI
        protected void onPostExecute(String message) {
            if (message == null) {
                Log.i("INFO", "null");
            } else {
                Log.i("INFO", message);
                refreshData();
            }
        }
    }
    
    private class UpdateNoteTask extends AsyncTask<Void, Void, String> {
        private HashMap<String, String> params;
        
        public UpdateNoteTask(HashMap<String, String> params) {
            this.params = params;
        }
        
        protected void onPreExecute() {
            Log.i("INFO", "UpdateNoteTask onPre");
        }
        
        // execute synchronous call in background thread.
        protected String doInBackground(Void... unused) {
            Log.i("INFO", "UpdateNoteTask doInBackground");
            String message = null;
            try {
                message = nextDB.updateNote(params, params.get("PK"));
            } catch (JSONException e) {
                Log.e("ERROR", e.getMessage());
                e.printStackTrace();
            }
            return message;
        }
        
        // report results to UI
        protected void onPostExecute(String message) {
            if (message == null) {
                Log.i("INFO", "null");
            } else {
                Log.i("INFO", message);
                refreshData();
            }
        }
    }
    
    private class CreateNoteTask extends AsyncTask<Void, Void, String> {
        private HashMap<String, String> params;
        
        public CreateNoteTask(HashMap<String, String> params) {
            this.params = params;
        }
        
        protected void onPreExecute() {
            Log.i("INFO", "CreateNoteTask onPre");
        }
        
        // execute synchronous call in background thread.
        protected String doInBackground(Void... unused) {
            String message = null;
            try {
                message = nextDB.createNote(params);
            } catch (JSONException e) {
                Log.e("ERROR", e.getMessage());
                e.printStackTrace();
            }
            return message;
        }
        
        // report results to UI
        protected void onPostExecute(String message) {
            if (message == null) {
                Log.i("INFO", "null");
            } else {
                Log.i("INFO", message);
                refreshData();
            }
        }
    }
    
    private class FetchAllNotesTask extends AsyncTask<Void, Void, Void> {        
        // execute synchronous call in background thread.
        protected Void doInBackground(Void... unused) {
            try {
                nextDB.fetchAllNotes(phoneNumber);
            } catch (JSONException e) {
                Log.e("ERROR", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }        
        // report results to UI
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            populateList();
        }
    }
}
