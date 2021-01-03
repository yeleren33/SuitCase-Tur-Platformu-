package com.example.suitcase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {

    Uri resimuri;
    String myUri = "";

    StorageTask yuklemeGorevi;
    StorageReference resimYuklemeYolu;

    ImageView imageClose, imageEklendi;
    TextView txtGonder;
    EditText txtHakkında;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        imageClose = findViewById(R.id.gonderi_close);
        imageEklendi = findViewById(R.id.gonderi_resim);
        txtGonder = findViewById(R.id.txt_gonder);
        txtHakkında = findViewById(R.id.gonderi_text);

        resimYuklemeYolu = FirebaseStorage.getInstance().getReference("gonderiler");

        imageClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this,AnasayfaActivity.class));
                finish();
            }
        });

        txtGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resimyukle();
            }
        });

        CropImage.activity().setAspectRatio(1,1).start(PostActivity.this);

    }

    private String dosyaUzantısıAl(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void resimyukle() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Gönderiliyor...");
        progressDialog.show();
        //Resim yükleme kodları
        if (resimuri != null){
            final StorageReference dosyayolu  =resimYuklemeYolu.child(System.currentTimeMillis()
                    +"."+dosyaUzantısıAl(resimuri));
            yuklemeGorevi = dosyayolu.putFile(resimuri);
            yuklemeGorevi.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return dosyayolu.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri indirmeUri = task.getResult();
                        myUri = indirmeUri.toString();

                        DatabaseReference veriYolu = FirebaseDatabase.getInstance().getReference("Gonderiler");
                        String gonderiId = veriYolu.push().getKey();

                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("gonderiId",gonderiId);
                        hashMap.put("gonderiResim",myUri);
                        hashMap.put("gonderiHakkinda",txtHakkında.getText().toString());
                        hashMap.put("gonderen", FirebaseAuth.getInstance().getCurrentUser().getUid());

                        veriYolu.child(gonderiId).setValue(hashMap);
                        progressDialog.dismiss();
                        startActivity(new Intent(PostActivity.this,AnasayfaActivity.class));
                        finish();
                    }else{
                        Toast.makeText(PostActivity.this, "Gönderme Başarısız !!!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PostActivity.this,AnasayfaActivity.class));
                        finish();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "Seçilen Resim Yok ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            resimuri = result.getUri();

            imageEklendi.setImageURI(resimuri);
        }else{
            Toast.makeText(this, "Resim Seçilemedi", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this,AnasayfaActivity.class));
            finish();
        }

    }
}