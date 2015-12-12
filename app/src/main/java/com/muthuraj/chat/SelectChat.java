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
import android.widget.Toast;

/**
 * Created by Muthuraj on 12/27/2014.
 */
public class SelectChat extends Activity {
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

        setTitle("Select Chat");

        getActionBar().setDisplayHomeAsUpEnabled(true);

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
                sharedPreferences = getSharedPreferences(SignInActivity.Chat, Context.MODE_PRIVATE);
                new Logout(this).execute(sharedPreferences.getString(SignInActivity.name, null), "http://chat.muthuraj.tk/android_logout.php");
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
}
