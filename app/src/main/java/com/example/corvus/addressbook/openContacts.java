package com.example.corvus.addressbook;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class openContacts extends AppCompatActivity {
    private long rowID;
    private TextView nameTextView;
    private TextView phoneTextView;
    private TextView emailTextView;
    private TextView streetTextView;
    private TextView cityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_contacts);

        nameTextView = (TextView) findViewById(R.id.nameTextView);
        phoneTextView = (TextView) findViewById(R.id.phoneTextView);
        emailTextView = (TextView) findViewById(R.id.emailTextView);
        streetTextView = (TextView) findViewById(R.id.streetTextView);
        cityTextView = (TextView) findViewById(R.id.cityTextView);

        Bundle extras = getIntent().getExtras();
        rowID = extras.getLong("row_id");
        Button edit = (Button) findViewById(R.id.button3);
        Button delete = (Button) findViewById(R.id.button2);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addContact = new Intent(openContacts.this, addContacts.class);
                addContact.putExtra("row_id", rowID);
                addContact.putExtra("name", nameTextView.getText());
                addContact.putExtra("phone", phoneTextView.getText());
                addContact.putExtra("email", emailTextView.getText());
                addContact.putExtra("street", streetTextView.getText());
                addContact.putExtra("city", cityTextView.getText());
                startActivity(addContact);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact();
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        new LoadContactTask().execute(rowID);
    }

    private class LoadContactTask extends AsyncTask<Long, Object, Cursor>{
        DatabaseConnector databaseConnector = new DatabaseConnector(openContacts.this);

        @Override
        protected Cursor doInBackground(Long... params){
            databaseConnector.open();
            return databaseConnector.getOneContact(params[0]);
        }
        @Override
        protected void onPostExecute(Cursor result){
            super.onPostExecute(result);
            result.moveToFirst();
            int nameIndex = result.getColumnIndex("name");
            int phoneIndex = result.getColumnIndex("phone");
            int emailIndex = result.getColumnIndex("email");
            int streetIndex = result.getColumnIndex("street");
            int cityIndex = result.getColumnIndex("city");

            nameTextView.setText(result.getString(nameIndex));
            phoneTextView.setText(result.getString(phoneIndex));
            emailTextView.setText(result.getString(emailIndex));
            streetTextView.setText(result.getString(streetIndex));
            cityTextView.setText(result.getString(cityIndex));

            result.close();
            databaseConnector.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view_contacts, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){


        switch (item.getItemId()){
            case R.id.editItem:
                Intent addContact = new Intent(this, addContacts.class);
                addContact.putExtra("row_id", rowID);
                addContact.putExtra("name", nameTextView.getText());
                addContact.putExtra("phone", phoneTextView.getText());
                addContact.putExtra("email", emailTextView.getText());
                addContact.putExtra("street", streetTextView.getText());
                addContact.putExtra("city", cityTextView.getText());
                startActivity(addContact);
                return true;
            case R.id.deleteItem:
                deleteContact();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void deleteContact(){
        AlertDialog.Builder builder = new AlertDialog.Builder(openContacts.this);
        builder.setTitle("Delete Contact");
        builder.setMessage("Are you sure?");

        builder.setPositiveButton("Delete",
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int button){
                        final DatabaseConnector databaseConnector = new DatabaseConnector(openContacts.this);
                        AsyncTask<Long, Object, Object> deleteTask = new AsyncTask<Long, Object, Object>() {
                            @Override
                            protected Object doInBackground(Long... params){
                                databaseConnector.deleteContact(params[0]);
                                return null;
                            }
                            @Override
                            protected void onPostExecute(Object result){
                                finish();
                            }
                        };
                        deleteTask.execute(new Long[] { rowID });
                    }
                });
        builder.setNegativeButton("Cancel", null);
        builder.show();
    }
}
