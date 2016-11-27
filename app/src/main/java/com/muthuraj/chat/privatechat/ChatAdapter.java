/* $Id$ */
package com.muthuraj.chat.privatechat;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.muthuraj.chat.R;
import com.muthuraj.chat.signin.SignInActivity;

import java.util.List;

/**
 * Created by muthu-3955 on 28/11/16.
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> senderNames;
    private List<String> messages;
    private String username;

    private LayoutInflater inflater;

    private static final int LEFT = 0;
    private static final int RIGHT = 1;

    public ChatAdapter(Context context, List<String> senderNames, List<String> messages) {
        this.senderNames = senderNames;
        this.messages = messages;

        inflater = LayoutInflater.from(context);

        SharedPreferences sharedPreferences = context.getSharedPreferences(SignInActivity.CHAT, Context.MODE_PRIVATE);
        username = sharedPreferences.getString(SignInActivity.NAME, "");
    }

    @Override
    public int getItemViewType(int position) {
        if (username.equals(senderNames.get(position))) {
            return RIGHT;
        }
        return LEFT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == LEFT) {
            return new LeftItemHolder(inflater.inflate(R.layout.private_chat_left_item, parent, false));
        }
        return new RightItemHolder(inflater.inflate(R.layout.private_chat_right_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LeftItemHolder) {
            ((LeftItemHolder) holder).textView.setText(messages.get(position));
        } else {
            ((RightItemHolder) holder).textView.setText(messages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    private static class LeftItemHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        LeftItemHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }

    private static class RightItemHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        RightItemHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }
}
