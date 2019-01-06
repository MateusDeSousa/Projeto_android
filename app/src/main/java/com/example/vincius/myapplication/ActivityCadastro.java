package com.example.vincius.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;


public class ActivityCadastro extends AppCompatActivity {

    private FirebaseAuth auth;
    private EditText editEmail, editSenha, editUsername;
    private Button btcadastrar, btSelectPhoto;
    private ImageView img_photo;
    private Uri uri_img_photo = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        startComponents();

        btSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selecionarFoto();
            }
        });

        btcadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editEmail.getText().toString();
                String senha = editSenha.getText().toString();
                String username = editUsername.getText().toString();
                criarUser(email, senha, username);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK) {
            if (data.getData() != null) {
                uri_img_photo = data.getData();
        }else{
            finish();
        }
            Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri_img_photo);
                    img_photo.setImageDrawable(new BitmapDrawable(bitmap));
                    btSelectPhoto.setAlpha(0);
                } catch (IOException e) {
                    finish();
                }
            }
        }
    private void selecionarFoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    private void criarUser(final String email, final String senha, final String username) {
        if (email == null || email.isEmpty() || senha == null || senha.isEmpty() || username == null || username.isEmpty()){
            alert("Nome, senha e email  devem ser preenchidos!");
            return;
        }
        if (uri_img_photo == null){
            alert("Coloque uma foto de Perfil!");
            return;
        }
            auth.createUserWithEmailAndPassword(email, senha)
                    .addOnCompleteListener(ActivityCadastro.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                alert("Usuario cadastrado com sucesso!");
                                salvarUserInFirebase();
                            } else {
                                alert("erro de cadastro!");
                            }
                        }
                    });
        }

    private void salvarUserInFirebase() {
        String filename = UUID.randomUUID().toString();
        final StorageReference ref = FirebaseStorage.getInstance().getReference("/images/" + filename);

        ref.putFile(uri_img_photo)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.i("teste", uri.toString());

                               String uid = FirebaseAuth.getInstance().getUid();
                               String username = editUsername.getText().toString();
                               String profileUrl = uri.toString();
                               User user =  new User(uid, username,profileUrl);

                                FirebaseFirestore.getInstance().collection("users")
                                        .add(user)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                Log.i("teste", documentReference.getId());
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.i("teste", e.getMessage());
                                            }
                                        });
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("teste", e.getMessage());
                    }
                });
    }


    private void alert(String menssagem){
        Toast.makeText(ActivityCadastro.this,menssagem,Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = Connect.getFirebaseAuth();
    }

    private void startComponents() {
        editUsername = findViewById(R.id.viewEditUsername2);
        editEmail = findViewById(R.id.viewEditEmail2);
        editSenha = findViewById(R.id.viewEditSenha2);
        btcadastrar = findViewById(R.id.btcadastrar);
        btSelectPhoto = findViewById(R.id.btphoto);
        img_photo = findViewById(R.id.img_photo);
    }
}
