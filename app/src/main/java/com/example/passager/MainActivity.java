package com.example.passager;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.PathUtils;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.passager.databinding.ActivityMainBinding;
import com.example.passager.ui.list_Fragment;
import com.google.android.material.navigation.NavigationView;


/* Quelle:
https://github.com/jorabin/KeePassJava2
Library: KeePassJava2
Ver.: 2.3.1
Lizenz: Apache2*/
import org.linguafranca.pwdb.Entry;
import org.linguafranca.pwdb.Group;
import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.kdbx.KdbxHeader;
import org.linguafranca.pwdb.kdbx.KdbxStreamFormat;
import org.linguafranca.pwdb.kdbx.simple.SimpleDatabase;
import org.linguafranca.pwdb.kdbx.simple.SimpleEntry;
import org.linguafranca.pwdb.kdbx.simple.SimpleGroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Executor;


public class MainActivity extends AppCompatActivity   {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    SimpleDatabase database = new SimpleDatabase();
    ArrayList<String> group_names = new ArrayList<String>();


    Group current_group;
    Group grp_toSend;
    Entry copied_entry;

    String filepicker_path = "";
    int startScreen_result = 2; //startScreen can have 2 options (0 & 1). 2 handled as error
    String Password;


    androidx.biometric.BiometricPrompt biometricPrompt;
    BiometricPrompt.PromptInfo promptInfo;
    ConstraintLayout mMainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
       // AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(binding.getRoot());

