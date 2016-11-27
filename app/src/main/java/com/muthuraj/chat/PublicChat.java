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
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.muthuraj.chat.network.FetchMessagePublic;
import com.muthuraj.chat.network.SendMessagePublic;
import com.muthuraj.chat.signin.SignInActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Muthuraj on 12/27/2014.
 */
public class PublicChat extends AppCompatActivity {

    ListView listView;
    SharedPreferences sharedPreferences;
    Button sendButton;
    EditText editText;
    private AlphaAnimation sendClick = new AlphaAnimation(1F, 0.8F);

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
        setContentView(R.layout.chat_activity);

        sharedPreferences = getSharedPreferences(SignInActivity.CHAT, Context.MODE_PRIVATE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle(sharedPreferences.getString(SignInActivity.NAME, "sendMsgSharedFailure"));

        sendButton = (Button)findViewById(R.id.buttonSend);
        editText = (EditText)findViewById(R.id.chatText);

        //Disable soft keyboard from appearing by default
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //provide focus to editText
        editText.requestFocus();

        listView = (ListView) findViewById(R.id.chatListView);

        repeat();

        //On Clicking Send Button
        sendButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                view.startAnimation(sendClick);

                //To avoid sending empty messages
                if (!editText.getText().toString().equals("")) {
                    if(checkNetwork())
                    new SendMessagePublic(getApplicationContext()).execute(sharedPreferences.getString(SignInActivity.NAME, "sendMsgSharedFailure"), editText.getText().toString());
                       else
                        Toast.makeText(getApplicationContext(), "No network available", Toast.LENGTH_SHORT).show();

                }
                //To clear Text Field
                editText.setText("");
            }
        });
    }

    private void repeat()
    {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                try{
                    if(checkNetwork())
                    new FetchMessagePublic(getBaseContext(), listView).execute("http://muthuraj.xyz/chat/android_fetch_public_msg.php");
                    else
                        Toast.makeText(getBaseContext(), "No network available", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){

                }
            }
        }, 0, 5000);
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
