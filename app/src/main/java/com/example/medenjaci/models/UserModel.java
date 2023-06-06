package com.example.medenjaci.models;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

public class UserModel extends AsyncTask<String, Void, Void> {
    private Context context;


    public UserModel(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(String... params) {
        String type = params[0];

        switch (type) {
            case "get_user_by_username":
                get_user_by_username(params[1]);
                break;
        }

        return null;
    }

    private void get_user_by_username(String username) {
        String my_url = "http://192.168.1.3/get_user_by_username.php";

        URL url = null;
        try {
            url = new URL(my_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            bufferedWriter.write(post_data);
            bufferedWriter.flush();

            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));

            String result = "";
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }

            Log.i("myInfo", result);
            Log.i("myInfo", "gotovo");
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }



    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

}