        //-----------------------------------------------------------------------------------

        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL)){
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(this, "No Fingerprint reader!", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(this, "No Fingerprint not working!", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(this, "No Fingerprint on Device!", Toast.LENGTH_SHORT).show();
                break;
        }

        Executor executor = ContextCompat.getMainExecutor(this);

       /* Handler handler = new Handler();

        Executor executor = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };*/



        biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                Toast.makeText(getApplicationContext(), "Login error!", Toast.LENGTH_SHORT).show();
                super.onAuthenticationError(errorCode, errString);
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                Toast.makeText(getApplicationContext(), "Login Success!", Toast.LENGTH_SHORT).show();
                super.onAuthenticationSucceeded(result);
            }

            @Override
            public void onAuthenticationFailed() {
                Toast.makeText(getApplicationContext(), "Login Failed!", Toast.LENGTH_SHORT).show();
                super.onAuthenticationFailed();
            }
        });


        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Passager").
                 setDescription("Use fingerprint").setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL).
                build();

      /*  promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Passager").
                setDescription("Use fingerprint").setDeviceCredentialAllowed(true).
                build();*/


        biometricPrompt.authenticate(promptInfo);

        //----------------------------------------------------------------------------------




        setSupportActionBar(binding.appBarMain.toolbar);
        FragmentManager fragmentManager = getSupportFragmentManager();

      /*  ActivityCompat.requestPermissions(this,
                new String[]{permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE, permission.MANAGE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);*/

        if (!Environment.isExternalStorageManager()){
            Intent getpermission = new Intent();
            getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivity(getpermission);
        }


        // Initial start screen
        Intent launch = new Intent(this, startScreen.class);
        launch.putExtra("path", true);
        startActivityForResult(launch, 101);
        launch.removeExtra("path");



        //build navigation elements
        ListView navmenu = findViewById(R.id.list_slidermenu);
        ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(this, R.array.default_groups, android.R.layout.simple_list_item_1);
        navmenu.setAdapter(arrayAdapter);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home
        )
                .setOpenableLayout(drawer)
                .build();
          NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
          NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
          NavigationUI.setupWithNavController(navigationView, navController);





        binding.appBarMain.add.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  //fragmentManager.clearBackStack(null);
                  startActivityForResult(launch, 101);
              }
          }
        );

        navmenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
                                    long id) {

                if(database != null){
                    String name = group_names.get(position);
                    binding.appBarMain.toolbar.setTitle(name);

                    if (position == 0){
                        grp_toSend = database.getRootGroup();
                    }
                    else {
                        grp_toSend = database.getRootGroup().getGroups().get(position-1);
                    }
                    current_group = grp_toSend;

                    list_Fragment new_fragment = new list_Fragment();
                    new_fragment.setArguments(new Bundle());

                    fragmentManager.beginTransaction()
                            .add(R.id.nav_host_fragment_content_main, new_fragment).addToBackStack(null)
                            .commit();

                    drawer.closeDrawers();

                }
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item ) {

        int id = item.getItemId();
        int save_id = R.id.action_save;
        int paste_entry_id = R.id.action_paste_entry;

        if (id  ==save_id){
            if (filepicker_path != null){
                save_db();
            }
        }
        if (id  == paste_entry_id){
            if (current_group != null && copied_entry!= null){
                current_group.addEntry(copied_entry);
                FragmentManager fm = getSupportFragmentManager();
                List frags = fm.getFragments();
                Log.v("paste frags", frags.toString());
                Fragment list_frag = (Fragment) frags.get(frags.size()-1);


                if (list_frag instanceof list_Fragment) {
                    ((list_Fragment) list_frag).update_listview();
                }
            }
            else{
                Toast.makeText(this, "No Entry Copied!", Toast.LENGTH_SHORT).show();
            }
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            if(resultCode == Activity.RESULT_OK & data!= null){
                Uri uri = data.getData();

                FileUtils pu = new FileUtils(getApplicationContext());
                String path = pu.getPath(uri);
               // String path = PathUtils.getPath(getApplicationContext(), uri);
                Log.v("path", path);


               /* path = uri.getPath();
                File file = new File(path);

                //String path = uri.getLastPathSegment();
                //Log.v("path", filepicker_path);
                Log.v("path", file.getAbsolutePath());
                Log.v("path", uri.getPath());
                //path = uri.getLastPathSegment().substring(4);

                path = getApplicationContext().getFilesDir().getAbsolutePath();
                Log.v("path", path);

                //File file = new File(path + "/abikor.txt");

                File thi = new File(getApplicationContext().getExternalFilesDir(null), "test.xls");

                Log.v("path", thi.getAbsolutePath());*/




                filepicker_path = path;
                Log.v("path", filepicker_path);

                TextView txtResult = findViewById(R.id.txtResult);
                txtResult.setText(path);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }

        if (requestCode == 101) {
            if(resultCode == Activity.RESULT_OK & data!= null){
                Uri uri = data.getData();
                startScreen_result = data.getIntExtra("result", 2);
                if (startScreen_result == 1){
                    database = import_db();
                }
                else if (startScreen_result == 0){
                    Intent gen_newDB = new Intent(this, gen_newDB.class);
                    startActivityForResult(gen_newDB, 102);
                }
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
            }
        }

        if (requestCode == 102) {
            if(resultCode == Activity.RESULT_OK & data!= null){
            String name = data.getStringExtra("name");
            String master_password = data.getStringExtra("master_password");
            String repeat_password = data.getStringExtra("repeat_password");
            String description = data.getStringExtra("description");
            Password = master_password;
            filepicker_path = "";

            database = new SimpleDatabase();
            

            database.setName(name);
            database.getRootGroup().setName(name);

            database.setDescription(description);

            database.getRootGroup().addGroup(database.newGroup("General"));
            database.getRootGroup().addGroup(database.newGroup("Windows"));
            database.getRootGroup().addGroup(database.newGroup("Network"));
            database.getRootGroup().addGroup(database.newGroup("Internet"));
            database.getRootGroup().addGroup(database.newGroup("eMail"));
            database.getRootGroup().addGroup(database.newGroup("Homebanking"));

            group_names.clear();
            String db_name = database.getName();


            String root = database.getRootGroup().getName();
            List entries = database.getRootGroup().getEntries();

            //get List of all groups, get only group name (/db_name/eMail -> eMail, and send group names to ArrayAdapter)
            List groups = database.getRootGroup().getGroups();

            group_names.add(root + " (ROOT)");
            for (int i  = 0; i < groups.size(); i++){
                String temp = groups.get(i).toString();
                temp = temp.substring((root.length()+2), temp.length()-1);
                group_names.add(temp);
            }
            ArrayAdapter groupadapter;
            groupadapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, group_names);
            ListView navmenu = findViewById(R.id.list_slidermenu);
            navmenu.setAdapter(groupadapter);

            binding.appBarMain.toolbar.setTitle(db_name);
            current_group = database.getRootGroup();

            list_Fragment new_fragment = new list_Fragment();
            new_fragment.setArguments(new Bundle());
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, new_fragment).addToBackStack(null)
                    .commit();
        }
            else {
                Intent launch = new Intent(MainActivity.this, startScreen.class);
                startActivityForResult(launch, 101);
            }
        }

    } //onActivityResult

    private void showFileChooser(){
        Intent fileChooser_intent = new Intent(Intent.ACTION_GET_CONTENT);

        fileChooser_intent.setType("*/*");
        fileChooser_intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(fileChooser_intent, "Select kdbx file!"), 100);
        }
        catch (Exception exception){
            Toast.makeText(this, "Install a file manager!", Toast.LENGTH_LONG).show();
        }
    }

    private SimpleDatabase import_db(){

        String Password = input_password();
        showFileChooser();
        ListView navmenu = findViewById(R.id.list_slidermenu);
    return database;
}


