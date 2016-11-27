package com.muthuraj.chat;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Muthuraj on 12/27/2014.
 */

public class AllUsers extends AppCompatActivity {

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
        setContentView(R.layout.all_users);

        /*ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Getting user list");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setIndeterminate(true);
        progressDialog.show();*/

        listView = (ListView)findViewById(R.id.allUsersList);

        if(checkNetwork())
        new FetchUsers(this, listView ).execute("http://muthuraj.xyz/chat/android_all_users.php");
        else
            Toast.makeText(this, "No network available", Toast.LENGTH_SHORT).show();
        //progressDialog.dismiss();

        //On clicking NAME in chalist, goto chat activity with receiver NAME
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
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Do something when a list item is clicked
    }*/
}
