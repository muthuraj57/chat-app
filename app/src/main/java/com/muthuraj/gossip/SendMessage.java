package com.muthuraj.gossip;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Muthuraj on 1/2/2015.
 */
public class SendMessage extends AsyncTask<String, Void, String> {

private Context context;
String sender, receiver, msg;

public SendMessage(Context context) {
        this.context = context;

        }

protected void onPreExecute() {

        }

@Override
protected String doInBackground(String... arg0) {
        try {
        sender = (String) arg0[0];
        receiver = (String) arg0[1];
        msg = (String) arg0[2];
        String link = arg0[3];
        String data = "sender"+"="+sender; //URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
        data += "&"+"receiver"+"="+receiver;
        data += "&"+"msg"+"="+msg;

        URL url = new URL(link);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String stat = reader.readLine();

        return stat;
        } catch (Exception e) {
        return new String("Exception: " + e.getMessage());
        }
        }
@Override
protected void onPostExecute(String result)
        {
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();

        }

        }
