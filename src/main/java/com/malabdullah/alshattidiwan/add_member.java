package com.malabdullah.alshattidiwan;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class add_member extends Fragment {


    EditText et_name;
    EditText et_date;
    EditText et_comments;

    Calendar getDate = Calendar.getInstance();

    String date;
    String comments;

    public add_member() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_member, container, false);

        et_name = (EditText) view.findViewById(R.id.et_name);
        et_date = (EditText) view.findViewById(R.id.et_date);
        et_comments = (EditText) view.findViewById(R.id.et_comments);

        et_date.setInputType(InputType.TYPE_NULL);

        view.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (et_name.getText() == null){
                    Toast.makeText(view.getContext(),"Please Fill in The Name Field",Toast.LENGTH_SHORT).show();
                }
                else {
                    String name = et_name.getText().toString();
                    if (et_date.getText().toString() != null)
                    { date = et_date.getText().toString();}
                    else
                    { date = "0000-00-00";}
                    if (et_comments.getText().toString() != null)
                    { comments = et_comments.getText().toString();}
                    else
                    { comments = "";}

                    Add_Member_Data_Upload add_member_data_upload = new Add_Member_Data_Upload(
                            getContext(),
                            getFragmentManager(),
                            "http://www.malabdullah.com/diwan/insert_json.php");
                    add_member_data_upload.execute(name,date,comments,"add");
                }

            }
        });

        view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                admin_fragment admin = new admin_fragment();
                fragmentTransaction.replace(R.id.frame_layout,admin);
                fragmentTransaction.commit();

            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                getDate.set(Calendar.YEAR,i);
                getDate.set(Calendar.MONTH,i1);
                getDate.set(Calendar.DAY_OF_MONTH,i2);

                updateDateField();
            }
        };

        et_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if (b) {
                    new DatePickerDialog(
                            getContext(),
                            date,
                            getDate.get(Calendar.YEAR),
                            getDate.get(Calendar.MONTH),
                            getDate.get(Calendar.DAY_OF_MONTH))
                            .show();
                }
            }
        });

        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(
                        getContext(),
                        date,
                        getDate.get(Calendar.YEAR),
                        getDate.get(Calendar.MONTH),
                        getDate.get(Calendar.DAY_OF_MONTH))
                        .show();
            }
        });

        et_name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                }
            }
        });

        et_comments.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(),0);
                }
            }
        });

        return view;
    }

    private void updateDateField(){
        String dateFormat = "yyyy/MM/dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);

        et_date.setText(simpleDateFormat.format(getDate.getTime()));
    }
}
