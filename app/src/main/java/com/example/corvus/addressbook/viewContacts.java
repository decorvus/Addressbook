package com.example.corvus.addressbook;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class viewContacts extends ListActivity {

    public static final String ROW_ID = "row_id"; //intent extra key
    private ListView contactListView;
    private CursorAdapter contactAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactListView = getListView();
        contactListView.setOnItemClickListener(viewContactListener);

        String[] from = new String[] { "name" };
        int[] to = new int[] {  R.id.contactTextView };

        CursorAdapter contactAdapter = new SimpleCursorAdapter(
                viewContacts.this, R.layout.content_view_contacts, null, from, to);
        setListAdapter(contactAdapter);



//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                startActivity(new Intent(viewContacts.this, addContacts.class));
//
//            }
//        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        new GetContactsTask().execute((Object[]) null);
    }
    @Override
    protected void onStop(){
        Cursor cursor  = contactAdapter.getCursor();
        if (cursor != null)
            cursor.deactivate();

        contactAdapter.changeCursor(null);
        super.onStop();
    }

    private class GetContactsTask extends AsyncTask<Object, Object, Cursor>{

        DatabaseConnector databaseConnector = new DatabaseConnector(viewContacts.this);
//
        @Override
        protected Cursor doInBackground(Object... params){
            databaseConnector.open();
            return databaseConnector.getAllContacts();
        }

        @Override
        protected void onPostExecute(Cursor result){
            contactAdapter.changeCursor(result);
            databaseConnector.close();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_view_contacts, menu);
        getMenuInflater().inflate(R.menu.menu_view_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent addNewContact = new Intent(viewContacts.this, addContacts.class);
        startActivity(addNewContact);
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    OnItemClickListener viewContactListener = new OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3){
            Intent openContact = new Intent(viewContacts.this, openContacts.class);
            openContact.putExtra(ROW_ID, arg3);
            startActivity(openContact);
        }
    };


}
