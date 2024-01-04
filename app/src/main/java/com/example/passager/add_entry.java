package com.example.passager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.passager.databinding.FragmentEntryBinding;
import com.example.passager.ui.list_Fragment;

import org.linguafranca.pwdb.kdbx.simple.SimpleEntry;
import org.linguafranca.pwdb.kdbx.simple.SimpleGroup;

import java.io.Serializable;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link add_entry#newInstance} factory method to
 * create an instance of this fragment.
 */
public class add_entry extends Fragment  {

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

    boolean is_edit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {





        View rootView = inflater.inflate(R.layout.fragment_add_entry, container, false);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        TextView title_text = rootView.findViewById(R.id.entry_Title);
        TextView username_text = rootView.findViewById(R.id.entry_username);
        TextView password_text = rootView.findViewById(R.id.entry_Password);
        TextView notes_text = rootView.findViewById(R.id.entry_Notes);
        TextView url_text = rootView.findViewById(R.id.entry_URL);

        is_edit = false;

        if (getArguments() != null){
            Bundle data = getArguments();
            title_text.setText(data.getString("title"));
            username_text.setText(data.getString("username"));
            password_text.setText(data.getString("password"));
            notes_text.setText(data.getString("notes"));
            url_text.setText(data.getString("URL"));
            is_edit = true;
        }



        Button back =  (Button) rootView.findViewById(R.id.entry_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fm.popBackStack();

            }
        });

        Button save_entry =  (Button) rootView.findViewById(R.id.entry_saveEntry);
        save_entry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                MainActivity Activity = ((MainActivity) getActivity());


                String title =  title_text.getText().toString();
                String password =  password_text.getText().toString();
                String notes =  notes_text.getText().toString();
                String url =  url_text.getText().toString();
                String username =  username_text.getText().toString();

                //String[] Data = {title, password, notes, url};


                SimpleEntry new_entry = Activity.database.newEntry();
                new_entry.setTitle(title);
                new_entry.setNotes(notes);
                new_entry.setUsername(username);
                new_entry.setPassword(password);
                new_entry.setUrl(url);
                //SimpleGroup group = Activity.database.getRootGroup().getGroups().get(0);
                //group.addEntry(new_entry);

                Log.v("kommt at", String.valueOf(Activity.current_group));
                //Activity.current_group.addEntry(new_entry);
                if (is_edit){
                    Bundle data = getArguments();
                    UUID uuid =  UUID.fromString(data.getString("UUID"));
                    Activity.edit_entry(new_entry, uuid);
                }
                else {
                    Activity.add_entry(new_entry);
                }

                fm.popBackStack();

            }
        });

        Button gen_password =  (Button) rootView.findViewById(R.id.entry_gen_password);

        gen_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //fm.saveBackStack("b_save");

               Intent gen_newPW = new Intent(getContext(), gen_password.class);
               startActivityForResult(gen_newPW, 100);


            }
        });



        // Inflate the layout for this fragment
        return rootView;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data); comment this unless you want to pass your result to the activity.

        if (requestCode == 100) {
            if(resultCode == Activity.RESULT_OK & data!= null){

                String password = data.getStringExtra("password");


                View rootView = getView();
                TextView password_text = rootView.findViewById(R.id.entry_Password);
                password_text.setText(password);

                FragmentManager fm = getActivity().getSupportFragmentManager();
                //fm.restoreBackStack("b_save");

            }
        }
    }

}
