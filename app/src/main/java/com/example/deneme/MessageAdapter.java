package com.example.deneme;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    List<String> userKeyList;
    Activity activity;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    String userID;
    List<Message> messageList;
    Boolean state;
    int viewTypeSend = 1, viewTypeRecieved = 2;


    public MessageAdapter(List<String> userKeyList, Activity activity, Context context, List<Message> messageList) {
        this.userKeyList = userKeyList;
        this.activity = activity;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        userID = firebaseUser.getUid();
        this.messageList = messageList;
        state = false;
    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view;
        if (viewType == viewTypeSend) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            view = layoutInflater.inflate(R.layout.message_send_layout, parent, false);
            return new ViewHolder(view);

        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            view = layoutInflater.inflate(R.layout.message_recieved_layout, parent, false);
            return new ViewHolder(view);

        }
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        System.out.println("mesajtext:"+messageList.get(position).getText());
        holder.messageText.setText(messageList.get(position).getText());

    }


    @Override
    public int getItemCount() {

        return messageList.size();
    }

    //view tanımlanma işleri
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView messageText;

        ViewHolder(View itemView) {
            super(itemView);
            if (state == true) {
                messageText = (TextView) itemView.findViewById(R.id.messageSendText);
            } else {
                messageText = (TextView) itemView.findViewById(R.id.messageRecievedText);
            }
        }


    }


    @Override
    public int getItemViewType(int position) {
        if (messageList.get(position).getFrom().equals(userID)) {
            state = true;
            return viewTypeSend;
        } else {
            state = false;
            return viewTypeRecieved;
        }
    }
}
