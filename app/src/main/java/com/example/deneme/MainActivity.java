package com.example.deneme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tanimla();

        kontrol();


    }

    public void tanimla()
    {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    public void kontrol()
    {
        if (firebaseUser == null)
        {
            Intent intent = new Intent(MainActivity.this,GirisYapActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.cikisYap)
        {
            firebaseAuth.signOut();

            Intent intent = new Intent(MainActivity.this,GirisYapActivity.class);
            startActivity(intent);
            finish();
        }
        if ( item.getItemId() == R.id.profilDuzenle)
        {
            Intent intent = new Intent(MainActivity.this,new KullaniciProfilActivity().getClass());
            startActivity(intent);

        }
        if ( item.getItemId() == R.id.kullanicilar)
        {
            Intent intent = new Intent(MainActivity.this,KullanicilarActivity.class);
            startActivity(intent);
        }
        if ( item.getItemId() == R.id.bildirimler)
        {
            Intent intent = new Intent(MainActivity.this,BildirimActivity.class);
            startActivity(intent);

        }
        if ( item.getItemId() == R.id.arkadaslistesi)
        {
            Intent intent = new Intent(MainActivity.this,FriendListActivity.class);
            startActivity(intent);

        }


        return super.onOptionsItemSelected(item);
    }
}