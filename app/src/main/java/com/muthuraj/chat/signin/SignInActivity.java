package com.muthuraj.chat.signin;

import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.muthuraj.chat.About;
import com.muthuraj.chat.R;
import com.muthuraj.chat.SelectChat;
import com.muthuraj.chat.util.GenerateUrl;
import com.muthuraj.chat.util.RequestProcessor;
import com.muthuraj.chat.util.RequestProcessorListener;
import com.muthuraj.chat.util.Utils;

import static android.widget.Toast.LENGTH_SHORT;


public class SignInActivity extends AppCompatActivity {

    public static final String CHAT = "CHAT";
    public static final String NAME = "nameKey";
    public static final String PASS = "passwordKey";
    private EditText usernameField, passwordField;
    private ProgressDialog dialog;

    private SharedPreferences sharedPreferences;

    private boolean checkNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isAvailable() && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        setTitle("Sign In");
        usernameField = (EditText) findViewById(R.id.username);
        passwordField = (EditText) findViewById(R.id.password);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Singing In...");
        dialog.setIndeterminate(true);

        if (checkNetwork()) {
            sharedPreferences = getSharedPreferences(CHAT, Context.MODE_PRIVATE);
            if (sharedPreferences.contains(NAME)) {
                if (sharedPreferences.contains(PASS)) {
                    dialog.show();
                    signInUser(sharedPreferences.getString(NAME, null), sharedPreferences.getString(PASS, null));
//                    new SignIn(this).execute(sharedPreferences.getString(NAME, null), sharedPreferences.getString(PASS, null));
                }
            }
        } else {
            Toast.makeText(this, "No network available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_in, menu);
        return true;
    }

    public void login(View view) {

        if (checkNetwork()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String username = usernameField.getText().toString().trim();
            String password = passwordField.getText().toString();
            if (username.trim().equals("")) {
                Toast.makeText(this, "Please enter Username", Toast.LENGTH_SHORT).show();
            } else if (password.trim().equals("")) {
                Toast.makeText(this, "Please enter Password", Toast.LENGTH_SHORT).show();
            } else {
                dialog.show();
                editor.putString(NAME, username.trim());
                editor.putString(PASS, password);
                editor.apply();
                signInUser(username, password);
//                new SignIn(this).execute(username, password);
            }
        } else {
            Toast.makeText(this, "No network available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (dialog != null && !dialog.isShowing()) {
            finish();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
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

    private void signInUser(String username, String password) {
        username = username.trim();
        String url = GenerateUrl.getSignInUrl(username, password);
        RequestProcessor requestProcessor = new RequestProcessor(this, Request.Method.POST);

        requestProcessor.setListener(new RequestProcessorListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("Signing", "onSuccess: " + response);
                SharedPreferences.Editor editor = getSharedPreferences(SignInActivity.CHAT, Context.MODE_PRIVATE).edit();
                editor.putString(SignInActivity.NAME, response);
                editor.apply();
                Intent intent = new Intent(SignInActivity.this, SelectChat.class);
                Toast.makeText(SignInActivity.this, "Welcome " + response, LENGTH_SHORT).show();
                dialog.dismiss();
                startActivity(intent);
            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onError(VolleyError error) {
                dialog.dismiss();
                String errorMessage = Utils.getErrorMessage(error);
                if (errorMessage != null) {
                    Log.d("Signing", "onError: " + errorMessage);
                    Toast.makeText(SignInActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignInActivity.this, "Unknown error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        requestProcessor.execute(url);
    }


}
