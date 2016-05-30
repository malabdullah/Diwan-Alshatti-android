package com.malabdullah.alshattidiwan;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class Add_Member_Data_Upload extends AsyncTask<String,Void,String> {

    String url_address;
    Context context;
    private String name;
    private String date;
    private String comments;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    ProgressDialog progressDialog;

    public Add_Member_Data_Upload(Context context, FragmentManager fragmentManager, String url) {
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.url_address = url;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Uploading data");
        progressDialog.setMessage("Please Wait ..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            URL url = new URL(url_address);

            name = params[0];
            date = params[1];
            comments = params[2];
            String type = params[3];

            if (type.equals("add")){

                HttpURLConnection httpURLConnection = (HttpURLConnection)  url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

                String post_data = URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"
                        +URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(date,"UTF-8")+"&"
                        +URLEncoder.encode("comments","UTF-8")+"="+URLEncoder.encode(comments,"UTF-8");

                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));

                String result = "";
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    result+=line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return result;

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        progressDialog.dismiss();

        if (result.contains("success")){
            fragmentTransaction = fragmentManager.beginTransaction();
            admin_fragment admin = new admin_fragment();
            fragmentTransaction.replace(R.id.frame_layout,admin);
            fragmentTransaction.commit();

            Toast.makeText(context,"record added successfully!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            //show the user the error
            Toast.makeText(context,result,Toast.LENGTH_SHORT).show();
        }
    }
}
