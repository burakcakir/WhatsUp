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

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> {

    List<String> userKeyList;
    Activity activity;
    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    String userID;


    public FriendRequestAdapter(List<String> userKeyList, Activity activity, Context context) {
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
        View view = layoutInflater.inflate(R.layout.friend_req_layout, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        databaseReference.child("Kullanicilar").child(userKeyList.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Kullanicilar kl = snapshot.getValue(Kullanicilar.class);

                Picasso.get().load(kl.getResim()).resize(180, 180).into(holder.userimg);
                holder.friend_req_text.setText(kl.getIsim());


                holder.friend_req_ekle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        kabulEt(userID, userKeyList.get(position));


                    }
                });
                holder.friend_req_red.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        redEt(userID, userKeyList.get(position));
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // holder.userLayout.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View view) {

        //         Intent intent = new Intent(context, OtherProfileActivity.class);
        //         intent.putExtra("bilgiler",userKeyList.get(position));
        //         context.startActivity(intent);

        //     }
        // });

    }


//adapter listesi sizeı

    @Override
    public int getItemCount() {

        return userKeyList.size();
    }

    //view tanımlanma işleri
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView friend_req_text;
        ImageView userimg;
        Button friend_req_ekle, friend_req_red;

        ViewHolder(View itemView) {
            super(itemView);
            friend_req_text = (TextView) itemView.findViewById(R.id.friend_req_text);
            userimg = (ImageView) itemView.findViewById(R.id.userimg);
            friend_req_ekle = (Button) itemView.findViewById(R.id.friend_req_ekle);
            friend_req_red = (Button) itemView.findViewById(R.id.friend_req_red);

        }

    }

    private void kabulEt(String userId, String otherId) {

        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);
        databaseReference.child("Arkadaslar").child(userId).child(otherId).child("tarih").setValue(reportDate).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                databaseReference.child("Arkadaslar").child(otherId).child(userId).child("tarih").setValue(reportDate).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Arkadaşlık İsteğini Kabul Ettiniz", Toast.LENGTH_SHORT).show();
                            databaseReference.child("Arkadaslik_Istek").child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    databaseReference.child("Arkadaslik_Istek").child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {



                                        }
                                    });
                                }
                            });

                        }
                    }
                });
            }
        });

    }

    private void redEt(String userId, String otherId) {

        databaseReference.child("Arkadaslik_Istek").child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child("Arkadaslik_Istek").child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Arkadaşlık İsteğini Reddettiniz", Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });
    }
}
