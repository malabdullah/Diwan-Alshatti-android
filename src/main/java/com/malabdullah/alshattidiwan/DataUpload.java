package com.malabdullah.alshattidiwan;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;
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
import java.net.URL;
import java.net.URLEncoder;

public class DataUpload extends AsyncTask<String,Void,String> {


    Context context;
    boolean checked;
    public static final String PREF = "login.config";
    private String username;
    private String password;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    View view;
    TextView error;
    ProgressDialog progressDialog;

    public DataUpload (Context c,boolean check,FragmentManager fm,View v){
        this.context = c;
        this.checked = check;
        this.fragmentManager = fm;
        this.view = v;

        error = (TextView) view.findViewById(R.id.tv_error);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Fetching data");
        progressDialog.setMessage("Please Wait ..");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        try {
            URL url = new URL("http://www.malabdullah.com/diwan/json_login.php");
            String type = params[2];

            if (type.equals("login"))
            {
                username = params[0];
                password = params[1];

                HttpURLConnection httpURLConnection = (HttpURLConnection)  url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

                String post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");

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
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result.contains("success")){
            Toast.makeText(context,result,Toast.LENGTH_SHORT).show();

            if (checked){
                SharedPreferences sharedPreferences = this.context.getSharedPreferences(PREF,Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("username",username).putString("password",password);
                editor.apply();
            }

            fragmentTransaction = fragmentManager.beginTransaction();
            admin_fragment admin = new admin_fragment();
            fragmentTransaction.replace(R.id.frame_layout,admin);
            fragmentTransaction.commit();
        }
        else
        {
            error.setText("Invalid username or password");
            error.setVisibility(View.VISIBLE);
        }
        progressDialog.dismiss();
    }
}
