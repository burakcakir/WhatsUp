package com.example.whatsup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class KayitOlActivity extends AppCompatActivity {

    ImageView imageView;
    EditText kullaniciMail;
    EditText kullaniciSifre;
    ImageButton kayitolButton;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);

        tanimla();

    }

    public void tanimla() {
        imageView = findViewById(R.id.logo);
        kullaniciMail = findViewById(R.id.kullaniciEmail);
        kullaniciSifre = findViewById(R.id.kullaniciSifre);
        kayitolButton = findViewById(R.id.girisYapButton);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void kayitOl(View view) {

        String email = kullaniciMail.getText().toString();
        String sifre = kullaniciSifre.getText().toString();


        if (email.equals("") || sifre.equals(""))
        {
            Toast.makeText(this, "Email ve Şifre Boş Bırakılamaz!", Toast.LENGTH_SHORT).show();
        }
        else {

            firebaseAuth.createUserWithEmailAndPassword(email, sifre).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(KayitOlActivity.this, "Kayıt Başarılı!", Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(KayitOlActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(KayitOlActivity.this, e.getLocalizedMessage().toString(), Toast.LENGTH_LONG).show();
                }
            });


        }

    }


}