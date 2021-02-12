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

public class OtherProfileActivity extends AppCompatActivity {

    String otherID,userID;
    String kontrol="";
    Intent intent;
    TextView kullaniciIsmiText,dogumTarihiText,hakkimdaText,userProfilText,eklemeText;
    ImageView arkadasEkleImg,kullaniciImg;
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
        userProfilText = (TextView)findViewById(R.id.userProfilText);
        eklemeText = (TextView)findViewById(R.id.eklemeText);
        arkadasEkleImg = (ImageView)findViewById(R.id.arkadasEkleImg);
        kullaniciImg = (ImageView)findViewById(R.id.kullaniciImg);


        databaseReferenceArkadaslik.child(otherID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(userID)){
                    kontrol = "istek";
                    arkadasEkleImg.setImageResource(R.drawable.reddet);
                    eklemeText.setText("İsteği İptal Et");

                }else if(kontrol.equals("")){
                    arkadasEkleImg.setImageResource(R.drawable.ekle);
                    eklemeText.setText("Arkadaş Ekle");

                }
                else {}

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("Arkadaslar").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(otherID)){
                    kontrol = "arkadas";
                    arkadasEkleImg.setImageResource(R.drawable.cikar);
                    eklemeText.setText("Arkadaşı Çıkar");
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

                if (kontrol.equals("istek")){
                    arkadasIptalEt(otherID,userID);

                }else if (kontrol.equals("arkadas")){
                    arkadasTablosundanCikar(otherID,userID);
                }
                else{
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
                            eklemeText.setText("İsteği İptal Et");
                            if (task.isSuccessful()){
                                kontrol="istek";
                                Toast.makeText(OtherProfileActivity.this , "Arkadaşlık İsteği Başarıyla Gönderildi", Toast.LENGTH_SHORT).show();
                                arkadasEkleImg.setImageResource(R.drawable.reddet);


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
        databaseReferenceArkadaslik.child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReferenceArkadaslik.child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol = "";
                        arkadasEkleImg.setImageResource(R.drawable.ekle);
                       eklemeText.setText("Arkadaş Ekle");
                        Toast.makeText(OtherProfileActivity.this , "Arkadaşlık isteği iptal edildi!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
    private void arkadasTablosundanCikar(final String otherId,final String userId) {
     databaseReference.child("Arkadaslar").child(otherId).child(userId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child("Arkadaslar").child(userId).child(otherId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        kontrol = "";
                        arkadasEkleImg.setImageResource(R.drawable.ekle);
                        eklemeText.setText("Arkadaş Ekle");
                        Toast.makeText(OtherProfileActivity.this , "Arkadaşlıktan Çıkarıldı!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }


}