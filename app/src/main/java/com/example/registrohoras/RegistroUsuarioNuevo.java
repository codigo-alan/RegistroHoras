package com.example.registrohoras;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistroUsuarioNuevo extends AppCompatActivity {

    EditText etUserNuevo, etPassNuevo;
    FirebaseAuth mAuth;
    String usuario = "";
    String password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario_nuevo);

        //ralaciono layout con la lógica
        etUserNuevo = findViewById(R.id.etMailNuevo);
        etPassNuevo = findViewById(R.id.etPassNuevo);

        //BARRA CON FLECHA HACIA ATRÁS
        ActionBar barra_accion = getSupportActionBar(); //Hago que aparezca la
        barra_accion.setDisplayHomeAsUpEnabled(true);   //flecha en la barra de accion

        //instancio autenticacion firebase
        mAuth = FirebaseAuth.getInstance();
    }

    //FUNCION PARA CAMBIAR EL TITULO QUE SE VE EN LA ACTIVITY (LA REALIZA CUANDO SE EJECUTA)
    @Override
    protected void onStart() {
        super.onStart();
        setTitle("Crear nuevo usuario");
        //String sEmail = mAuth.getCurrentUser().getEmail();
        //txt_mail.setText(sEmail);
    }


    //AL PRESIONAR BOTON REGISTRAR
    public void buttonRegistrar(View view){
        usuario = etUserNuevo.getText().toString();
        password = etPassNuevo.getText().toString();

        if (!usuario.isEmpty() && !password.isEmpty()){
            if (password.length() >= 6){
                RegisterUser();
            }else {
                Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Tienes que completar ambos campos", Toast.LENGTH_SHORT).show();
        }
    }

    //Método para registrar usuario en Autenticacion de Firebase
    public void RegisterUser(){
        mAuth.createUserWithEmailAndPassword(usuario, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Intent i = new Intent(getApplicationContext(), CalculadoraMensual.class);
                    startActivity(i);
                    finish(); //para que el usuario no pueda volver a esta activity con la flecha
                }else {
                    Toast.makeText(getApplicationContext(), "No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