//https://stackoverflow.com/questions/10903754/input-text-dialog-android
private String input_password(){
    final EditText txtUrl = new EditText(this);
    txtUrl.setHint("Password?");

    new AlertDialog.Builder(this)
            .setTitle("Password for chosen Database?")
            .setView(txtUrl)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Password = txtUrl.getText().toString();
                    ListView navmenu = findViewById(R.id.list_slidermenu);
                    if (!Objects.equals(filepicker_path, "")){
                        try {
                            InputStream inputStream = new FileInputStream(filepicker_path);
                            try {
                                KdbxCreds creds = new KdbxCreds(Password.getBytes());
                                database = SimpleDatabase.load(creds, inputStream);

                                    group_names.clear();
                                    String db_name = database.getName();
                                    String root = database.getRootGroup().getName();

                                    List entries = database.getRootGroup().getEntries();

                                    List groups = database.getRootGroup().getGroups();

                                    group_names.add(root + " (ROOT)");
                                    for (int i  = 0; i < groups.size(); i++){
                                        String temp = groups.get(i).toString();
                                        temp = temp.substring((root.length()+2), temp.length()-1);
                                        group_names.add(temp);
                                    }
                                    ArrayAdapter groupadapter;
                                    groupadapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, group_names);
                                    navmenu.setAdapter(groupadapter);

                                    binding.appBarMain.toolbar.setTitle(db_name);
                                    current_group = database.getRootGroup();





                                    list_Fragment new_fragment = new list_Fragment();
                                    new_fragment.setArguments(new Bundle());


                                  FragmentManager fragmentManager = getSupportFragmentManager();
                                    fragmentManager.beginTransaction()
                                            .add(R.id.nav_host_fragment_content_main, new_fragment).addToBackStack(null)
                                            .commit();

                            } catch (Exception e) {
                                //throw new RuntimeException(e);
                                Intent launch = new Intent(MainActivity.this, startScreen.class);
                                startActivityForResult(launch, 101);
                                Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_LONG).show();

                            }
                        } catch (FileNotFoundException e) {
                            //throw new RuntimeException(e);
                            Toast.makeText(MainActivity.this, filepicker_path, Toast.LENGTH_LONG).show();
                            //Toast.makeText(MainActivity.this, "File not found", Toast.LENGTH_LONG).show();

                        }
                    }

                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    ListView navmenu = findViewById(R.id.list_slidermenu);

                    if (!Objects.equals(filepicker_path, "")){
                        try {
                            InputStream inputStream = new FileInputStream(filepicker_path);
                            try {
                                KdbxCreds creds = new KdbxCreds(Password.getBytes());
                                database = SimpleDatabase.load(creds, inputStream);

                                group_names.clear();

                                String db_name = database.getName();
                                String root = database.getRootGroup().getName();

                                //get List of all groups, get only group name (/db_name/eMail -> eMail, and send group names to ArrayAdapter)
                                List groups = database.getRootGroup().getGroups();
                                for (int i  = 0; i < groups.size(); i++){
                                    String temp = groups.get(i).toString();
                                    temp = temp.substring((root.length()+2), temp.length()-1);
                                    group_names.add(temp);
                                }
                                ArrayAdapter groupadapter;
                                groupadapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, group_names);
                                navmenu.setAdapter(groupadapter);
                            }  catch (Exception e) {
                                //throw new RuntimeException(e);
                                Intent launch = new Intent(MainActivity.this, startScreen.class);
                                startActivityForResult(launch, 101);
                            }
                        } catch (Exception e) {
                            //throw new RuntimeException(e);
                            Intent launch = new Intent(MainActivity.this, startScreen.class);
                            startActivityForResult(launch, 101);
                        }
                    }
                    else {
                        Intent launch = new Intent(MainActivity.this, startScreen.class);
                        startActivityForResult(launch, 101);
                    }
                }
            })
            .show();
    return Password;
}

    public void print(){
        Entry entry1 = database.getRootGroup().getEntries().get(0);
    }

    public void add_grp(String name){
        if (name != null){
            SimpleGroup group = database.newGroup(name);
            current_group.addGroup(group);

            group_names.clear();

            String db_name = database.getName();

            String root = database.getRootGroup().getName();

            List<SimpleEntry> entries = database.getRootGroup().getEntries();

            //get List of all groups, get only group name (/db_name/eMail -> eMail, and send group names to ArrayAdapter)
            List<SimpleGroup> groups = database.getRootGroup().getGroups();

            group_names.add(root + " (ROOT)");
            for (int i  = 0; i < groups.size(); i++){
                String temp = groups.get(i).toString();
                temp = temp.substring((root.length()+2), temp.length()-1);
                group_names.add(temp);
            }
            ArrayAdapter groupadapter;
            groupadapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, group_names);
            ListView navmenu = findViewById(R.id.list_slidermenu);
            navmenu.setAdapter(groupadapter);

        }
    }

    public void delete_grp(){
        onBackPressed();
        UUID uuid = current_group.getUuid();
        database.deleteGroup(uuid);
        }

    public void set_group(Group group){
        current_group = group;
    }

    public Group get_current_group(){
        return current_group;
    }

    @Override
    public void onBackPressed()
    {

        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        if (currentFragment instanceof list_Fragment) {

            if(current_group.getParent() != null){
                current_group = current_group.getParent();
                updateBar();
                super.onBackPressed();
            }

        }
        else {
            super.onBackPressed();
        }
    }

    public void updateBar(){
        binding.appBarMain.toolbar.setTitle(get_current_group().getPath());
    }
    public void save_db(){
        try {
            if (filepicker_path == ""){
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File file_dir = new File(dir + "/" + database.getName().replace(" ", "_") + ".kdbx");
                //filepicker_path =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath();


                KdbxStreamFormat streamFormat = new KdbxStreamFormat(new KdbxHeader(4));
                OutputStream outputStream = new FileOutputStream(file_dir);

                KdbxCreds creds = new KdbxCreds(Password.getBytes());
                //file_dir.mkdir();
                FileOutputStream writer = new FileOutputStream(new File(dir, database.getName().replace(" ", "_")) + ".kdbx");
                Log.v("path file", file_dir.toString());
                database.save(streamFormat, creds, writer);

            }
            else {
                KdbxCreds creds = new KdbxCreds(Password.getBytes());
                OutputStream outputStream = new FileOutputStream(filepicker_path);
                database.save(creds, outputStream);
            }

            Log.v("path file", filepicker_path);


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete_entry(UUID uuid){
        database.deleteEntry(uuid);

        FragmentManager fm = getSupportFragmentManager();
        List frags = fm.getFragments();
        Fragment list_frag = (Fragment) frags.get(frags.size()-2);
        Log.v("delete frags", frags.toString());
        Log.v("tester", list_frag.toString());


        if (list_frag instanceof list_Fragment) {
            ((list_Fragment) list_frag).update_listview();

            Log.v("ist hirtaa", "ja");

        }
        group_names.clear();


        String root = database.getRootGroup().getName();
        List entries = database.getRootGroup().getEntries();
        List groups = database.getRootGroup().getGroups();
        group_names.add(root + " (ROOT)");
        for (int i  = 0; i < groups.size(); i++){
            String temp = groups.get(i).toString();
            temp = temp.substring((root.length()+2), temp.length()-1);
            group_names.add(temp);
        }
        ArrayAdapter groupadapter;
        groupadapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, group_names);
        ListView navmenu = findViewById(R.id.list_slidermenu);
        navmenu.setAdapter(groupadapter);

    }

    public void add_entry(Entry entry){
        current_group.addEntry(entry);
        FragmentManager fm = getSupportFragmentManager();
        List frags = fm.getFragments();
        Fragment list_frag = (Fragment) frags.get(frags.size()-2);

        Log.v("tester", list_frag.toString());


        if (list_frag instanceof list_Fragment) {
            ((list_Fragment) list_frag).update_listview();
        }
    }

    public void copy_entry(UUID uuid, String title) {
        copied_entry = database.findEntry(uuid);

        if (copied_entry == null){
            Group rec_bin = database.getRecycleBin();
            copied_entry = (Entry)  rec_bin.findEntries(title, true).get(0);
            Log.v("test entry", copied_entry.toString());
        }

    }

    public void edit_entry(Entry entry, UUID uuid){
        Entry old_entry = database.findEntry(uuid);
        old_entry.setTitle(entry.getTitle());
        old_entry.setNotes(entry.getNotes());
        old_entry.setPassword(entry.getPassword());
        old_entry.setUrl(entry.getUrl());
        old_entry.setUsername(entry.getUsername());

        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();


        List frags = fm.getFragments();
        Fragment currentFragment = (Fragment) frags.get(frags.size()-3);
        if (currentFragment instanceof list_Fragment) {
            ((list_Fragment) currentFragment).update_listview();

            }
        }
    }














