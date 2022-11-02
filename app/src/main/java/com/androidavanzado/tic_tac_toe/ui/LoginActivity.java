package com.androidavanzado.tic_tac_toe.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.androidavanzado.tic_tac_toe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity<showForm> extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegistro;
    private ScrollView formLogin;
    private ProgressBar pbLogin;
    private FirebaseAuth firebaseAuth;
    private String email,password;
    boolean tryLogin = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.editTexEmail);
        etPassword = findViewById(R.id.editTextPassword);
        btnLogin =findViewById(R.id.buttonLogin);
        formLogin =findViewById(R.id.formLogin);
        pbLogin = findViewById(R.id.progressBarLogin);
        btnRegistro = findViewById(R.id.buttonRegistro);

        firebaseAuth = FirebaseAuth.getInstance();

        changeLoginFormVisibility(false);
        eventos();
    }

    private void eventos() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();

               if(email.isEmpty()){
                    etEmail.setError("el email es obligatorio");
                }else if (password.isEmpty()){
                   etPassword.setError("la contraseña es necesaria");
                }else{
                    //TODO: REALIZAR EL login EN FIREBASE AUNTENTICACION

                   changeLoginFormVisibility(false);
                }

                loginUser();
            }
        });

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,RegistroActivity.class);
                startActivity(i);
            }
        });
    }

    private void loginUser() {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        tryLogin = true;
                    if(task.isSuccessful()){
                        FirebaseUser user =firebaseAuth.getCurrentUser();
                        updateUI(user);
                    }else{
                        Log.w("TAG","singInError: ",task.getException());
                       updateUI(null);
                    }

                    }
                });
    }


    public void changeLoginFormVisibility(boolean showForm) {
        btnLogin.setVisibility(showForm ? View.GONE : View.VISIBLE);
        formLogin.setVisibility(showForm ? View.VISIBLE: View.GONE);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null){
            //almacenar la informacion del usuario en Firebase
            //TODO

            //navegar hacia la siguiente pantalla de la aplicacion
            Intent i = new Intent(LoginActivity.this,FindGameActivity.class);
            startActivity(i);
        }else{
            changeLoginFormVisibility(true);
            if (tryLogin) {
                etPassword.setError("Nombre, email y/o contraseña incorrectos");
                etPassword.requestFocus();
            }
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        //comprobamos si previamente el usuario ya ha iniciado sesion
        //en este dispositivo
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        updateUI(currentUser);
    }
}
