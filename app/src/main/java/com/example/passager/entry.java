package com.example.passager;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.checkerframework.checker.units.qual.A;
import org.linguafranca.pwdb.Entry;

import java.util.UUID;

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
        }

    }

    MainActivity Activity = ((MainActivity) getActivity());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_entry, container, false);

        FragmentManager fm = getActivity().getSupportFragmentManager();



        Button back =  (Button) rootView.findViewById(R.id.entry_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.popBackStack();

            }
        });

        Button delete =  (Button) rootView.findViewById(R.id.entry_delete_entry);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle data = getArguments();
                String uuid_string = data.getString("UUID");
                UUID uuid = UUID.fromString(uuid_string);
                MainActivity Activity = ((MainActivity) getActivity());
                Activity.delete_entry(uuid);
                fm.popBackStack();

            }
        });




        Bundle data = getArguments();
        String title = data.getString("title");
        String username = data.getString("username");
        String password = data.getString("password");
        String notes = data.getString("notes");
        String url = data.getString("URL");

        TextView name_text = rootView.findViewById(R.id.entry_Title);
        name_text.setText(title);

        TextView password_text = rootView.findViewById(R.id.entry_Password);
        password_text.setText(password);

        TextView notes_text = rootView.findViewById(R.id.entry_Notes);
        notes_text.setText(notes);

        TextView username_text = rootView.findViewById(R.id.entry_username);
        username_text.setText(username);

        TextView url_text = rootView.findViewById(R.id.entry_URL);
        url_text.setText(url);


        Button edit =  (Button) rootView.findViewById(R.id.entry_edit_entry);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String uuid_string = data.getString("UUID");

                Bundle data = new Bundle();
                data.putString("password", password);
                data.putString("URL",url );
                data.putString("username", username);
                data.putString("title", title);
                data.putString("notes", notes);
                data.putString("UUID", uuid_string);
                add_entry frag = new add_entry();
                frag.setArguments(data);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.nav_host_fragment_content_main, frag).addToBackStack(null)
                        .commit();


            }
        });




        // Inflate the layout for this fragment
        return rootView;

    }

    public void deleteEntry(Entry entry){
        UUID uuid = entry.getUuid();
        Activity.delete_entry(uuid);
    }
}
