package com.example.passager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;

public class gen_password extends AppCompatActivity  {

    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen_password);

        password_generator pg = new password_generator();

        Switch lowercase = findViewById(R.id.switch_lowercase);
        Switch uppercase = findViewById(R.id.switch_uppercase);
        Switch numbers = findViewById(R.id.switch_numbers);
        Switch special = findViewById(R.id.switch_special_characters);
        TextView length = findViewById(R.id.pw_length);
        TextView password_txt = findViewById(R.id.pw_result);






       Button generate =  (Button) findViewById(R.id.pw_generate);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String lenght_s = length.getText().toString();

                if (!lenght_s.matches("[0-9]+")){
                    Toast.makeText( getApplicationContext(), "Please enter a number", Toast.LENGTH_SHORT).show();
                }
                else {
                    int lenght_int = Integer.parseInt(lenght_s);

                    if (!lowercase.isChecked() && !uppercase.isChecked()&& !numbers.isChecked() && !special.isChecked()){
                        Toast.makeText( getApplicationContext(), "No Switch checked!", Toast.LENGTH_SHORT).show();
                    } else if (3 >= lenght_int || 33 <= lenght_int) {
                        Toast.makeText( getApplicationContext(), "Please enter number between 4 and 32", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        password = pg.generatePassword(lenght_int, lowercase.isChecked(),
                                uppercase.isChecked(), numbers.isChecked(), special.isChecked());

                        password_txt.setText(password);
                    }
                }
            }
        });


        Button keep =  (Button) findViewById(R.id.pw_keep);
        keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent returnIntent = new Intent();
                returnIntent.putExtra("password", password);
                setResult(RESULT_OK, returnIntent);
                finish();

            }
        });


    }
}