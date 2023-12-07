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

import com.example.passager.R;
import com.example.passager.ui.placeholder.PlaceholderContent;
import com.example.passager.entry;

import org.linguafranca.pwdb.Entry;
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



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);


            ArrayList<Parcelable> groups = getArguments().getParcelableArrayList("entries");

            if (groups.size() != 0){
                SimpleEntry test_entry =  (SimpleEntry) groups.get(0);
                Log.v("fragment log", String.valueOf(test_entry.getTitle()));

                ArrayAdapter groupadapter;
                //groupadapter = new ArrayAdapter<String>(getActivity(), android.R.layout. simple_list_item_1, test_entry.getTitle());
                groupadapter = new ArrayAdapter<String>(getActivity(), android.R.layout. simple_list_item_1, Collections.singletonList(test_entry.getTitle()));
                setListAdapter(groupadapter);





                //ListView listView =   getListView();
                //ListView listView = view.findViewById(android.R.id.list);





            }






        }
        else { Log.v("ies ist", "leer"); }

        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
                                    long id) {
                Toast.makeText(getActivity(),"gello",
                        Toast.LENGTH_LONG).show();


            }
        });*/





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

        FragmentManager fragmentManager = getParentFragmentManager();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
                                    long id) {


                if (getArguments() != null) {
                    mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
                    ArrayList<Parcelable> groups = getArguments().getParcelableArrayList("entries");

                    if (groups.size() != 0){
                        SimpleEntry test_entry =  (SimpleEntry) groups.get(0);


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




                }




            }
        });


    }





}