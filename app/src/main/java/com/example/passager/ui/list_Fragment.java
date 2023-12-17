package com.example.passager.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import com.example.passager.MainActivity;
import com.example.passager.R;
import com.example.passager.add_entry;
import com.example.passager.startScreen;
import com.example.passager.ui.placeholder.PlaceholderContent;
import com.example.passager.entry;

import org.linguafranca.pwdb.Entry;
import org.linguafranca.pwdb.Group;
import org.linguafranca.pwdb.kdbx.KdbxCreds;
import org.linguafranca.pwdb.kdbx.simple.SimpleDatabase;
import org.linguafranca.pwdb.kdbx.simple.SimpleEntry;
import org.linguafranca.pwdb.kdbx.simple.SimpleGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.Inflater;

/**
 * A fragment representing a list of Items.
 */
public class list_Fragment extends ListFragment {


    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public list_Fragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static list_Fragment newInstance(int columnCount) {

        list_Fragment fragment = new list_Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;

    }




    ArrayAdapter groupadapter;
    public AppCompatActivity parent;





    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);


            ArrayList<Parcelable> entries = getArguments().getParcelableArrayList("entries");
            ArrayList<Parcelable> groups = getArguments().getParcelableArrayList("groups");







            Log.v("neue entrues", String.valueOf(entries));


            setListAdapter(groupadapter);

            MainActivity Activity = ((MainActivity) getActivity());







            if (groups.size() > 0) {
                    Log.v("ist in groups.size() > 0", String.valueOf("ja"));

                    ArrayList<String> group_names = new ArrayList<String>();


                    for (int i = 0; i < groups.size(); i++) {
                        SimpleGroup temp = (SimpleGroup) groups.get(i);
                        String grp_name = temp.getName();
                        grp_name = "|GROUP| " + grp_name;
                        group_names.add(grp_name);
                    }
                    groupadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, group_names);



                    if (entries  != null) {
                        if (entries.size() > 0) {
                            Log.v("ist in entries.size() > 0", String.valueOf("ja"));
                            ArrayList<String> entries_names = new ArrayList<String>();

                            for (int i = 0; i < entries.size(); i++) {
                                SimpleEntry temp = (SimpleEntry) entries.get(i);
                                String entry_name = temp.getTitle();

                                entries_names.add(entry_name);
                            }


                            groupadapter.addAll(entries_names);
                        }
                    }

                }

            else if (entries  != null) {
                if (entries.size() > 0) {
                    Log.v("ist in zweite entries.size() > 0", String.valueOf("ja"));

                    ArrayList<String> entries_names = new ArrayList<String>();

                    for (int i = 0; i < entries.size(); i++) {
                        SimpleEntry temp = (SimpleEntry) entries.get(i);
                        String entry_name = temp.getTitle();
                        entries_names.add(entry_name);
                    }

                    groupadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, entries_names);

                }
            }
            else { Log.v("es ist", "leer"); }



            if (groupadapter == null){
                groupadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
            }

            groupadapter.add("ADD Entry");
            groupadapter.add("ADD Group ");

            setListAdapter(groupadapter);

        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS));
        }

        return view;
    }

    @Override
    public void onViewCreated (View view, Bundle savedInstanceState) {

        ListView lv = getListView();

        MainActivity Activity = ((MainActivity) getActivity());




        //FragmentManager fragmentManager = getParentFragmentManager();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
                                    long id) {


                start_loop:
                if (getArguments() != null) {
                    mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);


                    if (lv.getAdapter().getCount()-2 == position){

                        add_entry add_entry = new add_entry();

                        fragmentManager.beginTransaction()
                                .replace(R.id.nav_host_fragment_content_main, add_entry).addToBackStack(null)
                                .commit();

                        break start_loop;

                    }

                    if (lv.getAdapter().getCount()-1 == position){

                        /*add_entry add_entry = new add_entry();
                        fragmentManager.beginTransaction()
                                .replace(R.id.nav_host_fragment_content_main, add_entry).addToBackStack(null)
                                .commit();*/

                        input_grp_name();




                        Log.v("kommt an", "ja");

                        break start_loop;

                    }


                    ArrayList<Parcelable> entries = getArguments().getParcelableArrayList("entries");
                    ArrayList<Parcelable> groups = getArguments().getParcelableArrayList("groups");


                    Log.v("groups.size;",    String.valueOf(groups.size()));




                    int grp_size = groups.size();

                    if (groups != null){
                        grp_size = groups.size();
                    }
                    else{
                        grp_size = 0;

                    }




                    if ( (position+1)  > grp_size) {
                            SimpleEntry test_entry = (SimpleEntry) entries.get(position - groups.size());

                            entry entry_fragment = new entry();
                            Bundle bundle = new Bundle();
                            bundle.putString("title", test_entry.getTitle());
                            bundle.putString("notes", test_entry.getNotes());
                            bundle.putString("password", test_entry.getPassword());

                            entry_fragment.setArguments(bundle);




                            fragmentManager.beginTransaction()
                                    .replace(R.id.nav_host_fragment_content_main, entry_fragment).addToBackStack(null)
                                    .commit();
                        }
                    else if (grp_size > 0) {

                           //Group grp_toSend = (SimpleGroup) groups.get(position);
                            Group grp_toSend = (SimpleGroup) groups.get(position);
                            Activity.set_group(grp_toSend);
                            List entries_toSend = grp_toSend.getEntries();

                            list_Fragment next_fragment = new list_Fragment();

                            ArrayList<Group> new_groups = new ArrayList<Group>(grp_toSend.getGroups().size());

                            if(grp_toSend != null){
                                new_groups.addAll(grp_toSend.getGroups());
                            }


                            ArrayList<Entry> new_entries = new ArrayList<Entry>(entries_toSend.size());
                            if (entries_toSend  != null){
                                new_entries.addAll(entries_toSend);
                            }



                            Bundle bundle = new Bundle();
                            bundle.putSerializable("groups", new_groups);
                            bundle.putSerializable("entries", new_entries);
                            bundle.putInt("int", 1);

                            Log.v("ist angekommen", "ja");
                            Log.v("gesendete gruppe", String.valueOf(new_groups));
                            Log.v("gesendete entries", String.valueOf(new_entries));


                            next_fragment.setArguments(bundle);


                            fragmentManager.beginTransaction()
                                    .replace(R.id.nav_host_fragment_content_main, next_fragment).addToBackStack(null)
                                    .commit();
                        }





                }




            }
        });




    }

    public void input_grp_name(){

        final EditText grp_name = new EditText(getActivity());

        grp_name.setHint("Name");

        new AlertDialog.Builder(getActivity())
                .setTitle("Group name")
                .setView(grp_name)
                .setPositiveButton("Moustachify", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        MainActivity Activity = ((MainActivity) getActivity());
                        String name = grp_name.getText().toString();

                        Activity.add_grp(name);



                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
    }





}

