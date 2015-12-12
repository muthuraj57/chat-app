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
import android.widget.EditText;
import android.widget.Toast;


public class SignInActivity extends Activity {

    public static final String Chat = "Chat";
    public static final String name = "nameKey";
    public static final String pass = "passwordKey";
    private EditText usernameField, passwordField;

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
        setContentView(R.layout.sign_in);
        setTitle("Sign In");
        usernameField = (EditText)findViewById(R.id.username);
        passwordField = (EditText)findViewById(R.id.password);


    }

   @Override
    protected void onResume() {
        if(checkNetwork()) {
            sharedPreferences = getSharedPreferences(Chat, Context.MODE_PRIVATE);
            if (sharedPreferences.contains(name)) {
                if (sharedPreferences.contains(pass)) {
                    new SignIn(this).execute(sharedPreferences.getString(name, null), sharedPreferences.getString(pass, null));
                }
            }
        }
       else {
            Toast.makeText(this, "No network available", Toast.LENGTH_SHORT).show();
        }
       super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
        return true;
    }

    public void login(View view) {

        if(checkNetwork()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString();
            if (username.trim().equals("")) {
                Toast.makeText(this, "Please enter Username", Toast.LENGTH_SHORT).show();
            } else if (password.trim().equals("")) {
                Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
            } else {
                editor.putString(name, username);
                editor.putString(pass, password);
                editor.commit();
                new SignIn(this).execute(username, password);
            }
        }
        else {
            Toast.makeText(this, "No network available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
