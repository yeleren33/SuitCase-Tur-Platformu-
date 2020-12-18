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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
public class KaydolActivity extends AppCompatActivity {
    FirebaseAuth yetki;
    DatabaseReference yol;
    ProgressDialog progressDialog;
    EditText edit_kullaniciad, edit_Ad, edit_email, edit_sifre,edit_sifretekrar;
    Button btn_kaydol;
    TextView txt_kayitsoru;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kaydol);
        yetki = FirebaseAuth.getInstance();
        edit_kullaniciad = findViewById(R.id.kayitKullaniciAd);
        edit_Ad = findViewById(R.id.kayitAd);
        edit_email = findViewById(R.id.kayitEmail);
        edit_sifre = findViewById(R.id.kayitSifre);
        edit_sifretekrar = findViewById(R.id.kayitSifreTekrar);
        btn_kaydol = findViewById(R.id.kayitActivityButon);
        btn_kaydol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // kaydetme işlemi yapılırken beklmee işlemi tanımlandı
                progressDialog = new ProgressDialog(KaydolActivity.this);
                progressDialog.setMessage("Lütfen Bekleyin");
                progressDialog.show();
                final String str_kullaniciad = edit_kullaniciad.getText().toString();
                final String str_ad = edit_Ad.getText().toString();
                String str_email = edit_email.getText().toString();
                String str_sifre = edit_sifre.getText().toString();
                String str_sifretekrar = edit_sifretekrar.getText().toString();
                if (TextUtils.isEmpty(str_kullaniciad)||TextUtils.isEmpty(str_ad)||TextUtils.isEmpty(str_email)// Kaydedilecek alanları boş olması durumu kontorl edildi
                        ||TextUtils.isEmpty(str_sifre)||TextUtils.isEmpty(str_sifretekrar)){
                    Toast.makeText(KaydolActivity.this, "Lütfen Bütün Alanları Doldurun ", Toast.LENGTH_SHORT).show();
                }else if (!str_sifre.equals(str_sifretekrar)){//iki şifrenin aynı olması kontrol edildi
                    Toast.makeText(KaydolActivity.this, "Şifreler Aynı değil", Toast.LENGTH_SHORT).show();
                }else if (str_sifre.length()<6){//şifrenin altı karakterden az olup olmadığı kontorl edildi
                    Toast.makeText(KaydolActivity.this, "Şifreniz 6 karakterden az olamaz", Toast.LENGTH_SHORT).show();
                }else {

                    yetki.createUserWithEmailAndPassword(str_email, str_sifre).addOnSuccessListener(KaydolActivity.this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(getApplicationContext(),"Kullanıcı başarıyla oluştu",Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Kullanıcı Oluşturma Hatası!",Toast.LENGTH_SHORT).show();
                        }
                    });
                    // kayıt ekleme işlemi burda yapılacak lakin karmaşıklık olacağı için method tanımlanıp öyle giriş yapılacak
                    // normalde burdada yapılabilir
                   // kayitet(str_kullaniciad,str_ad,str_email,str_sifre);
                  /*  yetki.createUserWithEmailAndPassword(str_email,str_sifre)
                            .addOnCompleteListener(KaydolActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        FirebaseUser firebaseUser = yetki.getCurrentUser();
                                        String kullaniciId = firebaseUser.getUid();
                                        yol = FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(kullaniciId);
                                        //Birden fazla veri kaydetmek için hashmap kullanıyoruz
                                        HashMap<String, Object> hashMap = new HashMap<>();
                                        hashMap.put("id",kullaniciId);
                                        hashMap.put("kullaniciAdi",str_kullaniciad.toLowerCase());
                                        hashMap.put("ad",str_ad);
                                        hashMap.put("bio","");
                                        hashMap.put("resimler","https://firebasestorage.googleapis.com/v0/b/suitcase-e3b63.appspot.com/o/placeholder.png?alt=media&token=604b5cf6-8599-443d-9b02-401b58d5e1cf");
                                        yol.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    //Kaydetme sırasında yüklenme işlemini sonlandırdık
                                                    progressDialog.dismiss();
                                                    Intent intent = new Intent(KaydolActivity.this, AnasayfaActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                                    }else{
                                        progressDialog.dismiss();
                                        Toast.makeText(KaydolActivity.this, "Bu Email veya Şifre ile Kayıt Başarısız ", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });*/
                }
            }
        });
        txt_kayitsoru = findViewById(R.id.kayitSoru);
        txt_kayitsoru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(KaydolActivity.this,GirisActivity.class));
            }
        });
    }
    /*private void kayitet(final String kullaniciadi,final  String ad, String email, String sifre){
        // kaydetme kodları burda
        yetki.createUserWithEmailAndPassword(email,sifre)
                .addOnCompleteListener(KaydolActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = yetki.getCurrentUser();
                            String kullaniciId = firebaseUser.getUid();
                            yol = FirebaseDatabase.getInstance().getReference().child("Kullanicilar").child(kullaniciId);
                            //Birden fazla veri kaydetmek için hashmap kullanıyoruz
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("id",kullaniciId);
                            hashMap.put("kullaniciAdi",kullaniciadi.toLowerCase());
                            hashMap.put("ad",ad);
                            hashMap.put("bio","");
                            hashMap.put("resimler","https://firebasestorage.googleapis.com/v0/b/suitcase-e3b63.appspot.com/o/placeholder.png?alt=media&token=604b5cf6-8599-443d-9b02-401b58d5e1cf");
                            yol.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        //Kaydetme sırasında yüklenme işlemini sonlandırdık
                                        progressDialog.dismiss();
                                        Intent intent = new Intent(KaydolActivity.this, AnasayfaActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(KaydolActivity.this, "Bu Email veya Şifre ile Kayıt Başarısız ", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }*/
}