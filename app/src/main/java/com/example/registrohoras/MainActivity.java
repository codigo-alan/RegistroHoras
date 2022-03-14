package com.example.registrohoras;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    String sEmail, sPassword;
    EditText etUsuario, etPassword;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //relaciono layout con logica
        etUsuario = findViewById(R.id.etMail);
        etPassword = findViewById(R.id.etPassword);


        mAuth = FirebaseAuth.getInstance();

    }

    //FUNCION PARA CAMBIAR EL TITULO QUE SE VE EN LA ACTIVITY (LA REALIZA CUANDO SE EJECUTA) y VER SI
    //USUARIO ESTABA LOGUEADO
    @Override
    protected void onStart() {
        super.onStart();
        setTitle("Autenticar usuario");
        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), CalculadoraMensual.class));
            finish();
        }
    }


    //AL PRESIONAR BUTTON INICIAR SESIÓN
    public void IniciarSesion(View view){
        sEmail = etUsuario.getText().toString();
        sPassword = etPassword.getText().toString();

        if (!sEmail.isEmpty() && !sPassword.isEmpty()){
            loginUser();
        }else {
            Toast.makeText(getApplicationContext(), "Debes completar ambos campos", Toast.LENGTH_SHORT).show();
        }

    }

    //FUNCION PARA LOGEAR USUARIO (INICIO DE SESION)
    private void loginUser(){
        mAuth.signInWithEmailAndPassword(sEmail, sPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(getApplicationContext(), CalculadoraMensual.class)); // voy a activity Calculadora
                    finish(); //finalizar el proceso, para que no pueda volver atrás con la flecha el usuario

                }else {
                    Toast.makeText(getApplicationContext(), "No se pudo iniciar sesión", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void RegistrarUsuario(View view){
        Intent i = new Intent(this, RegistroUsuarioNuevo.class);
        startActivity(i);
    }

}