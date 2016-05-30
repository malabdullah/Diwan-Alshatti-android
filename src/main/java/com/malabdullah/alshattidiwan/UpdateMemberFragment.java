package com.malabdullah.alshattidiwan;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UpdateMemberFragment extends Fragment {

    EditText et_name,et_date,et_comments;
    Button btn_update,btn_delete,btn_cancel;
    String name,date,comments;
    int id;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    Calendar getDate = Calendar.getInstance();

    public UpdateMemberFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_update_member, container, false);

        btn_update = (Button) view.findViewById(R.id.btn_update);
        btn_delete = (Button) view.findViewById(R.id.btn_delete);
        btn_cancel = (Button) view.findViewById(R.id.btn_cancel);


        final DatePickerDialog.OnDateSetListener dateCalendar = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                getDate.set(Calendar.YEAR,i);
                getDate.set(Calendar.MONTH,i1);
                getDate.set(Calendar.DAY_OF_MONTH,i2);

                updateDateField();
            }
        };

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!et_name.getText().toString().isEmpty()) {
                    UpdateMemberDataUpload updateMemberDataUpload = new UpdateMemberDataUpload(
                            getContext(),
                            "http://malabdullah.com/diwan/update_delete_json.php",
                            getFragmentManager());
                    updateMemberDataUpload.execute(
                            String.valueOf(id),
                            et_name.getText().toString(),
                            et_date.getText().toString(),
                            et_comments.getText().toString(),
                            "update"
                    );
                }
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are You Sure you want to delete this record?")
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                UpdateMemberDataUpload updateMemberDataUpload = new UpdateMemberDataUpload(
                                        getContext(),
                                        "http://malabdullah.com/diwan/update_delete_json.php",
                                        getFragmentManager());
                                updateMemberDataUpload.execute(
                                        String.valueOf(id),
                                        et_name.getText().toString(),
                                        et_date.getText().toString(),
                                        et_comments.getText().toString(),
                                        "delete"
                                );

                                dialogInterface.dismiss();
                            }
                        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
                builder.show();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                admin_fragment admin = new admin_fragment();
                fragmentTransaction.replace(R.id.frame_layout,admin);
                fragmentTransaction.commit();
            }
        });

        et_name = (EditText) view.findViewById(R.id.et_name);
        et_date = (EditText) view.findViewById(R.id.et_date);
        et_comments = (EditText) view.findViewById(R.id.et_comments);

        et_date.setInputType(InputType.TYPE_NULL);

        et_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    new DatePickerDialog(
                            getContext(),
                            dateCalendar,
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
                        dateCalendar,
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

        name = getArguments().getString("name");
        date = getArguments().getString("date");
        comments = getArguments().getString("comments");
        id = getArguments().getInt("id");

        et_name.setText(name);
        et_date.setText(date);
        et_comments.setText(comments);

        return view;
    }

    private void updateDateField(){
        String dateFormat = "yyyy/MM/dd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);

        et_date.setText(simpleDateFormat.format(getDate.getTime()));
    }

}
