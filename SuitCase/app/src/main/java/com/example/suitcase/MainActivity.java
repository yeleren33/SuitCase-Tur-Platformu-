package com.example.suitcase;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
public class MainActivity extends AppCompatActivity {
    Button btn_girisActivity, btn_kayitActivity;
    FirebaseUser baslangıcKullanici;
    @Override
    protected void onStart() {
        super.onStart();
        baslangıcKullanici = FirebaseAuth.getInstance().getCurrentUser();
        //Eğer kullanici veritabanında varsa direk anasayfaya gidecek
        if (baslangıcKullanici != null){
            startActivity(new Intent(MainActivity.this,AnasayfaActivity.class));
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_girisActivity = findViewById(R.id.btn_giris);
        btn_girisActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,GirisActivity.class));
            }
        });
        btn_kayitActivity = findViewById(R.id.btn_kayit);
        btn_kayitActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,KaydolActivity.class));
            }
        });
    }
}