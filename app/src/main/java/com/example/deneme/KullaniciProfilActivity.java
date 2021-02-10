package com.example.deneme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KullaniciProfilActivity extends AppCompatActivity {
    String imageURL;
    private FirebaseUser firebaseUser;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private View view;
    EditText kullaniciIsmi,dogumTarihi,hakkimda;
    ImageView resim;
    Button bilgiGuncelle;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;
    StorageTask storageTask;




    public void guncelle()
    {
        String isim = kullaniciIsmi.getText().toString();
        String dogum = dogumTarihi.getText().toString();
        String hakkimdakismi = hakkimda.getText().toString();

        databaseReference = database.getReference().child("Kullanicilar").child(firebaseAuth.getUid());
        Map map = new HashMap();
        //map.put("resim",resim);
        map.put("isim",isim);
        map.put("dogumTarihi",dogum);
        map.put("hakkimda",hakkimdakismi);
        if (imageURL.equals("null")){
            map.put("resim","null");
        }else{
            map.put("resim",imageURL);
        }
        databaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(KullaniciProfilActivity.this, new KullaniciProfilActivity().getClass());
                    startActivity(intent);
                    finish();
                    Toast.makeText(KullaniciProfilActivity.this, "Güncelleme Başarılı", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(KullaniciProfilActivity.this, "Güncelleme Başarısız!", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



    public void tanimla()
    {
        kullaniciIsmi = findViewById(R.id.kullaniciIsmi);
        dogumTarihi = findViewById(R.id.dogumTarihi);
        hakkimda = findViewById(R.id.hakkimda);
        resim = findViewById(R.id.resim);
        bilgiGuncelle = findViewById(R.id.bilgiGuncelle);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        bilgiGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guncelle();
            }
        });
        resim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 galeriAc();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("Kullanicilar").child(firebaseUser.getUid());
    }

    private void galeriAc() {

        Intent ıntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(ıntent,5);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5 && resultCode == Activity.RESULT_OK) {
            Uri filePath = data.getData();
            String randomname=RandomName.getSaltString();
            StorageReference ref = storageReference.child("kullaniciresimleri").child(randomname +".jpg");
            storageTask = ref.putFile(filePath);
            storageTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw  task.getException();
                    }

                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri downloadUri = task.getResult();
                    String mUri = downloadUri.toString();
                    databaseReference = database.getReference().child("Kullanicilar").child(firebaseAuth.getUid());
                    String isim = kullaniciIsmi.getText().toString();
                    String dogum = dogumTarihi.getText().toString();
                    String hakkimdakismi = hakkimda.getText().toString();
                    firebaseStorage.getReference().child("Kullanicilar").child(firebaseAuth.getUid());
                    databaseReference = database.getReference().child("Kullanicilar").child(firebaseAuth.getUid());
                    Map map = new HashMap();
                    map.put("isim",isim);
                    map.put("dogumTarihi",dogum);
                    map.put("hakkimda",hakkimdakismi);
                    map.put("resim",mUri);

                    databaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(KullaniciProfilActivity.this, new KullaniciProfilActivity().getClass());
                                startActivity(intent);
                                finish();
                                Toast.makeText(KullaniciProfilActivity.this, "Güncelleme Başarılı", Toast.LENGTH_SHORT).show();


                            }else{
                                Toast.makeText(KullaniciProfilActivity.this, "Güncelleme Başarısız!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else
                    Toast.makeText(KullaniciProfilActivity.this, "Resim Güncellenemedi!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    public void bilgileriGetir()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                Kullanicilar kl = snapshot.getValue(Kullanicilar.class);

                kullaniciIsmi.setText(kl.getIsim());
                dogumTarihi.setText(kl.getDogumTarihi());
                hakkimda.setText(kl.getHakkimda());
                imageURL = kl.getResim();
                if (!kl.getResim().equals("null")){
                    Picasso.get().load(kl.getResim()).resize(180,180).into(resim);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {

            }
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanici_profil);
        tanimla();
        bilgileriGetir();

    }
}