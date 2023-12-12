package com.example.passager.ui;

import android.content.Context;
import android.os.Bundle;

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
import android.widget.ListView;
import android.widget.Toast;
import java.io.Serializable;

import com.example.passager.R;
import com.example.passager.ui.placeholder.PlaceholderContent;
import com.example.passager.entry;

import org.linguafranca.pwdb.Entry;
import org.linguafranca.pwdb.Group;
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




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);









            ArrayList<Parcelable> entries = getArguments().getParcelableArrayList("entries");
            ArrayList<Parcelable> groups = getArguments().getParcelableArrayList("groups");



           /* if (getArguments().getParcelableArrayList("new_entries") != null){
                entries = getArguments().getParcelableArrayList("new_entries");
                groups = getArguments().getParcelableArrayList("new_groups");

                Log.v("neue entrues", String.valueOf(entries));
            }*/

            Log.v("neue entrues", String.valueOf(entries));








            //Group testgroup = (SimpleGroup) groups.get(0);
            //Log.v("fragment log groups get 0", String.valueOf(testgroup));

            //ArrayAdapter groupadapter;









            if (groups != null) {
                if (groups.size() > 0) {

                    Log.v("ist in groups.size() > 0", String.valueOf("ja"));

//                SimpleEntry test_entry =  (SimpleEntry) groups.get(0);
//                Log.v("fragment log", String.valueOf(test_entry.getTitle()));
//
//                ArrayAdapter groupadapter;
//                groupadapter = new ArrayAdapter<String>(getActivity(), android.R.layout. simple_list_item_1, Collections.singletonList(test_entry.getTitle()));
//                setListAdapter(groupadapter);

                    ArrayList<String> group_names = new ArrayList<String>();


                    for (int i = 0; i < groups.size(); i++) {
                        SimpleGroup temp = (SimpleGroup) groups.get(i);
                        String grp_name = temp.getName();
                        group_names.add(grp_name);
                    }
                    groupadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, group_names);



                    if (entries  != null) {
                        if (entries.size() > 0) {
                            Log.v("ist in entries.size() > 0", String.valueOf("ja"));
//                SimpleEntry test_entry =  (SimpleEntry) groups.get(0);
//                Log.v("fragment log", String.valueOf(test_entry.getTitle()));
//
//                ArrayAdapter groupadapter;
//                groupadapter = new ArrayAdapter<String>(getActivity(), android.R.layout. simple_list_item_1, Collections.singletonList(test_entry.getTitle()));
//                setListAdapter(groupadapter);

                            ArrayList<String> entries_names = new ArrayList<String>();

                            for (int i = 0; i < entries.size(); i++) {
                                SimpleEntry temp = (SimpleEntry) entries.get(i);
                                String entry_name = temp.getTitle();
                                entries_names.add(entry_name);
                            }


                            groupadapter.addAll(entries_names);
                            //setListAdapter(groupadapter);
                        }
                    }

                    //setListAdapter(groupadapter);
                }



            }

            else if (entries  != null) {
                if (entries.size() > 0) {
                    Log.v("ist in zweite entries.size() > 0", String.valueOf("ja"));
//                SimpleEntry test_entry =  (SimpleEntry) groups.get(0);
//                Log.v("fragment log", String.valueOf(test_entry.getTitle()));
//
//                ArrayAdapter groupadapter;
//                groupadapter = new ArrayAdapter<String>(getActivity(), android.R.layout. simple_list_item_1, Collections.singletonList(test_entry.getTitle()));
//                setListAdapter(groupadapter);

                    ArrayList<String> entries_names = new ArrayList<String>();

                    for (int i = 0; i < entries.size(); i++) {
                        SimpleEntry temp = (SimpleEntry) entries.get(i);
                        String entry_name = temp.getTitle();
                        entries_names.add(entry_name);
                    }


                    //groupadapter.addAll(entries_names);
                    groupadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, entries_names);
                    //setListAdapter(groupadapter);
                }
            }

            setListAdapter(groupadapter);






        }
        //else { Log.v("ies ist", "leer"); }

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

        //FragmentManager fragmentManager = getParentFragmentManager();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
                                    long id) {


                if (getArguments() != null) {
                    mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
                    ArrayList<Parcelable> entries = getArguments().getParcelableArrayList("entries");
                    ArrayList<Parcelable> groups = getArguments().getParcelableArrayList("groups");



                    //Log.v("entries size",    String.valueOf(entries.size()));

                    groups.get(0);
                    //groups.get(1);
                    groups.size();
                    Log.v("groups.get(0);",    String.valueOf(groups.get(0)));
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
                            //bundle.putString("entries", test_entry.getTitle());


                            fragmentManager.beginTransaction()
                                    .replace(R.id.nav_host_fragment_content_main, entry_fragment).addToBackStack(null)
                                    .commit();
                        }
                    else if (grp_size > 0) {

                           //Group grp_toSend = (SimpleGroup) groups.get(position);
                            Group grp_toSend = (SimpleGroup) groups.get(position);
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





}