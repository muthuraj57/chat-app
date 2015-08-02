package com.muthuraj.gossip;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.SignalStrength;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import static android.widget.Toast.LENGTH_SHORT;

/**
 * Created by Muthuraj on 12/26/2014.
 */
public class SignIn extends AsyncTask<String, Void, String> {

    private Context context;
    private String stat=null;

    public SignIn(Context context) {
        this.context = context;

    }

    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String username = (String) arg0[0];
            String password = (String) arg0[1];
            String link = "http://gosssip.tk/android_signin.php";
            String data = "username"+"="+username; //URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&"+"password"+"="+password; //URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
           /* StringBuilder sb = new StringBuilder();
            String line = null;

            //Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }
            return sb.toString();*/
            stat = reader.readLine();
            return stat;
        } catch (IOException e) {
            return new String("Exception: " + e.getMessage());
        }
    }
    @Override
    protected void onPostExecute(String result)
    {
        if(result.equals("Success"))
        {
            //Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(context, SelectChat.class);
            Toast.makeText(context, "Login Success", LENGTH_SHORT).show();
            context.startActivity(intent);

        }
        else
            Toast.makeText(context, result, LENGTH_SHORT).show();
    }

}