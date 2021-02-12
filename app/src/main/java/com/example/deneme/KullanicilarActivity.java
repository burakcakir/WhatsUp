package com.example.deneme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class KullanicilarActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    List<String> userKeyList;
    RecyclerView userListRecyler;
    UserAdapter userAdapter;
    FirebaseAuth auth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanicilar);
        tanimla();
        kullanicilariGetir();
    }


    public void tanimla(){
        userKeyList = new ArrayList<>();
        firebaseDatabase =FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference();
        userListRecyler = findViewById(R.id.userListRecyler);
        RecyclerView.LayoutManager mng = new GridLayoutManager(this,2);
        userListRecyler.setLayoutManager(mng);
        userAdapter = new UserAdapter(userKeyList,new KullanicilarActivity(),this);
        userListRecyler.setAdapter(userAdapter);
        auth= FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
    }

    public void kullanicilariGetir(){
        databaseReference.child("Kullanicilar").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                databaseReference.child("Kullanicilar").child(snapshot.getKey()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Kullanicilar kl = snapshot.getValue(Kullanicilar.class);
                        if (!kl.getIsim().equals("İsim") && !snapshot.getKey().equals(user.getUid()) ){
                            userKeyList.add(snapshot.getKey());
                            userAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                userAdapter.notifyDataSetChanged();
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