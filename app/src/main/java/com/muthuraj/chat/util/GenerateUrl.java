/* $Id$ */
package com.muthuraj.chat.util;

import android.net.Uri;
import android.util.Log;

/**
 * Created by muthu-3955 on 27/11/16.
 */
public class GenerateUrl {

    public static final String BASE_URL = "muthuraj.xyz/chat";
    public static final String SCHEMA = "http";

    public static String getSignInUrl(String username, String password) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEMA)
                .encodedAuthority(BASE_URL)
                .appendPath("android_signin.php")
                .appendQueryParameter("username", username)
                .appendQueryParameter("password", password);
        return builder.build().toString();
    }

    public static String getPrivateFetchMsgUrl(String sender, String receiver){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEMA)
                .encodedAuthority(BASE_URL)
                .appendPath("android_fetch_msg.php")
                .appendQueryParameter("sender", sender)
                .appendQueryParameter("receiver", receiver);
        return builder.build().toString();
    }

    public static String getPrivateSendMsgUrl(String sender, String receiver, String msg){
        Uri.Builder builder = new Uri.Builder();
        Log.d("Send", "getPrivateSendMsgUrl: sender: "+sender+" receiver: "+receiver+" msg: "+msg);
        builder.scheme(SCHEMA)
                .encodedAuthority(BASE_URL)
                .appendPath("android_send_msg.php")
                .appendQueryParameter("sender", sender)
                .appendQueryParameter("receiver", receiver)
                .appendQueryParameter("msg", msg);
        return builder.build().toString();
    }

    public static String getChatListUrl(String username){
        Uri.Builder builder = new Uri.Builder();
        builder.scheme(SCHEMA)
                .encodedAuthority(BASE_URL)
                .appendPath("android_private_chat_list.php")
                .appendQueryParameter("username", username);
        return builder.build().toString();
    }
}
