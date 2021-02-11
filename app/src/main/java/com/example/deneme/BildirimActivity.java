package com.example.deneme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class BildirimActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseUser user;
    String userID;
    List<String> friend_req_key_list;
    RecyclerView friend_req_recy;
    FriendRequestAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bildirim);
        tanimla();
        istekler();
    }

    public void tanimla(){

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Arkadaslik_Istek");
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        userID = user.getUid();
        friend_req_key_list = new ArrayList<>();
        friend_req_recy = findViewById(R.id.friend_req_recy);
        RecyclerView.LayoutManager mng = new GridLayoutManager(this,1);
        friend_req_recy.setLayoutManager(mng);
        adapter = new FriendRequestAdapter(friend_req_key_list,new BildirimActivity(),this);
        friend_req_recy.setAdapter(adapter);



    }

    public void istekler(){

        reference.child(userID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                String kontrol = snapshot.child("tip").getValue().toString();
                if (kontrol.equals("alındı")) {
                    friend_req_key_list.add(snapshot.getKey());
                    adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                friend_req_key_list.remove(snapshot.getKey());
                adapter.notifyDataSetChanged();
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