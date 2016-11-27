package com.muthuraj.chat;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.muthuraj.chat.util.GenerateUrl;
import com.muthuraj.chat.util.RequestProcessor;
import com.muthuraj.chat.util.RequestProcessorListener;
import com.muthuraj.chat.util.Utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Muthuraj on 12/29/2014.
 */
public class ChatActivity extends AppCompatActivity {

    ListView listView;
    SharedPreferences sharedPreferences;
    String receiver;
    Button sendButton;
    EditText editText;
    private AlphaAnimation sendClick = new AlphaAnimation(1F, 0.8F);
    private ArrayAdapter<String> adapter;

    private Timer timer;
    private TimerTask timerTask;

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
        setContentView(R.layout.chat_activity);
        sharedPreferences = getSharedPreferences(SignInActivity.CHAT, Context.MODE_PRIVATE);

        receiver = getIntent().getExtras().getString("receiver").trim();

        setTitle(receiver);

        Toast.makeText(this, receiver, Toast.LENGTH_SHORT).show();
        sendButton = (Button) findViewById(R.id.buttonSend);
        editText = (EditText) findViewById(R.id.chatText);

        //Disable soft keyboard from appearing by default
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //provide focus to editText
        editText.requestFocus();

        listView = (ListView) findViewById(R.id.chatListView);
        adapter = new ArrayAdapter<>(ChatActivity.this, android.R.layout.simple_list_item_1, new String[]{});
        listView.setAdapter(adapter);

        // new FetchMsg(this, listView).execute(sharedPreferences.getString(SignInActivity.NAME, "senderShareDefFailure"),receiver,"http://chat.muthuraj.tk/android_fetch_msg.php");

        repeat();

        //On Clicking Send Button
        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                view.startAnimation(sendClick);

                //To avoid sending empty messages
                if (!editText.getText().toString().equals("")) {
                    if (checkNetwork()) {
                        sendMessage(sharedPreferences.getString(SignInActivity.NAME, "").trim()
                                , receiver.trim(), editText.getText().toString().trim());
//                        new SendMessage(getApplicationContext()).execute(sharedPreferences.getString(SignInActivity.NAME, "sendMsgSharedFailure"), receiver, editText.getText().toString(), "http://muthuraj.xyz/chat/android_send_msg.php");
                        //To clear Text Field
                        editText.setText("");
                    } else
                        Toast.makeText(getApplicationContext(), "No network available", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    private void sendMessage(String sender, String receiver, String message){
        RequestProcessor requestProcessor = new RequestProcessor(this, Request.Method.GET);

        requestProcessor.setListener(new RequestProcessorListener() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(ChatActivity.this, "Sent", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onError(VolleyError error) {
                String errorMessage = Utils.getErrorMessage(error);
                if (errorMessage != null) {
                    Log.d("Send", "onError: "+errorMessage);
                }
                Toast.makeText(ChatActivity.this, "Cannot send", Toast.LENGTH_SHORT).show();
            }
        });
        requestProcessor.execute(GenerateUrl.getPrivateSendMsgUrl(sender, receiver, message));
    }

    private void repeat() {
        timer = new Timer();
        if (timerTask == null){
            timerTask = new TimerTask() {
                @Override
                public void run() {

                    try {
                        if (checkNetwork()) {
                            fetchMessage(sharedPreferences.getString(SignInActivity.NAME, "senderShareDefFailure"), receiver);
                        } else {
                            Toast.makeText(getBaseContext(), "No network available", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        }
        timer.scheduleAtFixedRate(timerTask, 0, 5000);
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (timer != null){
//            timer.cancel();
//        }
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (timer != null){
//            timer.cancel();
//            timer.scheduleAtFixedRate(timerTask, 0, 5000);
//        }
    }

    private void fetchMessage(String sender, String receiver) {
        final String url = GenerateUrl.getPrivateFetchMsgUrl(sender.trim(), receiver.trim());
        RequestProcessor requestProcessor = new RequestProcessor(this, Request.Method.GET);

        requestProcessor.setListener(new RequestProcessorListener() {
            @Override
            public void onSuccess(String response) {
                Log.d("PrivateChat", "onSuccess: "+response);
                String[] users = response.split("-");
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ChatActivity.this, android.R.layout.simple_list_item_1, users);
                listView.setAdapter(adapter);
            }

            @Override
            public void onLoading() {

            }

            @Override
            public void onError(VolleyError error) {
                String errorMessage = Utils.getErrorMessage(error);
                if (errorMessage != null) {
                    Log.d("PrivateChat", "onError: "+errorMessage);
                } else {
                    Log.d("PrivateChat", "onError: ");
                }
            }
        });

        requestProcessor.execute(url);
    }

}

