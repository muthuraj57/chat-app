package com.muthuraj.chat.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Muthuraj on 12/28/2014.
 *
 * Used as a template. Called to get users list, active users from server
 */
public class FetchUsers extends AsyncTask<String,Void, String> {
    private Context context;
    private ListView listView;


    public FetchUsers(Context context, ListView listView)
    {
        this.context = context;
        this.listView = listView;
    }



    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... params) {
        String data = null;
        try {
            //Get users from server through php
            String link = params[0];
            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            data = reader.readLine();
        } catch (Exception e) {

        }

        return data;
    }

    @Override
    protected void onPostExecute(String s) {
        List<String > usernames = new ArrayList<>();
        String[] users = s.split("-");
        users = Arrays.copyOfRange(users, 1, users.length);

        for (String user : Arrays.copyOfRange(users, 1, users.length)) {
            user = user.trim();
            if (!usernames.contains(user)){
                Log.d("FetchUsers", "onPostExecute: "+user+" length: "+user.length());
                usernames.add(user);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, usernames);

        listView.setAdapter(adapter);


    }
}

