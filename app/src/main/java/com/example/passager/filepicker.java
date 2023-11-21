package com.example.passager;

import static org.spongycastle.asn1.pkcs.PKCSObjectIdentifiers.data;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;


import com.example.passager.databinding.ActivityMainBinding;

public class filepicker extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, 101);


        String a = intent.getStringExtra("haha");
        //Log.v("gaga", a);



        Intent returnIntent = new Intent();

        returnIntent.putExtra("result", "aaa");
        setResult(Activity.RESULT_OK,intent);
        finish();


//        ActivityResultLauncher<String> launcher = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
//            @Override
//            public void onActivityResult(Uri o) {
//                binding.appBarMain.add.setImageURI(o);
//            }
//        });

        //binding.appBarMain.add.setOnClickListener(view -> launcher.launch("image/*"));


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == 101) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                Log.v("a", "b");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
                Log.v("a", "b");
            }
        }
    } //onActivityResult



}