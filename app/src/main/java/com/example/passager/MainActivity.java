package com.example.passager;

//import static org.spongycastle.asn1.cms.CMSObjectIdentifiers.data;

import static android.app.PendingIntent.getActivity;

import static java.security.AccessController.getContext;




import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.net.Uri;

import com.example.passager.ui.home.HomeFragment;
import com.example.passager.ui.list_Fragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import java.io.File;
import android.Manifest.permission;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

//import de.slackspace.openkeepass.KeePassDatabase;
//import de.slackspace.openkeepass.domain.KeePassFile;
//import de.slackspace.openkeepass.KeePassDatabase;
//import de.slackspace.openkeepass.domain.Entry;
//import de.slackspace.openkeepass.domain.Group;
//import de.slackspace.openkeepass.domain.KeePassFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.security.Permission;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.passager.databinding.ActivityMainBinding;

//import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.apache.commons.codec.binary.StringUtils;
import org.linguafranca.pwdb.Database;
import org.linguafranca.pwdb.Entry;
import org.linguafranca.pwdb.Group;
import org.linguafranca.pwdb.Visitor;
import org.linguafranca.pwdb.base.AbstractDatabase;
import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.kdbx.jaxb.JaxbDatabase;
import org.linguafranca.pwdb.kdbx.simple.SimpleDatabase;
import org.linguafranca.pwdb.kdbx.simple.SimpleEntry;
import org.linguafranca.pwdb.kdbx.simple.SimpleGroup;
import org.w3c.dom.Text;

import org.linguafranca.pwdb.kdbx.*;

import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy;


public class MainActivity extends AppCompatActivity   {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    //static PrintStream printStream = getTestPrintStream();


    SimpleDatabase database = new SimpleDatabase();
    ArrayList<String> group_names = new ArrayList<String>();

    Group current_group;
    Group grp_toSend;

    String filepicker_path = "";
    int startScreen_result = 2; //startScreen can have 2 options (0 & 1). 2 handled as error
    String Password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {






        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        TextView txtResult = findViewById(R.id.txtResult);

        FragmentManager fragmentManager = getSupportFragmentManager();



        ActivityCompat.requestPermissions(this,
                new String[]{permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE, permission.MANAGE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

        if (!Environment.isExternalStorageManager()){
            Intent getpermission = new Intent();
            getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivity(getpermission);
        }


        Intent launch = new Intent(this, startScreen.class);
        startActivityForResult(launch, 101);



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

                  String filepicker_path = "";

                  //navController.navigate(R.id.nav_home);
                  fragmentManager.clearBackStack(null);

                /*  list_Fragment new_fragment = new list_Fragment();

                  fragmentManager.beginTransaction()
                          .replace(R.id.nav_host_fragment_content_main, new_fragment)
                          .commit();
*/


                  drawer.closeDrawers();

                  Log.v("dies wird", "erreicht");
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


                    List grp_entries = database.getRootGroup().findGroups(name);


                    if (position == 0){
                        grp_toSend = database.getRootGroup();
                    }
                    else {
                        grp_toSend = database.getRootGroup().getGroups().get(position-1);
                    }
                    current_group = grp_toSend;

                    grp_entries = grp_toSend.getEntries();


                    list_Fragment new_fragment = new list_Fragment();
                    ArrayList<Group> groups = new ArrayList<Group>(grp_toSend.getGroups().size());
                    groups.addAll(grp_toSend.getGroups());

                    ArrayList<Entry> entries = new ArrayList<Entry>(grp_entries.size());
                    entries.addAll(grp_entries);


                    Bundle bundle = new Bundle();
                    bundle.putSerializable("groups", groups);
                    bundle.putSerializable("entries", entries);


                    new_fragment.setArguments(bundle);

                    Log.v("current group", String.valueOf(current_group));



                    fragmentManager.beginTransaction()
                            .replace(R.id.nav_host_fragment_content_main, new_fragment).addToBackStack(null)
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

        Log.v("id", String.valueOf(item.getItemId()));

        switch (item.getItemId()){
            //save id
            case 2131296801 :

                 if (filepicker_path != null){
                     save_db();
                 }



                 break;

                 //delete group id
            case 2131296803 :
                //delete_grp();
                break;
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


                String path = uri.getPath();
                path = uri.getLastPathSegment().substring(4);

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
            Log.v("kommt", "an");

            Uri uri = data.getData();

            String name = data.getStringExtra("name");
            String master_password = data.getStringExtra("master_password");
            String repeat_password = data.getStringExtra("repeat_password");
            String description = data.getStringExtra("description");
            Password = master_password;
            filepicker_path = "";

            database = new SimpleDatabase();
            

            database.setName(name);
            database.getRootGroup().setName(name);

            //database.setName("name");
            //database.getRootGroup().setName("name");


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
            Log.v("Root Name onclick",  String.valueOf(root));

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
            groupadapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, group_names);
            ListView navmenu = findViewById(R.id.list_slidermenu);
            navmenu.setAdapter(groupadapter);

            binding.appBarMain.toolbar.setTitle(db_name);


            current_group = database.getRootGroup();



            FragmentManager fragmentManager = getSupportFragmentManager();

            binding.appBarMain.toolbar.setTitle(db_name);


            current_group = database.getRootGroup();




            Group grp_toSend = database.getRootGroup();;
            List grp_entries = database.getRootGroup().getEntries();
            list_Fragment new_fragment = new list_Fragment();

            ArrayList<Group> groups_root = new ArrayList<Group>(grp_toSend.getGroups().size());
            groups.addAll(grp_toSend.getGroups());

            ArrayList<Entry> entries_root = new ArrayList<Entry>(grp_entries.size());
            entries.addAll(grp_entries);
            Bundle bundle = new Bundle();
            bundle.putSerializable("groups", groups_root);
            bundle.putSerializable("entries", entries_root);
            new_fragment.setArguments(bundle);


            fragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment_content_main, new_fragment).addToBackStack(null)
                    .commit();
        }
        }

    } //onActivityResult

    private void showFileChooser(){
        Intent intent4 = new Intent(Intent.ACTION_GET_CONTENT);
        
        intent4.setType("*/*");
        intent4.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent4, "Select kdbx file!"), 100);
        }
        catch (Exception exception){
            Toast.makeText(this, "Install a file manager!", Toast.LENGTH_LONG).show();
        }
    }

    private SimpleDatabase import_db(){

        //filepicker_path = "";
        String Password = input_password();
        showFileChooser();
        ListView navmenu = findViewById(R.id.list_slidermenu);


    return database;

}


