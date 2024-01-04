package com.example.passager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class startScreen extends AppCompatActivity {
    int result;
    Boolean no_db = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        //set 0 for create, 1 for import

        no_db = false;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
             no_db = extras.getBoolean("path");
        }





        Button create_db = findViewById(R.id.create_database);
        create_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", 0);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });

        Button import_db = findViewById(R.id.import_database);
        import_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                returnIntent.putExtra("result", 1);
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });


    }


    @Override
    public void onBackPressed() {

        Log.v("show", no_db.toString());
        {
            if (!no_db){
                super.onBackPressed();
            }
        }

    }


}