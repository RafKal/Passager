package com.example.passager;

import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.linguafranca.pwdb.kdbx.simple.SimpleEntry;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link entry#newInstance} factory method to
 * create an instance of this fragment.
 */
public class entry extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public entry() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment entry.
     */
    // TODO: Rename and change types and number of parameters
    public static entry newInstance(String param1, String param2) {
        entry fragment = new entry();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);

            //Bundle data = getArguments();
            //String name = data.getString("title");









        }








    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_entry, container, false);



        Button back =  (Button) rootView.findViewById(R.id.back);

        FloatingActionButton backs = rootView.findViewById(R.id.back_button);
        backs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                FragmentManager fm = getActivity().getSupportFragmentManager();
                //FragmentManager fm = getParentFragmentManager();
                fm = getParentFragmentManager();
                //fm.popBackStack (null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                fm.popBackStack();





                Toast.makeText(getActivity(), "hello", Toast.LENGTH_LONG).show();
            }
        });

        Bundle data = getArguments();
        String name = data.getString("title");

        TextView name_text = rootView.findViewById(R.id.entry_Name);
        name_text.setText(name);




        // Inflate the layout for this fragment
        return rootView;



    }
}
