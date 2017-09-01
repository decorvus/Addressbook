package com.example.corvus.addressbook;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class addContacts extends AppCompatActivity {
    private long rowID;
    private EditText nameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText streetEditText;
    private EditText cityEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contacts);

        Button cancel = (Button) findViewById(R.id.button4);
        Button save = (Button) findViewById(R.id.button);
        nameEditText = (EditText) findViewById(R.id.addname);
        phoneEditText = (EditText) findViewById(R.id.addnumber);
        emailEditText = (EditText) findViewById(R.id.addemail);
        streetEditText = (EditText) findViewById(R.id.addstreet);
        cityEditText = (EditText) findViewById(R.id.addcity);

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            rowID = extras.getLong("row_id");
            nameEditText.setText(extras.getString("name"));
            phoneEditText.setText(extras.getString("phone"));
            emailEditText.setText(extras.getString("email"));
            streetEditText.setText(extras.getString("street"));
            cityEditText.setText(extras.getString("city"));
        }
        Button saveContactButton = (Button) findViewById(R.id.button);
        saveContactButton.setOnClickListener(saveContactButtonClicked);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(addContacts.this, viewContacts.class));
            }
        });
    }
    OnClickListener saveContactButtonClicked = new OnClickListener(){
        @Override
        public void onClick(View v){
            if (nameEditText.getText().length() != 0){
                AsyncTask<Object, Object, Object> saveContactTask = new AsyncTask<Object, Object, Object>() {
                    @Override
                    protected Object doInBackground(Object... params){
                        saveContact();
                        return null;
                    }
                    @Override
                    protected void onPostExecute(Object result){
                        finish();
                    }
                };
                saveContactTask.execute((Object[]) null);
            }
            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(addContacts.this);
                builder.setTitle("Error");
                builder.setMessage("An error has occured");
                builder.setPositiveButton("Okay", null);
                builder.show();
            }
        }
    };

    private void saveContact(){
        DatabaseConnector databaseConnector = new DatabaseConnector(this);

        if (getIntent().getExtras() == null){
            System.out.println("OREWA" + nameEditText.getText());
            System.out.println("OREWA" + nameEditText.getText().toString());
            System.out.println("OREWA" + String.valueOf(nameEditText.getText()));
            databaseConnector.addContacts(
                    //marker  insertContact originally

                    nameEditText.getText().toString(),
                    phoneEditText.getText().toString(),
                    emailEditText.getText().toString(),
                    streetEditText.getText().toString(),
                    cityEditText.getText().toString());
        }
        else {
            databaseConnector.updateContact(rowID,
                    nameEditText.getText().toString(),
                    phoneEditText.getText().toString(),
                    emailEditText.getText().toString(),
                    streetEditText.getText().toString(),
                    cityEditText.getText().toString());


        }

    }
}


