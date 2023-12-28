package com.example.passager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class gen_newDB extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_new_db);



        TextView name_text = findViewById(R.id.gen_name);
        android.widget.TextView password_text = findViewById(R.id.gen_master_password);
        TextView description_text = findViewById(R.id.gen_description);
        android.widget.TextView repeat_password_text = findViewById(R.id.gen_repeat_master_password);

        //String[] Data = {name, password, notes, url};


        Button create_db = findViewById(R.id.gen_create);
        create_db.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();

                returnIntent.putExtra("name", name_text.getText());
                returnIntent.putExtra("master_password", password_text.getText());
                returnIntent.putExtra("repeat_password", repeat_password_text.getText());
                returnIntent.putExtra("description", description_text.getText());
                setResult(RESULT_OK, returnIntent);
                finish();
            }
        });
    }


}