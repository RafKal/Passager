package com.example.passager.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.passager.R;
import com.example.passager.ui.placeholder.PlaceholderContent;

import java.util.ArrayList;
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



        //ArrayAdapter arrayAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.default_groups, android.R.layout.simple_list_item_1);
        //setListAdapter(arrayAdapter);










        View view = getView();

        //ListView listView = view.findViewById(android.R.id.list);













        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
//            ArrayList<String> groups = getArguments().getStringArrayList("groups");
//            Log.v("ies ist", "NICHT leer");
//            Log.v("nummer ist", String.valueOf(groups.get(0)));
//
//
//            ArrayAdapter groupadapter;
//            groupadapter = new ArrayAdapter<String>(getActivity(), android.R.layout. simple_list_item_1, groups);
//            setListAdapter(groupadapter);

        }
        else { Log.v("ies ist", "leer"); }





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

  /*  public static list_Fragment newInstance(int someInt) {
        list_Fragment new_frag = new list_Fragment();

        Bundle args = new Bundle();
        args.putInt("someInt", someInt);
        new_frag.setArguments(args);

        return new_frag;
    }*/



}