//https://stackoverflow.com/questions/10903754/input-text-dialog-android
private String input_password(){
    final EditText txtUrl = new EditText(this);
     // Set the default text to a link of the Queen
    txtUrl.setHint("Password?");
    //String Password;

    new AlertDialog.Builder(this)
            .setTitle("Password for chosen Database?")
            .setView(txtUrl)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Password = txtUrl.getText().toString();
                    ListView navmenu = findViewById(R.id.list_slidermenu);

                    //Log.v("kommh ier pw", filepicker_path);

                    if (filepicker_path != ""){
                        try {
                            InputStream inputStream = new FileInputStream(filepicker_path);
                            Log.v("kommh ier pw", filepicker_path);
                            try {
                                KdbxCreds creds = new KdbxCreds(Password.getBytes());
                                database = SimpleDatabase.load(creds, inputStream);

                                if (database != null){
                                    group_names.clear();

                                    String db_name = database.getName();
                                    //Log.v("Database Name",  String.valueOf(db_name));



                                    String root = database.getRootGroup().getName();
                                    Log.v("Root Name onclick",  String.valueOf(root));

                                    //Log.v("getEntries result",  String.valueOf(database.getRootGroup().getEntries()));
                                    //List entries = database.getRootGroup().getEntries();
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
                                    groupadapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, group_names);
                                    navmenu.setAdapter(groupadapter);

                                    binding.appBarMain.toolbar.setTitle(db_name);


                                    current_group = database.getRootGroup();



                                    FragmentManager fragmentManager = getSupportFragmentManager();

                                    binding.appBarMain.toolbar.setTitle(db_name);



                                    Group grp_toSend = database.getRootGroup();;
                                    List grp_entries = database.getRootGroup().getEntries();
                                    list_Fragment new_fragment = new list_Fragment();

                                    ArrayList<Group> groups_root = new ArrayList<Group>(grp_toSend.getGroups().size());
                                    groups.addAll(grp_toSend.getGroups());

                                    ArrayList<Entry> entries_root = new ArrayList<Entry>(grp_entries.size());
                                    entries.addAll(grp_entries);
                                    Bundle bundle = new Bundle();
                                    bundle.putSerializable("groups", groups_root);
                                    bundle.putSerializable("entries", entries_root);
                                    new_fragment.setArguments(bundle);


                                    fragmentManager.beginTransaction()
                                            .replace(R.id.nav_host_fragment_content_main, new_fragment).addToBackStack(null)
                                            .commit();



                                    //NavController navController = Navigation.findNavController(MainActivity.this, R.id.nav_host_fragment_content_main);




                                }
                                //Exception instead of IOexception here
                            } catch (Exception e) {
                                //throw new RuntimeException(e);
                                Intent launch = new Intent(MainActivity.this, startScreen.class);
                                startActivityForResult(launch, 101);
                                Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_LONG).show();

                            }
                        } catch (FileNotFoundException e) {
                            //throw new RuntimeException(e);
                            Toast.makeText(MainActivity.this, "File not found", Toast.LENGTH_LONG).show();

                        }
                    }



                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    ListView navmenu = findViewById(R.id.list_slidermenu);

                    if (filepicker_path != ""){
                        try {
                            InputStream inputStream = new FileInputStream(filepicker_path);
                            try {
                                KdbxCreds creds = new KdbxCreds(Password.getBytes());
                                database = SimpleDatabase.load(creds, inputStream);

                                if (database != null){
                                    group_names.clear();

                                    String db_name = database.getName();
                                    //Log.v("Database Name",  String.valueOf(db_name));



                                    String root = database.getRootGroup().getName();
                                    Log.v("Root Name onclick",  String.valueOf(root));

                                    //Log.v("getEntries result",  String.valueOf(database.getRootGroup().getEntries()));
                                    //List entries = database.getRootGroup().getEntries();
                                    List entries = database.getRootGroup().getEntries();

                                    //get List of all groups, get only group name (/db_name/eMail -> eMail, and send group names to ArrayAdapter)
                                    List groups = database.getRootGroup().getGroups();
                                    for (int i  = 0; i < groups.size(); i++){
                                        String temp = groups.get(i).toString();
                                        temp = temp.substring((root.length()+2), temp.length()-1);
                                        group_names.add(temp);
                                    }
                                    ArrayAdapter groupadapter;
                                    groupadapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, group_names);
                                    navmenu.setAdapter(groupadapter);

                                   /* current_group = database.getRootGroup();

                                    list_Fragment a = new list_Fragment();
                                    FragmentManager fragmentManager = getSupportFragmentManager();

                                    binding.appBarMain.toolbar.setTitle(db_name);

                                    fragmentManager.beginTransaction()
                                            .replace(R.id.nav_host_fragment_content_main, a).addToBackStack(null)
                                            .commit();*/
                                }
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
        Log.v("kann printen", "ja");
    }

    public void add_grp(String name){
        if (name != null){
            SimpleGroup group = database.newGroup(name);
            current_group.addGroup(group);

            group_names.clear();

            String db_name = database.getName();
            //Log.v("Database Name",  String.valueOf(db_name));



            String root = database.getRootGroup().getName();
            Log.v("Root Name onclick",  String.valueOf(root));

            //Log.v("getEntries result",  String.valueOf(database.getRootGroup().getEntries()));
            //List entries = database.getRootGroup().getEntries();
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
            groupadapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, group_names);
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

    public Group get_root_group(){
        return database.getRootGroup();
    }

    @Override
    public void onBackPressed()


    {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);

        Log.v("current frag", String.valueOf(currentFragment));

        if (currentFragment instanceof list_Fragment) {
            Log.v("is list", "yes");

            if(current_group.getParent() != null){
                current_group = current_group.getParent();
                updateBar();
            }
        }





        super.onBackPressed();  // optional depending on your needs
    }

    public void updateUI(){
        list_Fragment a = new list_Fragment();
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, a).addToBackStack(null)
                .commit();
    }

    public void updateBar(){
        //Group group
        binding.appBarMain.toolbar.setTitle(get_current_group().getPath());
    }
    public void save_db(){
        try {
            if (filepicker_path == ""){
                File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                File file_dir = new File(dir + "/" + database.getName().replace(" ", "_") + ".kdbx");
                //filepicker_path =  Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getPath();

                Log.v("path file", file_dir.toString());
                //KdbxStreamFormat streamFormat = new KdbxStreamFormat(new KdbxHeader(4));
                //OutputStream outputStream = new FileOutputStream(file_dir);

                KdbxCreds creds = new KdbxCreds(Password.getBytes());
                file_dir.mkdir();
                FileOutputStream writer = new FileOutputStream(new File(dir, database.getName().replace(" ", "_")) + ".kdbx");
                Log.v("path file", file_dir.toString());
                database.save(creds, writer);



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
    }

    public void add_entry(Entry entry){
        current_group.addEntry(entry);
    }

    public void edit_entry(Entry entry, UUID uuid){
        database.deleteEntry(uuid);
        current_group.addEntry(entry);

        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();


        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);

        Log.v("type?", currentFragment.toString());

        if (currentFragment instanceof list_Fragment) {
            ((list_Fragment) currentFragment).update_listview();

            Log.v("ist hirtaa", "ja");

            }
        }





    }














