package com.muthuraj.chat.privatechat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.muthuraj.chat.About;
import com.muthuraj.chat.OnlineUsers;
import com.muthuraj.chat.R;
import com.muthuraj.chat.network.ChatList;
import com.muthuraj.chat.signin.SignInActivity;
import com.muthuraj.chat.util.GenerateUrl;
import com.muthuraj.chat.util.RequestProcessor;
import com.muthuraj.chat.util.RequestProcessorListener;
import com.muthuraj.chat.util.Utils;

import java.util.Arrays;

/**
 * Created by Muthuraj on 12/29/2014.
 */
public class PrivateChat extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    ListView listView;

    private boolean checkNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isAvailable() && connectivityManager.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(SignInActivity.CHAT, Context.MODE_PRIVATE);
        setContentView(R.layout.private_chat);
        listView = (ListView) findViewById(R.id.privateChatList);
        if (checkNetwork()) {
//            updateChatList(sharedPreferences.getString(SignInActivity.NAME, "").trim());
            new ChatList(this, listView).execute(sharedPreferences.getString(SignInActivity.NAME, ""), "http://muthuraj.xyz/chat/android_private_chat_list.php");
        } else {
            Toast.makeText(this, "No network available", Toast.LENGTH_SHORT).show();
        }


        //On clicking NAME in chalist, goto chat activity with receiver NAME
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String data = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                intent.putExtra("receiver", data);
                if (checkNetwork())
                    startActivity(intent);
                else
                    Toast.makeText(getApplicationContext(), "No network available", Toast.LENGTH_SHORT).show();

                //Toast.makeText(getApplicationContext(),data, Toast.LENGTH_SHORT).show();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void updateChatList(String username) {
        RequestProcessor requestProcessor = new RequestProcessor(this, Request.Method.POST);
        requestProcessor.setListener(new RequestProcessorListener() {
            @Override
            public void onSuccess(String response) {
                if (response != null) {
                    String[] users = response.split("-");
                    users = Arrays.copyOfRange(users, 1, users.length);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PrivateChat.this, android.R.layout.simple_list_item_1, users);

                    listView.setAdapter(adapter);
                }
            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onError(VolleyError error) {
                String errorMessage = Utils.getErrorMessage(error);
                if (errorMessage != null) {
                    Log.d("Chatlist", "onError: "+errorMessage);
                }
                Toast.makeText(PrivateChat.this, "Cannot get chat list", Toast.LENGTH_SHORT).show();
            }
        });
        requestProcessor.execute(GenerateUrl.getChatListUrl(username));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_online, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.active) {
            if (checkNetwork()) {
                Intent intent = new Intent(this, OnlineUsers.class);
                startActivity(intent);
            } else
                Toast.makeText(this, "No network available", Toast.LENGTH_SHORT).show();
            return true;
        } else if (item.getItemId() == R.id.about) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
