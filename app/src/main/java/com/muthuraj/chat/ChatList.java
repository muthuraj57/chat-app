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
import java.util.Arrays;

/**
 * Created by Muthuraj on 12/29/2014.
 */
public class ChatList extends AsyncTask<String,Void, String> {
private Context context;
private ListView listView;


public ChatList(Context context, ListView listView)
        {
        this.context = context;
        this.listView = listView;
        }



@Override
protected void onPreExecute() {
        super.onPreExecute();

        }

@Override
protected String doInBackground(String... arg) {
        String list = null;
        try {
        //Get users from server through php
        String username = (String)arg[0];
        String link = (String)arg[1];
        String data = "username="+username;
        URL url = new URL(link);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        list = reader.readLine();
        } catch (Exception e) {

        }

        return list;
        }

@Override
protected void onPostExecute(String s) {
        String[] users = s.split("-");
        users = Arrays.copyOfRange(users, 1, users.length);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, users);

        listView.setAdapter(adapter);


        }
        }


