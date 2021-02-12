package com.example.deneme;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> {

    List<String> userKeyList;
    Activity activity;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    String userID;




    public FriendListAdapter(List<String> userKeyList, Activity activity, Context context) {
        this.userKeyList = userKeyList;
        this.activity = activity;
        this.context = context;
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        userID = firebaseUser.getUid();
    }

    //layout tanımlası yapılacak sonrasında viewlara saetleme yapılacak
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.friends_layout, parent, false);


        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        databaseReference.child("Kullanicilar").child(userKeyList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanicilar kl = snapshot.getValue(Kullanicilar.class);

                Picasso.get().load(kl.getResim()).resize(180, 180).into(holder.userimg);
                holder.friend_list_text.setText(kl.getIsim());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.friendslayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("bilgiler",userKeyList.get(position));
                context.startActivity(intent);

            }
        });


    }


    @Override
    public int getItemCount() {

        return userKeyList.size();
    }

    //view tanımlanma işleri
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView friend_list_text;
        ImageView userimg;
        LinearLayout friendslayout;



        ViewHolder(View itemView) {
            super(itemView);
            friend_list_text = (TextView) itemView.findViewById(R.id.friend_list_text);
            userimg = (ImageView) itemView.findViewById(R.id.userimg);
            friendslayout = (LinearLayout) itemView.findViewById(R.id.friendslayout);


        }

    }




}
