package com.muthuraj.chat.network;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.muthuraj.chat.privatechat.ChatAdapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Muthuraj on 1/11/2015.
 */
public class FetchMessagePublic extends AsyncTask<String, Void, String> {
    private RecyclerView recyclerView;
    private Context context;



    public FetchMessagePublic(Context context, RecyclerView listView) {
        this.context = context;
        this.recyclerView = listView;

    }

    protected void onPreExecute() {


    }

    @Override
    protected String doInBackground(String... arg0) {
        try {

            String link = (String) arg0[0];



            URL url = new URL(link);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);

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
        List<String > messages = new ArrayList<>();
        List<String > names = new ArrayList<>();

        String[] users = result.split("-");
        for (String user : users) {

//                    user = user.trim();
//                    String name = user.substring(0, user.length() - 1);

            String [] message = user.split(":");
            names.add(message[0].trim());
            messages.add(message[1].trim());
        }
        recyclerView.setAdapter(new ChatAdapter(context, names, messages));

//        String[] users = result.split("-");
//        ArrayAdapter<String> adapter= new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, users);
//
//        listView.setAdapter(adapter);


    }

}

