package com.example.deneme;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class GirisYapActivity extends AppCompatActivity {

    ImageView imageView;
    EditText kullaniciMail;
    EditText kullaniciSifre;
    Button kayitolButton;


    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris_yap);

        tanimla();


    }

    public void tanimla() {

        imageView = findViewById(R.id.logo);
        kullaniciMail = findViewById(R.id.kullaniciEmail);
        kullaniciSifre = findViewById(R.id.kullaniciSifre);
        kayitolButton = findViewById(R.id.girisYapButton);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void KayitOl(View view)
    {
        Intent intent = new Intent(GirisYapActivity.this,KayitOlActivity.class);
        startActivity(intent);

    }

    public void GirisYap(View view) {
        String email = kullaniciMail.getText().toString();
        String sifre = kullaniciSifre.getText().toString();


        if (email.equals("") || sifre.equals(""))
        {
            Toast.makeText(this, "Email ve Şifre Boş Bırakılamaz!", Toast.LENGTH_SHORT).show();
        } else
        {
            firebaseAuth.signInWithEmailAndPassword(email, sifre).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    Toast.makeText(GirisYapActivity.this, "Giriş Başarılı", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(GirisYapActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(GirisYapActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                }
            });
        }

    }

}