package com.muthuraj.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Muthuraj on 12/28/2014.
 */
public class OnlineUsers extends Activity {
    ListView listView;

    private boolean checkNetwork(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getActiveNetworkInfo()!=null && connectivityManager.getActiveNetworkInfo().isAvailable() && connectivityManager.getActiveNetworkInfo().isConnected()){
            return true;
        }else {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_users);

        /*ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting user list");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(true);
        progressDialog.show();*/

        listView = (ListView)findViewById(R.id.onlineUsersList);
        if(checkNetwork())
        new FetchUsers(this, listView).execute("http://chat.muthuraj.tk/android_online.php");
        else
            Toast.makeText(this, "No network available", Toast.LENGTH_SHORT).show();
        //progressDialog.dismiss();

        //On clicking name in chalist, goto chat activity with receiver name
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = (String)parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("receiver", data);
                startActivity(intent);

                //Toast.makeText(getApplicationContext(),data, Toast.LENGTH_SHORT).show();
            }
        });
    }
    /*@Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked

       String receiver = l.getItemAtPosition(position).toString();
    }*/
}
