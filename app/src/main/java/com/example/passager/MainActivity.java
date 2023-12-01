package com.example.passager;

//import static org.spongycastle.asn1.cms.CMSObjectIdentifiers.data;

import static android.app.PendingIntent.getActivity;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
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

import com.example.passager.ui.list_Fragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import java.io.File;
import android.Manifest.permission;
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
import org.linguafranca.pwdb.Database;
import org.linguafranca.pwdb.Entry;
import org.linguafranca.pwdb.Visitor;
import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.kdbx.jaxb.JaxbDatabase;
import org.linguafranca.pwdb.kdbx.simple.SimpleDatabase;
import org.w3c.dom.Text;

import org.linguafranca.pwdb.kdbx.*;




public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    //static PrintStream printStream = getTestPrintStream();





    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        TextView txtResult = findViewById(R.id.txtResult);

        String[] testlist = {"a","e","i","o","u"};
        List<String> vowelsList = Arrays.asList(testlist);



        ActivityCompat.requestPermissions(this,
                new String[]{permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE, permission.MANAGE_EXTERNAL_STORAGE},
                PackageManager.PERMISSION_GRANTED);

        if (!Environment.isExternalStorageManager()){
            Intent getpermission = new Intent();
            getpermission.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
            startActivity(getpermission);
        }







        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home ,R.id.nav_games,  R.id.nav_services, R.id.nav_add
        )
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);







        binding.appBarMain.add.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  showFileChooser();
                  String paths = (String) txtResult.getText();

                  KdbxCreds creds = new KdbxCreds("1234".getBytes());
                  File path = Environment.getExternalStoragePublicDirectory((Environment.DIRECTORY_DOWNLOADS+"/"+"test1.kdbx"));
                  Log.v("patj", path.getPath());

                  if (path != null){
                      try {
                          InputStream inputStream = new FileInputStream(path);
                          try {
                              Database database = SimpleDatabase.load(creds, inputStream);
                              if (database != null){

                                  Log.v("inputstream",  String.valueOf(database.getRootGroup().getEntries()));
                                  List entries = database.getRootGroup().getEntries();
                                  Log.v("inputstream",  String.valueOf(entries.get(0)));
                                  String entry1 = String.valueOf(entries.get(0));
                                   List c = database.findEntries("Sample Entry");
                                  c = database.findEntries("general");

                                   Entry temp =  (Entry) c.get(0);

                                  Log.v("list",  String.valueOf(c));
                                  Log.v("pw",  temp.getPassword());
                                  //Log.v("inputstream",  String.valueOf(database.findEntry(id)));

                              }

                          } catch (IOException e) {
                              throw new RuntimeException(e);
                          }

                      } catch (FileNotFoundException e) {
                          throw new RuntimeException(e);
                      }
                  }









              }
          }
        );







        Menu menu = navigationView.getMenu();
        menu.add("Home2").setCheckable(true);
        menu.add("Games2").setCheckable(true);
        int id =  menu.getItem(4).getItemId();
        menu.add(0, id, 0, "haha");




        CharSequence name =  menu.getItem(4).getTitle();
        MenuItem item =  menu.getItem(4);

        NavGraph graph = navController.getGraph();
        //graph.addDestination(ActivityNavigator(this).);


        Log.v("id", String.valueOf(id));
        Log.v("name", String.valueOf(name));

        Fragment fragment= new list_Fragment();



       // getSupportFragmentManager().beginTransaction().add(R.id.nav_host_fragment_content_main, fragment).commit();



        //addMenuItemInNavMenuDrawer();


        //MenuItem item = menu.getItem(4);
        //item.
        //menu.




        //getSupportFragmentManager().beginTransaction().

















//        getSupportFragmentManager().beginTransaction()
//                .add(id, fragment).commit();


;






        //Bundle category_entries = new Bundle();
        //category_entries.putStringArray("vowels", testlist);
        //category_entries.putInt("nr", 3);






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
                File file = new File(path);


                TextView txtResult = findViewById(R.id.txtResult);
                txtResult.setText(path);
                Log.v("aaa", path);

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

    public ArrayList<Fragment> getAllFragments() {
        ArrayList<Fragment> lista = new ArrayList<Fragment>();

        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            try {
                fragment.getTag();
                lista.add(fragment);
            } catch (NullPointerException e) {
            }
        }
        return lista;

    }

    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new list_Fragment();

        switch(item.getItemId()){
            default:fragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, fragment)
                .commit();
//            case 0:
//                Toast.makeText(this, "aha",
//                        Toast.LENGTH_SHORT).show();
//                return true;
        }

        return false;
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item){
//                Toast.makeText(this, "aha", Toast.LENGTH_LONG).show();
//                return true;
//        }

    private void addMenuItemInNavMenuDrawer() {
        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navView.getMenu();
        Menu submenu = menu.addSubMenu("New Super SubMenu");

        submenu.add("Super Item1");
        submenu.add("Super Item2");
        submenu.add("Super Item3");

        navView.invalidate();
    }



    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        // update the main content by replacing fragments
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager(); // For AppCompat use getSupportFragmentManager
        switch(id) {
            default:fragment = new list_Fragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main, fragment)
                        .commit();
            case 4:
                fragment = new list_Fragment();

                break;
            case 1245:
                fragment = new list_Fragment();

                break;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment_content_main, fragment)
                .commit();
        return true;
    }














}

