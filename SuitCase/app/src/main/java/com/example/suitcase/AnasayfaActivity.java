package com.example.suitcase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import Cerceve.HomeFragment;
import Cerceve.PersonFragment;
import Cerceve.SearchFragment;

public class AnasayfaActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Fragment secilicerceve=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayıcı,new HomeFragment()).commit();

    }


    private  BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                    switch (item.getItemId()){
                        //Anaçerçeve çagırma
                        case R.id.nav_home:
                            secilicerceve = new HomeFragment();
                        break;
                        //Search çagırma
                        case R.id.nav_search:
                            secilicerceve = new SearchFragment();
                            break;
                        //Add çagırma
                        case R.id.nav_add:
                            secilicerceve = null;
                            startActivity(new Intent(AnasayfaActivity.this,PostActivity.class));
                            break;
                        //Person çagırma
                        case R.id.nav_person:
                            SharedPreferences.Editor editor = getSharedPreferences("prefs",MODE_PRIVATE).edit();
                            editor.putString("profield", FirebaseAuth.getInstance().getCurrentUser().getUid());
                            editor.apply();
                            secilicerceve = new PersonFragment();
                            break;
                    }
                    if (secilicerceve != null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.cerceve_kapsayıcı,secilicerceve).commit();
                    }

                    return true;
                }
            };
}