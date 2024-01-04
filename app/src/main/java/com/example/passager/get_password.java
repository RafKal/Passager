package com.example.passager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import java.io.Serializable;

public class get_password extends AppCompatActivity  {

    String Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_password);

        final EditText txtUrl = new EditText(this);



        // Set the default text to a link of the Queen
        txtUrl.setHint("Password?");
        //String Password;

        new AlertDialog.Builder(this)
                .setTitle("Password for chosen Database?")
                //.setMessage("optional message")
                .setView(txtUrl)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Password = txtUrl.getText().toString();

                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("password", Password);
                        setResult(RESULT_OK, returnIntent);
                        finish();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();

    }
}