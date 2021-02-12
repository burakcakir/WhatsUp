package com.example.deneme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<String> userKeyList;
    FirebaseAuth auth;
    FirebaseUser user;
    Intent intent;
    String userID;
    ImageView userImg, backImg;
    TextView userText;
    EditText messageTextEditText;
    FloatingActionButton sendMessage;
    List<Message> messageList;
    RecyclerView chatRecyView;
    MessageAdapter messageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        tanimla();
        action();
        loadMessage();

    }

    public void tanimla() {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        intent = getIntent();
        userID = intent.getStringExtra("bilgiler");
        userImg = (ImageView) findViewById(R.id.userimg);
        backImg = (ImageView) findViewById(R.id.backImg);
        userText = (TextView) findViewById(R.id.userText);
        sendMessage = (FloatingActionButton) findViewById(R.id.sendMessage);
        messageTextEditText =(EditText)findViewById(R.id.messageTextEditText);
        messageList = new ArrayList<>();
        userKeyList = new ArrayList<>();
        chatRecyView = (RecyclerView)findViewById(R.id.chatRecyView);
        RecyclerView.LayoutManager mng = new GridLayoutManager(ChatActivity.this,1);
        chatRecyView.setLayoutManager(mng);
        messageAdapter = new MessageAdapter(userKeyList,ChatActivity.this,ChatActivity.this,messageList);
        chatRecyView.setAdapter(messageAdapter);
    }

    private void action() {
        databaseReference.child("Kullanicilar").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Kullanicilar kl = snapshot.getValue(Kullanicilar.class);
                userText.setText(kl.getIsim());
                Picasso.get().load(kl.getResim()).resize(180, 180).into(userImg);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });


        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, new OtherProfileActivity().getClass());
                intent.putExtra("bilgiler", userID);
                startActivity(intent);
                finish();

            }
        });


        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message = messageTextEditText.getText().toString();
                messageTextEditText.setText("");
                sendMessage(user.getUid(),userID,"text",GetDate.getDate(),false,message);

            }
        });

    }


    public void sendMessage(final String userID, final String otherID, String textType, String date, Boolean seen, String messageText) {

        String messageID = databaseReference.child("Mesajlar").child(userID).child(otherID).push().getKey();

        Map messageMap = new HashMap();

        messageMap.put("type", textType);
        messageMap.put("seen", seen);
        messageMap.put("time", date);
        messageMap.put("text", messageText);
        messageMap.put("from",userID);

        databaseReference.child("Mesajlar").child(userID).child(otherID).child(messageID).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                databaseReference.child("Mesajlar").child(otherID).child(userID).child(messageID).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });

            }
        });
    }
    public void loadMessage(){
        databaseReference.child("Mesajlar").child(user.getUid()).child(userID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
               // System.out.println("mesaj:"+snapshot.getKey());
                Log.i("messajtext",snapshot.getKey());
                messageList.add(message);
                messageAdapter.notifyDataSetChanged();
                userKeyList.add(snapshot.getKey());
                chatRecyView.scrollToPosition(messageList.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
