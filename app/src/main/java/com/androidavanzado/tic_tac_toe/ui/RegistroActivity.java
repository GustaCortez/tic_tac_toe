package com.androidavanzado.tic_tac_toe.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.androidavanzado.tic_tac_toe.R;
import com.androidavanzado.tic_tac_toe.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistroActivity extends AppCompatActivity {
    EditText etName, etEmail,etPass;
    Button btnRegistro;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore db;
    String name, email, password;
    ProgressBar pbRegistro;
    ScrollView formRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        etName = findViewById(R.id.editTextName);
        etEmail = findViewById(R.id.editTexEmail);
        etPass = findViewById(R.id.editTextPassword);
        btnRegistro= findViewById(R.id.buttonRegistros);
        pbRegistro= findViewById(R.id.progressBarRegistro);
        formRegistro= findViewById(R.id.formRegistro);


        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
       changeRegistroFormVisibility(true);
        eventos();
    }


    private void eventos() {
        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 name = etName.getText().toString();
                 email =etEmail.getText().toString();
                 password =etPass.getText().toString();

                if (name.isEmpty()){
                etName.setError("el nombre es obligatorio");
                }else if(email.isEmpty()){
                    etEmail.setError("el email es obligatorio");
                }else if (password.isEmpty()){
                    etPass.setError("la contraseña es necesaria");
                }else{
                    //TODO: realizar registro  en Firebase Auth
                    createUser();
                }


            }
        });
    }

    private void createUser() {
        changeRegistroFormVisibility(false);
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                     if (task.isSuccessful()){
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        updateUI(user);
                     }else {
                         Toast.makeText(RegistroActivity.this, "error en el registro",Toast.LENGTH_LONG);
                         updateUI(null);
                     }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null){
            //almacenar la informacion del usuario en Firebase
            User nuevoUsuario = new User(name,0,0);


            db.collection("users")
                    .document(user.getUid())
                    .set(nuevoUsuario)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            //navegar hacia la siguiente pantalla de la aplicacion
                            finish();
                            Intent i = new Intent(RegistroActivity.this,FindGameActivity.class);
                            startActivity(i);
                        }
                    });



        }else{
            changeRegistroFormVisibility(true);
            etPass.setError("Nombre, email y/o contraseña incorrectos");
            etPass.requestFocus();
        }
    }

    public void changeRegistroFormVisibility(boolean showForm) {
        pbRegistro.setVisibility(showForm ? View.GONE : View.VISIBLE);
        formRegistro.setVisibility(showForm ? View.VISIBLE: View.GONE);
    }


}