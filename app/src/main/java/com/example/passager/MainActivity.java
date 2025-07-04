package com.example.passager;

import static org.spongycastle.asn1.cms.CMSObjectIdentifiers.data;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.net.Uri;
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
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import de.slackspace.openkeepass.KeePassDatabase;
import de.slackspace.openkeepass.domain.KeePassFile;
import de.slackspace.openkeepass.KeePassDatabase;
import de.slackspace.openkeepass.domain.Entry;
import de.slackspace.openkeepass.domain.Group;
import de.slackspace.openkeepass.domain.KeePassFile;

import java.io.File;
import java.net.URI;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.example.passager.databinding.ActivityMainBinding;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        TextView txtResult = findViewById(R.id.txtResult);

        String[] testlist = {"a","e","i","o","u"};
        List<String> vowelsList = Arrays.asList(testlist);








        //KeePassFile database = KeePassDatabase.getInstance("document/raw:/storage/emulated/0/Download/test.kdbx").openDatabase("1234");

//        ActivityCompat.requestPermissions(this,
//                new String[]{permission.WRITE_EXTERNAL_STORAGE, permission.READ_EXTERNAL_STORAGE},
//                PackageManager.PERMISSION_GRANTED);






        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_games,  R.id.nav_services, R.id.nav_add)
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
                  if(paths != ""){
                      Log.v("af", paths);
                      KeePassFile database = KeePassDatabase.getInstance("/storage/emulated/0/Download/test.kdbx").openDatabase("1234");}
              }
          }
        );

        //final Menu menu = navigationView.getMenu();
        //menu.add("eins");

        Bundle category_entries = new Bundle();
        //category_entries.putStringArray("vowels", testlist);
        category_entries.putInt("nr", 3);

        Fragment temp = getSupportFragmentManager().findFragmentById(R.id.nav_home);
        temp.setArguments(category_entries);


        //Fragment fragment = new ListFragment();
        //getSupportFragmentManager().beginTransaction().add(androidx.fragment.R.id.fragment_container_view_tag, fragment).commit();

        //temp.setArguments(category_entries);



        ArrayList<Fragment> lista = getAllFragments();
        //long anzahlFrag = lista.stream().count();
        //Log.v("number", String.valueOf(anzahlFrag));

        //Fragment temp = getVisibleFragment();
        //temp.getId();














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


    //String path =  "/storage/emulated/0/Download/test.kdbx";
    //KeePassFile database = KeePassDatabase.getInstance(path).openDatabase("1234");
    //File file = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
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


}
