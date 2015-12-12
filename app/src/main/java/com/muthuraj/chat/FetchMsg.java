package com.muthuraj.chat;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Muthuraj on 12/30/2014.
 */
public class FetchMsg extends AsyncTask<String, Void, String> {
    private ListView listView;
    private Context context;



    public FetchMsg(Context context, ListView listView) {
        this.context = context;
        this.listView = listView;

    }

    protected void onPreExecute() {


    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String sender = (String) arg0[0];
            String receiver = (String) arg0[1];
            String link = (String) arg0[2];

            String data = "sender"+"="+sender; //URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&"+"receiver"+"="+receiver;

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
        String[] users = result.split("-");
        //users = Arrays.copyOfRange(users, 1, users.length);
        ArrayAdapter<String> adapter= new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, users);

        listView.setAdapter(adapter);


    }

}
