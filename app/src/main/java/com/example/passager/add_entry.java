package com.example.passager;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.linguafranca.pwdb.kdbx.simple.SimpleEntry;
import org.linguafranca.pwdb.kdbx.simple.SimpleGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link add_entry#newInstance} factory method to
 * create an instance of this fragment.
 */
public class add_entry extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public add_entry() {
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
    public static add_entry newInstance(String param1, String param2) {
        add_entry fragment = new add_entry();
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
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View rootView = inflater.inflate(R.layout.fragment_add_entry, container, false);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        TextView name_text = rootView.findViewById(R.id.entry_Title);
        TextView password_text = rootView.findViewById(R.id.entry_Password);
        TextView notes_text = rootView.findViewById(R.id.entry_Notes);
        TextView url_text = rootView.findViewById(R.id.entry_URL);



        Button back =  (Button) rootView.findViewById(R.id.button_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.popBackStack();

            }
        });

        Button save_entry =  (Button) rootView.findViewById(R.id.button_saveEntry);
        save_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                MainActivity Activity = ((MainActivity) getActivity());






                String name = (String) name_text.getText().toString();
                String password = (String) password_text.getText().toString();
                String notes = (String) notes_text.getText().toString();
                String url = (String) url_text.getText().toString();

                String[] Data = {name, password, notes, url};


                SimpleEntry new_entry = Activity.database.newEntry();
                new_entry.setTitle(name);
                new_entry.setNotes(notes);
                new_entry.setPassword(password);
                new_entry.setUrl(url);
                //SimpleGroup group = Activity.database.getRootGroup().getGroups().get(0);
                //group.addEntry(new_entry);

                Log.v("kommt at", String.valueOf(Activity.current_group));
                Activity.current_group.addEntry(new_entry);












                fm.popBackStack();

            }
        });

       /* Bundle data = getArguments();
        String name = data.getString("title");
        String password = data.getString("password");
        String notes = data.getString("notes");

        TextView name_text = rootView.findViewById(R.id.entry_Name);
        name_text.setText(name);

        TextView password_text = rootView.findViewById(R.id.entry_Password);
        password_text.setText(password);

        TextView notes_text = rootView.findViewById(R.id.entry_Notes);
        notes_text.setText(notes);*/




        // Inflate the layout for this fragment
        return rootView;

    }
}
