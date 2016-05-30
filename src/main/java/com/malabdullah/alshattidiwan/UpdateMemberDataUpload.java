package com.malabdullah.alshattidiwan;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class UpdateMemberDataUpload extends AsyncTask<String,Void,String> {

    Context context;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    View view;
    ProgressDialog progressDialog;
    private String url_address;
    private String id,name,date,comments;

    public UpdateMemberDataUpload(Context c,String url,FragmentManager fm){
        this.context = c;
        this.url_address = url;
        this.fragmentManager = fm;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Processing Your Request");
        progressDialog.setMessage("Please wait ..");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            URL url = new URL(url_address);
            String type = params[4];

            id = params[0];
            name = params[1];
            date = params[2];
            comments = params[3];

            HttpURLConnection httpURLConnection = (HttpURLConnection)  url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setConnectTimeout(5000);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

            String post_data = URLEncoder.encode("id","UTF-8")+"="+URLEncoder.encode(id,"UTF-8")+"&"
                    +URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"
                    +URLEncoder.encode("date","UTF-8")+"="+URLEncoder.encode(date,"UTF-8")+"&"
                    +URLEncoder.encode("comments","UTF-8")+"="+URLEncoder.encode(comments,"UTF-8")+"&"
                    +URLEncoder.encode("operation","UTF-8")+"="+URLEncoder.encode(type,"UTF-8");

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

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
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

        if (result.contains("update success"))
        {
            fragmentTransaction = fragmentManager.beginTransaction();
            admin_fragment admin = new admin_fragment();
            fragmentTransaction.replace(R.id.frame_layout,admin);
            fragmentTransaction.commit();

            Toast.makeText(context,"Update done successfully",Toast.LENGTH_LONG).show();
        }
        else if (result.contains("delete success"))
        {
            fragmentTransaction = fragmentManager.beginTransaction();
            admin_fragment admin = new admin_fragment();
            fragmentTransaction.replace(R.id.frame_layout,admin);
            fragmentTransaction.commit();

            Toast.makeText(context,"Delete done successfully",Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(context,"Error updating or deleting record!",Toast.LENGTH_LONG).show();
        }

        progressDialog.dismiss();
    }
}
