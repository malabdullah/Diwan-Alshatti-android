package com.malabdullah.alshattidiwan;


import android.animation.TypeConverter;
import android.content.ClipData;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

public class admin_fragment extends Fragment {

    View view;
    ListView listView_members;
    String url = "http://www.malabdullah.com/diwan/JSONnames.php";

    public static final String PREF = "login.config";

    public admin_fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_admin_fragment, container, false);

        setHasOptionsMenu(true);
        listView_members = (ListView) view.findViewById(R.id.lv_members);
        listView_members.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                TextView temp = (TextView) view.findViewById(android.R.id.text1);
                String t = temp.getText().toString();

                Map<String,String> item = (Map<String, String>) adapterView.getItemAtPosition(i);

                //Toast.makeText(getActivity(),item.get("id"),Toast.LENGTH_LONG).show();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                Bundle bundle = new Bundle();
                bundle.putString("name",item.get("name"));
                bundle.putString("date",item.get("date"));
                bundle.putString("comments",item.get("comments"));
                bundle.putInt("id", Integer.parseInt(item.get("id")));

                UpdateMemberFragment updateMemberFragment = new UpdateMemberFragment();
                updateMemberFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.frame_layout,updateMemberFragment);
                fragmentTransaction.commit();
            }
        });
        Downloader downloader = new Downloader(view.getContext(),url,listView_members);
        downloader.execute();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.admin_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add_member){

            add_member add_member = new add_member();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout,add_member);
            fragmentTransaction.commit();

        }else if (item.getItemId() == R.id.logout){
            SharedPreferences sharedPreferences = getContext().getSharedPreferences(PREF, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            login_fragment login_fragment = new login_fragment();
            fragmentTransaction.replace(R.id.frame_layout,login_fragment);
            fragmentTransaction.commit();
        }

        return super.onOptionsItemSelected(item);
    }
}
