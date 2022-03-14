package com.example.registrohoras;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Extras extends AppCompatActivity {
    TextView tvRecibido1, tvRecibido2, tvRecibido3, tvRecibido4, tvRecibido5;
    TextView tvCotizado1, tvCotizado2, tvCotizado3, tvCotizado4, tvCotizado5;
    EditText etRecibido1, etRecibido2, etRecibido3, etRecibido4, etRecibido5;
    EditText etCotizado1, etCotizado2, etCotizado3, etCotizado4, etCotizado5;
    Button btnCalcular;
    FloatingActionButton btnAgregar;
    TextView tvSueldoExtra;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extras);
        //Relaciono layout con backend
        tvRecibido1 = findViewById(R.id.tv_tituloRecibido);
        tvRecibido2 = findViewById(R.id.tv_tituloRecibido2);
        tvRecibido3 = findViewById(R.id.tv_tituloRecibido3);
        tvRecibido4 = findViewById(R.id.tv_tituloRecibido4);
        tvRecibido5 = findViewById(R.id.tv_tituloRecibido5);

        tvCotizado1 = findViewById(R.id.tv_tituloCotizado);
        tvCotizado2 = findViewById(R.id.tv_tituloCotizado2);
        tvCotizado3 = findViewById(R.id.tv_tituloCotizado3);
        tvCotizado4 = findViewById(R.id.tv_tituloCotizado4);
        tvCotizado5 = findViewById(R.id.tv_tituloCotizado5);

        etRecibido1 = findViewById(R.id.et_recibido);
        etRecibido2 = findViewById(R.id.et_recibido2);
        etRecibido3 = findViewById(R.id.et_recibido3);
        etRecibido4 = findViewById(R.id.et_recibido4);
        etRecibido5 = findViewById(R.id.et_recibido5);

        etCotizado1 = findViewById(R.id.et_cotizado);
        etCotizado2 = findViewById(R.id.et_cotizado2);
        etCotizado3 = findViewById(R.id.et_cotizado3);
        etCotizado4 = findViewById(R.id.et_cotizado4);
        etCotizado5 = findViewById(R.id.et_cotizado5);

        btnAgregar = findViewById(R.id.btn_flotante);
        btnCalcular = findViewById(R.id.btn_calcularExtras);

        tvSueldoExtra = findViewById(R.id.tv_extraTotal);

        //Pongo visibilidad GONE a los elementos necesarios
        tvCotizado2.setVisibility(View.GONE);
        tvRecibido2.setVisibility(View.GONE);
        etCotizado2.setVisibility(View.GONE);
        etRecibido2.setVisibility(View.GONE);
        tvCotizado3.setVisibility(View.GONE);
        tvRecibido3.setVisibility(View.GONE);
        etCotizado3.setVisibility(View.GONE);
        etRecibido3.setVisibility(View.GONE);
        tvCotizado4.setVisibility(View.GONE);
        tvRecibido4.setVisibility(View.GONE);
        etCotizado4.setVisibility(View.GONE);
        etRecibido4.setVisibility(View.GONE);
        tvCotizado5.setVisibility(View.GONE);
        tvRecibido5.setVisibility(View.GONE);
        etCotizado5.setVisibility(View.GONE);
        etRecibido5.setVisibility(View.GONE);

        //BARRA CON FLECHA HACIA ATRÁS
        ActionBar barra_accion = getSupportActionBar(); //Hago que aparezca la
        barra_accion.setDisplayHomeAsUpEnabled(true);   //flecha en la barra de accion

    }

    //FUNCION PARA CAMBIAR EL TITULO QUE SE VE EN LA ACTIVITY (LA REALIZA CUANDO SE EJECUTA)
    @Override
    protected void onStart() {
        super.onStart();
        setTitle("Sueldo extra");
    }

    //Al hacer click en CalcularExtras
    public void Calcular_sueldoExtra(View view){
        Double extraTotal = 0.0;
        String recibido1 = etRecibido1.getText().toString();
        String cotizado1 = etCotizado1.getText().toString();
        String recibido2 = etRecibido2.getText().toString();
        String cotizado2 = etCotizado2.getText().toString();
        String recibido3 = etRecibido3.getText().toString();
        String cotizado3 = etCotizado3.getText().toString();
        String recibido4 = etRecibido4.getText().toString();
        String cotizado4 = etCotizado4.getText().toString();
        String recibido5 = etRecibido5.getText().toString();
        String cotizado5 = etCotizado5.getText().toString();

        if (!recibido1.isEmpty() && !cotizado1.isEmpty()){
            Double extra1 = Double.valueOf(cotizado1) - Double.valueOf(recibido1);
            extraTotal += extra1;
        }else {
            Toast.makeText(this, "Completa ambos campos", Toast.LENGTH_SHORT).show();
        }

        if (!recibido2.isEmpty() && !cotizado2.isEmpty()){
            Double extra2 = Double.parseDouble(cotizado2) - Double.parseDouble(recibido2);
            extraTotal += extra2;
        }

        if (!recibido3.isEmpty() && !cotizado3.isEmpty()){
            Double extra3 = Double.parseDouble(cotizado3) - Double.parseDouble(recibido3);
            extraTotal += extra3;
        }

        if (!recibido4.isEmpty() && !cotizado4.isEmpty()){
            Double extra4 = Double.parseDouble(cotizado4) - Double.parseDouble(recibido4);
            extraTotal += extra4;
        }

        if (!recibido5.isEmpty() && !cotizado5.isEmpty()){
            Double extra5 = Double.parseDouble(cotizado5) - Double.parseDouble(recibido5);
            extraTotal += extra5;
        }
        tvSueldoExtra.setText("Te deben pagar "+extraTotal.toString()+" euros\n" +
                "en concepto de horas extras");
    }

    //Al hacer click en botón flotante
    public void AgregarCampo(View view){
        switch (i)
        {
            case 0:
                tvCotizado2.setVisibility(View.VISIBLE);
                tvRecibido2.setVisibility(View.VISIBLE);
                etCotizado2.setVisibility(View.VISIBLE);
                etRecibido2.setVisibility(View.VISIBLE);
                i += 1;
                break;
            case 1:
                tvCotizado3.setVisibility(View.VISIBLE);
                tvRecibido3.setVisibility(View.VISIBLE);
                etCotizado3.setVisibility(View.VISIBLE);
                etRecibido3.setVisibility(View.VISIBLE);
                i += 1;
                break;
            case 2:
                tvCotizado4.setVisibility(View.VISIBLE);
                tvRecibido4.setVisibility(View.VISIBLE);
                etCotizado4.setVisibility(View.VISIBLE);
                etRecibido4.setVisibility(View.VISIBLE);
                i += 1;
                break;
            case 3:
                tvCotizado5.setVisibility(View.VISIBLE);
                tvRecibido5.setVisibility(View.VISIBLE);
                etCotizado5.setVisibility(View.VISIBLE);
                etRecibido5.setVisibility(View.VISIBLE);
                i += 1;
                break;
            default:
                break;
        }
    }

}