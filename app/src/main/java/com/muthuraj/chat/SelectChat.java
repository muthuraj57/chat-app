package com.muthuraj.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.muthuraj.chat.privatechat.PrivateChat;
import com.muthuraj.chat.signin.SignInActivity;

/**
 * Created by Muthuraj on 12/27/2014.
 */
public class SelectChat extends AppCompatActivity {
    SharedPreferences sharedPreferences;

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
        setContentView(R.layout.select_chat);

        setTitle("Select CHAT");

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.logout)
        {
            if(checkNetwork()) {
                sharedPreferences = getSharedPreferences(SignInActivity.CHAT, Context.MODE_PRIVATE);
                new Logout(this).execute(sharedPreferences.getString(SignInActivity.NAME, null), "http://muthuraj.xyz/chat/android_logout.php");
                //startActivity(new Intent(this, SignIn.class));
            }else
                Toast.makeText(this, "No network available", Toast.LENGTH_SHORT).show();
        }

        else if(item.getItemId() == android.R.id.home)
        {
            finish();
        }
        else if(item.getItemId() == R.id.about)
        {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void gotoPublicChat(View view)
    {
        if(checkNetwork()) {
            Intent intent = new Intent(this, PublicChat.class);
            startActivity(intent);
        }else
            Toast.makeText(this, "No network available", Toast.LENGTH_SHORT).show();
    }
    public void gotoPrivateChat(View view)
    {
        if(checkNetwork()) {
            Intent intent = new Intent(this, PrivateChat.class);
            startActivity(intent);
        }else
            Toast.makeText(this, "No network available", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
