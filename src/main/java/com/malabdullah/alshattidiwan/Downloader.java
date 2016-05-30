package com.malabdullah.alshattidiwan;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Downloader extends AsyncTask<Void,Void,String> {

    Context c;
    String address;
    ListView listView;

    ProgressDialog progressDialog;

    public Downloader(Context c, String address, ListView listView) {
        this.c = c;
        this.address = address;
        this.listView = listView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(c);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait ..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        return DownloadData();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        progressDialog.dismiss();

        if (s != null)
        {
            Parser parser = new Parser(c,listView,s);
            parser.execute();
        }
        else
        {
            Toast.makeText(c,"Unable To download data!, please check your connection",Toast.LENGTH_SHORT).show();
        }
    }

    private String DownloadData (){
        InputStream inputStream = null;
        String line;

        try{

            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            inputStream = new BufferedInputStream(connection.getInputStream());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();

            if (bufferedReader != null){

                while((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line + "\n");
                }

            }else{
                return null;
            }

            return stringBuffer.toString();


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                try {

                    inputStream.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
