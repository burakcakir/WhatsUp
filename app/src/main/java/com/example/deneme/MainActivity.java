package com.example.deneme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;
    RecyclerView friend_list_recy;
    FriendListAdapter adapter;
    List<String> keylist;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tanimla();
        kontrol();

        arkadasListesiGetir();


    }


    public void tanimla() {
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Arkadaslar");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        userID = firebaseUser.getUid();
        keylist = new ArrayList<>();
        friend_list_recy = findViewById(R.id.friend_list_recy);
        RecyclerView.LayoutManager mng = new GridLayoutManager(this, 1);
        friend_list_recy.setLayoutManager(mng);
        adapter = new FriendListAdapter(keylist, new MainActivity(), this);
        friend_list_recy.setAdapter(adapter);

    }

    public void arkadasListesiGetir() {

        reference.child(userID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String kontrol = snapshot.child("tarih").getValue().toString();
                if (!kontrol.equals("")){
                keylist.add(snapshot.getKey());
                adapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                keylist.remove(snapshot.getKey());
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

    public void kontrol() {
        if (firebaseUser == null) {
            Intent intent = new Intent(MainActivity.this, GirisYapActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.cikisYap) {
            firebaseAuth.signOut();

            Intent intent = new Intent(MainActivity.this, GirisYapActivity.class);
            startActivity(intent);
            finish();
        }
        if (item.getItemId() == R.id.profilDuzenle) {
            Intent intent = new Intent(MainActivity.this, new KullaniciProfilActivity().getClass());
            startActivity(intent);

        }
        if (item.getItemId() == R.id.kullanicilar) {
            Intent intent = new Intent(MainActivity.this, KullanicilarActivity.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.bildirimler) {
            Intent intent = new Intent(MainActivity.this, BildirimActivity.class);
            startActivity(intent);

        }


        return super.onOptionsItemSelected(item);
    }
}