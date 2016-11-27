package com.muthuraj.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.muthuraj.chat.signin.SignInActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Muthuraj on 12/29/2014.
 */
public class Logout extends AsyncTask<String, Void, String> {

    private Context context;

    SharedPreferences sharedPreferences;

    public Logout(Context context) {
        this.context = context;


    }

    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String username = (String) arg0[0];
            String link = (String) arg0[1];

            String data = "usrname"+"="+username; //URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");

            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            return reader.readLine();
        } catch (Exception e) {
            return "Exception: " + e.getMessage();
        }
    }
    @Override
    protected void onPostExecute(String result)
    {

        sharedPreferences = context.getSharedPreferences(SignInActivity.CHAT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor= sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Toast.makeText(context, result+" logged out", Toast.LENGTH_SHORT).show();
        context.startActivity(new Intent(context, SignInActivity.class));


    }

}
