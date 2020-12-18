package com.example.suitcase;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class GirisActivity extends AppCompatActivity {
    FirebaseAuth girisyetki;
    DatabaseReference girisyol;
    ProgressDialog progressDialog;
    EditText edit_email, edit_sifre;
    Button btn_giris;
    TextView giris_soru;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);
        girisyetki = FirebaseAuth.getInstance();
        edit_email = findViewById(R.id.girisEmail);
        edit_sifre = findViewById(R.id.girisSifre);
        btn_giris = findViewById(R.id.girisActivityButon);
        btn_giris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(GirisActivity.this);
                progressDialog.setMessage("Giriş Yapılıyor");
                progressDialog.show();
                String str_emailgiris = edit_email.getText().toString();
                String str_sifregiris = edit_sifre.getText().toString();
                if (TextUtils.isEmpty(str_emailgiris)||TextUtils.isEmpty(str_sifregiris)){
                    Toast.makeText(GirisActivity.this, "Bütün Alanları Doldurun", Toast.LENGTH_LONG).show();
                }else{
                    //Giriş yapma kodları
                    girisyetki.signInWithEmailAndPassword(str_emailgiris,str_sifregiris)
                            .addOnCompleteListener(GirisActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        girisyol = FirebaseDatabase.getInstance().getReference().
                                                child("Kullanicilar").child(girisyetki.getCurrentUser().getUid());
                                        girisyol.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                progressDialog.dismiss();
                                                Intent intent = new Intent(GirisActivity.this,AnasayfaActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {
                                                progressDialog.dismiss();
                                            }
                                        });
                                    }
                                    else{
                                        progressDialog.dismiss();
                                        Toast.makeText(GirisActivity.this, "Giriş Başarısız Oldu", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            }
        });
        giris_soru = findViewById(R.id.girisSoru);
        giris_soru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GirisActivity.this, KaydolActivity.class));
            }
        });
    }
}