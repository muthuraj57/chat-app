package com.muthuraj.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by Muthuraj on 12/29/2014.
 */
public class PrivateChat extends Activity {
    SharedPreferences sharedPreferences;
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
        sharedPreferences = getSharedPreferences(SignInActivity.Chat, Context.MODE_PRIVATE);
        setContentView(R.layout.private_chat);
        listView = (ListView)findViewById(R.id.privateChatList);
        if(checkNetwork())
        new ChatList(this, listView).execute(sharedPreferences.getString(SignInActivity.name, "sharedprefFailure") ,"http://chat.muthuraj.tk/android_private_chat_list.php");
        else
            Toast.makeText(this, "No network available", Toast.LENGTH_SHORT).show();


        //On clicking name in chalist, goto chat activity with receiver name
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = (String)parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("receiver", data);
                if(checkNetwork())
                startActivity(intent);
                else
                    Toast.makeText(getApplicationContext(), "No network available", Toast.LENGTH_SHORT).show();

                //Toast.makeText(getApplicationContext(),data, Toast.LENGTH_SHORT).show();
            }
        });

        getActionBar().setDisplayHomeAsUpEnabled(true);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_online, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==R.id.active)
        {
            if(checkNetwork()) {
                Intent intent = new Intent(this, OnlineUsers.class);
                startActivity(intent);
            }else
                Toast.makeText(this, "No network available", Toast.LENGTH_SHORT).show();
            return true;
        }
        else if (item.getItemId() == R.id.about)
        {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            return true;
        }
        else if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
