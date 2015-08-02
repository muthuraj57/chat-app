package com.muthuraj.gossip;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Muthuraj on 12/29/2014.
 */
public class ChatActivity extends Activity {

    ListView listView;
    SharedPreferences sharedPreferences;
    String receiver;
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
        sharedPreferences = getSharedPreferences(SignInActivity.Gossip, Context.MODE_PRIVATE);
        setContentView(R.layout.chat_activity);

        receiver = getIntent().getExtras().getString("receiver");

        setTitle(receiver);

        Toast.makeText(this, receiver, Toast.LENGTH_SHORT).show();
        sendButton = (Button)findViewById(R.id.buttonSend);
        editText = (EditText)findViewById(R.id.chatText);

        //Disable soft keyboard from appearing by default
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //provide focus to editText
        editText.requestFocus();

        listView = (ListView) findViewById(R.id.chatListView);

       // new FetchMsg(this, listView).execute(sharedPreferences.getString(SignInActivity.name, "senderShareDefFailure"),receiver,"http://gosssip.tk/android_fetch_msg.php");

        repeat();

        //On Clicking Send Button
        sendButton.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View view)
            {
                view.startAnimation(sendClick);

                //To avoid sending empty messages
                if (!editText.getText().toString().equals("")) {
                    if(checkNetwork()) {
                        new SendMessage(getApplicationContext()).execute(sharedPreferences.getString(SignInActivity.name, "sendMsgSharedFailure"), receiver, editText.getText().toString(), "http://gosssip.tk/android_send_msg.php");
                        //To clear Text Field
                        editText.setText("");
                    }else
                        Toast.makeText(getApplicationContext(), "No network available", Toast.LENGTH_SHORT).show();

                }

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
                new FetchMsg(getBaseContext(), listView).execute(sharedPreferences.getString(SignInActivity.name, "senderShareDefFailure"),receiver,"http://gosssip.tk/android_fetch_msg.php");
                else
                    Toast.makeText(getBaseContext(), "No network available", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){

            }
        }
    }, 0, 5000);
    }

    /*public void sendMsg(){


    }*/

}

