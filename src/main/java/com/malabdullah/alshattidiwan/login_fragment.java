package com.malabdullah.alshattidiwan;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class login_fragment extends Fragment {

    View view;
    EditText et_username,et_password;
    CheckBox checkBox;
    boolean checked;
    public static final String PREF = "login.config";

    public login_fragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_login_fragment, container, false);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREF,Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username","null");
        if (!username.equals("null")) {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            admin_fragment admin_fragment = new admin_fragment();
            fragmentTransaction.replace(R.id.frame_layout,admin_fragment);
            fragmentTransaction.commit();
        }
        et_username = (EditText) view.findViewById(R.id.et_username);
        et_password = (EditText) view.findViewById(R.id.et_password);
        checkBox = (CheckBox) view.findViewById(R.id.cb_remember);
        checked = checkBox.isChecked();
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                checked = compoundButton.isChecked();
            }
        });

        view.findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login_clicked();
            }
        });

        return view;
    }

    public void login_clicked(){
        String username = et_username.getText().toString();
        String password = et_password.getText().toString();
        String type = "login";
        try {
            InputMethodManager imm =  (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

            DataUpload dataUpload = new DataUpload(getActivity(), checked, getFragmentManager(), view);
            dataUpload.execute(username, password, type);
        }
        catch (Exception e)
        {
            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_LONG);
        }
    }

}
