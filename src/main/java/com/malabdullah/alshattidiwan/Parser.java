package com.malabdullah.alshattidiwan;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.SimpleArrayMap;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.AbstractOwnableSynchronizer;

public class Parser extends AsyncTask<Void,Integer,Integer> {

    Context c;
    ListView listView;
    String data;

    ArrayList<HashMap<String,String>> membersList = new ArrayList<HashMap<String,String>>();

    ProgressDialog progressDialog;

    public Parser(Context c, ListView listView, String data) {
        this.c = c;
        this.listView = listView;
        this.data = data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(c);
        progressDialog.setTitle("Fetching data");
        progressDialog.setMessage("Please Wait ..");
        progressDialog.show();
    }

    @Override
    protected Integer doInBackground(Void... voids) {
        return this.parsingData();
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

        if (integer == 1){
            SimpleAdapter simpleAdapter = new SimpleAdapter(
                    c,
                    membersList,
                    android.R.layout.simple_list_item_2,
                    new String[]{"name","date"},
                    new int []{android.R.id.text1,android.R.id.text2}
            );
            listView.setAdapter(simpleAdapter);

        }else{

        }

        progressDialog.dismiss();
    }

    private Integer parsingData(){

        try {
            JSONObject first_json_object = null;
            first_json_object = new JSONObject(data);
            JSONArray jsonArray = first_json_object.getJSONArray("names");
            JSONObject jsonObject = null;
            Model dataModel = new Model();

            for (int i=0; i<jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);

                dataModel.setId(jsonObject.getInt("id"));
                dataModel.setName(jsonObject.getString("name"));
                dataModel.setDate(jsonObject.getString("date"));
                dataModel.setComments(jsonObject.getString("comments"));

                HashMap<String,String> hashMap = new HashMap<String,String>();
                hashMap.put("id",String.valueOf(dataModel.getId()));
                hashMap.put("name",dataModel.getName().toString());
                hashMap.put("date",dataModel.getDate().toString());
                hashMap.put("comments",dataModel.getComments().toString());

                membersList.add(hashMap);
            }

            return 1;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
