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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import org.linguafranca.pwdb.Visitor;
import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.kdbx.jaxb.JaxbDatabase;
import org.linguafranca.pwdb.kdbx.simple.SimpleDatabase;
import org.w3c.dom.Text;

import org.linguafranca.pwdb.kdbx.*;





public class MainActivity extends AppCompatActivity  {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    //static PrintStream printStream = getTestPrintStream();


    SimpleDatabase database = new SimpleDatabase();
    ArrayList<String> group_names = new ArrayList<String>();

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





          //implement start screen with default new database values

          //group_names.add("Home");
          //group_names.add("Games");
          //group_names.add("Services");
          //group_names.add("Add");


          Log.v("startScreen_result in main", String.valueOf(startScreen_result));








        binding.appBarMain.add.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                 /* //showFileChooser();
                  //String paths = (String) txtResult.getText();

                  KdbxCreds creds = new KdbxCreds("1234".getBytes());
                  //File path = Environment.getExternalStoragePublicDirectory((Environment.DIRECTORY_DOWNLOADS+"/"+"test1.kdbx"));
                  //Log.v("File", path.getPath());
                  //Log.v("txtResultPath", paths);

                  //Log.v("filepicker path", filepicker_path);



                  if (filepicker_path != null){
                      try {
                          InputStream inputStream = new FileInputStream(filepicker_path);
                          try {
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


                              }

                          } catch (IOException e) {
                              throw new RuntimeException(e);
                          }

                      } catch (FileNotFoundException e) {
                          throw new RuntimeException(e);
                      }
                  }*/

                  //database = import_db();



                  navController.navigate(R.id.nav_home);
                  fragmentManager.clearBackStack(null);

                  list_Fragment new_fragment = new list_Fragment();

                  fragmentManager.beginTransaction()
                          .replace(R.id.nav_host_fragment_content_main, new_fragment)
                          .commit();



                  drawer.closeDrawers();
                  startActivityForResult(launch, 101);













              }
          }
        );

        navmenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
                                    long id) {

                if(database != null){



                    String name = group_names.get(position);
                    Log.v("onItemClick Name",  String.valueOf(name));


                    List grp_entries = database.getRootGroup().findGroups(name);


                    // make group search more robust so that groups with two / arent included in results

                    grp_entries = database.findEntries(name);
                    Log.v("onItemClick size",  String.valueOf(grp_entries.size()));
                    //grp_entries = database.findGroup()

                    Log.v("onItemClick entries",  String.valueOf(grp_entries));





                    if (grp_entries.size() > 0){
                        list_Fragment new_fragment = new list_Fragment();
                        ArrayList<Entry> groups = new ArrayList<Entry>(grp_entries.size());
                        groups.addAll(grp_entries);




                        Bundle bundle = new Bundle();
                        bundle.putSerializable("entries", groups);
                        new_fragment.setArguments(bundle);


                        fragmentManager.beginTransaction()
                                .replace(R.id.nav_host_fragment_content_main, new_fragment)
                                .commit();

                        drawer.closeDrawers();


                    }
                    else {
                        Toast.makeText(MainActivity.this, "Chosen Group is Empty", Toast.LENGTH_SHORT).show();
                    }



                }
            }
        });







    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
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

                TextView txtResult = findViewById(R.id.txtResult);
                txtResult.setText(path);
                Log.v("OnResult path", path);

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

                    String Password = input_password();

                }

                Log.v("startScreen_result", String.valueOf(startScreen_result));

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                // Write your code if there's no result
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
        showFileChooser();
        //String paths = (String) txtResult.getText();

        //KdbxCreds creds = new KdbxCreds("1234".getBytes());
        //File path = Environment.getExternalStoragePublicDirectory((Environment.DIRECTORY_DOWNLOADS+"/"+"test1.kdbx"));
        //Log.v("File", path.getPath());
        //Log.v("txtResultPath", paths);

        //Log.v("filepicker path", filepicker_path);
        ListView navmenu = findViewById(R.id.list_slidermenu);


        //Log.v("paswod", Password);



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


                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }


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
            //.setMessage("optional message")
            .setView(txtUrl)
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Password = txtUrl.getText().toString();

                }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                }
            })
            .show();
    return Password;
}
}

