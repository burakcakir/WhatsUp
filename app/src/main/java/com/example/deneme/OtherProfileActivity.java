package com.example.deneme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class OtherProfileActivity extends AppCompatActivity {

    String otherID,userID;
    String kontrol="";
    Intent intent;
    TextView kullaniciIsmiText,dogumTarihiText,hakkimdaText,takipciSayisi,arkadasSayisi,userProfilText;
    ImageView arkadasEkleImg,mesajGonderImg,takipEtImg,kullaniciImg;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference,databaseReferenceArkadaslik;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        tanimla();
        action();
    }


    public void tanimla(){

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        databaseReferenceArkadaslik = firebaseDatabase.getReference().child("Arkadaslik_Istek");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = user.getUid();


        intent = getIntent();
        otherID = intent.getStringExtra("bilgiler");
        kullaniciIsmiText = (TextView)findViewById(R.id.kullaniciIsmiText);
        dogumTarihiText = (TextView)findViewById(R.id.dogumTarihiText);
        hakkimdaText = (TextView)findViewById(R.id.hakkimdaText);
        takipciSayisi = (TextView)findViewById(R.id.takipciSayisi);
        arkadasSayisi = (TextView)findViewById(R.id.arkadasSayisi);
        userProfilText = (TextView)findViewById(R.id.userProfilText);
        arkadasEkleImg = (ImageView)findViewById(R.id.arkadasEkleImg);
        mesajGonderImg = (ImageView)findViewById(R.id.mesajGonderImg);
        takipEtImg = (ImageView)findViewById(R.id.takipEtImg);
        kullaniciImg = (ImageView)findViewById(R.id.kullaniciImg);
        databaseReferenceArkadaslik.child(otherID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userID)){
                    kontrol = snapshot.child(userID).child("tip").getValue().toString();
                    arkadasEkleImg.setImageResource(R.drawable.cikar);

                }else{

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public void action(){

        databaseReference.child("Kullanicilar").child(otherID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                System.out.println("girdi:"+ otherID);

                Kullanicilar kl = snapshot.getValue(Kullanicilar.class);
                userProfilText.setText(kl.getIsim());
                kullaniciIsmiText.setText("İsim                  : "+ kl.getIsim());
                dogumTarihiText.setText("Doğum Tarihi : "+ kl.getDogumTarihi());
                hakkimdaText.setText("Hakkinda         : "+ kl.getHakkimda());
                Picasso.get().load(kl.getResim()).resize(180,180).into(kullaniciImg);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        arkadasEkleImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!kontrol.equals("")){

                }else{
                    arkadasEkle(otherID,userID);
                }

            }
        });

    }

    public void arkadasEkle(String otherId, String userId){

        databaseReferenceArkadaslik.child(userId).child(otherId).child("tip").setValue("gonderdi").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    databaseReferenceArkadaslik.child(otherId).child(userId).child("tip").setValue("alındı").addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(OtherProfileActivity.this , "Arkadaşlık İsteği Başarıyla Gönderildi", Toast.LENGTH_SHORT).show();
                                arkadasEkleImg.setImageResource(R.drawable.cikar);
                            }else{
                                Toast.makeText(OtherProfileActivity.this , "Bir problem meydana geldi!", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });

                }else{
                    Toast.makeText(OtherProfileActivity.this , "Bir problem meydana geldi!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void arkadasIptalEt(String otherId, String userId){

    }


}