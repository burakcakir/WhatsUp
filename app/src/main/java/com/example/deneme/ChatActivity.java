package com.example.deneme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<String> userKeyList;
    FirebaseAuth auth;
    FirebaseUser user;
    Intent intent;
    String userID;
    ImageView userImg,backImg;
    TextView userText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        tanimla();
        action();

    }

    public void tanimla() {
        userKeyList = new ArrayList<>();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        intent = getIntent();
        userID = intent.getStringExtra("bilgiler");
        userImg = (ImageView)findViewById(R.id.userimg);
        backImg = (ImageView)findViewById(R.id.backImg);
        userText = (TextView)findViewById(R.id.userText);
    }
    private void action() {
        databaseReference.child("Kullanicilar").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Kullanicilar kl = snapshot.getValue(Kullanicilar.class);
                userText.setText(kl.getIsim());
                Picasso.get().load(kl.getResim()).resize(180,180).into(userImg);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
