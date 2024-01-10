package com.example.passager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class startScreen extends AppCompatActivity {
    Boolean no_db = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        //This Activity returns 0 for create, 1 for import

        no_db = false;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            //check if no Databank is open. If that's the case, deactivate onBackPressed
             no_db = extras.getBoolean("no_db");
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
        {
            if (!no_db){
                super.onBackPressed();
            }
        }

    }


}