package com.example.registrohoras;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class GuardarJornada extends AppCompatActivity {


    TextView tvFecha, tvValorHora2, tvHoras2;
    //TextView tvCodigo, tvCodigoMes, tvMul1; //provisorio para Debug
    EditText etValorHora, etHorasTrabajadas, etValorHora2, etHorasTrabajadas2;
    FloatingActionButton btnAgregar;
    //declaro los edit text del scroll view
    EditText et_valor1, et_valor2, et_valor3, et_valor4; //edit text de los distintos valores de hora
    EditText et_horas1, et_horas2, et_horas3, et_horas4; // edit text de las distintas horas trabajadas
    EditText et_plusDiario;

    int codigo = 0;
    int codigoMes = 0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guardar_jornada);

        //FRONTEND CON BACKEND DEL SCROLLVIEW
        //valores horas
        et_valor1 = findViewById(R.id.etv1);
        et_valor2 = findViewById(R.id.etv2);
        et_valor3 = findViewById(R.id.etv3);
        et_valor4 = findViewById(R.id.etv4);
        //horas trabajadas
        et_horas1 = findViewById(R.id.eth1);
        et_horas2 = findViewById(R.id.eth2);
        et_horas3 = findViewById(R.id.eth3);
        et_horas4 = findViewById(R.id.eth4);
        //Plus diario
        et_plusDiario = findViewById(R.id.et_plus);

        //Relacionar frontend con backend

        tvFecha = findViewById(R.id.tvFechaC);


        /*tvCodigo = findViewById(R.id.tvCodigoC); //provisorio para Debug
        tvCodigoMes = findViewById(R.id.tvCodigoMes);
        tvMul1 = findViewById(R.id.tvMul1);

         */

        etValorHora = findViewById(R.id.etValorHoraC);
        etHorasTrabajadas = findViewById(R.id.etHorasC);

        etValorHora2 = findViewById(R.id.etValorHoraC2);
        etHorasTrabajadas2 = findViewById(R.id.etHorasC2);
        etValorHora2.setVisibility(View.GONE);
        etHorasTrabajadas2.setVisibility(View.GONE);

        tvValorHora2 = findViewById(R.id.tvValorHora2);
        tvHoras2 = findViewById(R.id.tvHoras2);
        tvValorHora2.setVisibility(View.GONE);
        tvHoras2.setVisibility(View.GONE);

        //BARRA CON FLECHA HACIA ATRÁS
        ActionBar barra_accion = getSupportActionBar(); //Hago que aparezca la
        barra_accion.setDisplayHomeAsUpEnabled(true);   //flecha en la barra de accion

        btnAgregar = findViewById(R.id.btn_Agregar); //floating button




    }

    //FUNCION PARA CAMBIAR EL TITULO QUE SE VE EN LA ACTIVITY (LA REALIZA CUANDO SE EJECUTA)
    @Override
    protected void onStart() {
        super.onStart();
        setTitle("Registrar nuevas jornadas");


    }


    //FUNCION PARA MOSTRAR CALENDARIO
    public void MuestraCalendario(View view){
        Calendar calendario = Calendar.getInstance();
        int anyo = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);



        DatePickerDialog dpd = new DatePickerDialog(GuardarJornada.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String fecha = dayOfMonth + "/" + (month+1) + "/" + year;
                tvFecha.setText(fecha);
                codigo = Integer.parseInt(fecha.replaceAll("/", "")); //convierto a Int el String fecha
                                                                                        //pero reemplazando las barras por ningún espacio,
                                                                                        // osea que quitando las barras

                String s_codigoMes = (month+1) + "/" + year;
                codigoMes = Integer.parseInt(s_codigoMes.replaceAll("/", "")); // AGREGADA A LA TABLA SQLite
                                                                                                // la columna codigo_mes
                                                                                                    // en Clase Admin..
                et_valor1.setText("");
                et_valor2.setText("");
                et_valor3.setText("");
                et_valor4.setText("");

                et_horas1.setText("");
                et_horas2.setText("");
                et_horas3.setText("");
                et_horas4.setText("");

                et_plusDiario.setText("");

                /*
                etValorHora.setText("");
                etHorasTrabajadas.setText("");
                etValorHora2.setText("");
                etHorasTrabajadas2.setText("");
                etValorHora2.setVisibility(View.GONE);
                etHorasTrabajadas2.setVisibility(View.GONE);
                tvValorHora2.setVisibility(View.GONE);
                tvHoras2.setVisibility(View.GONE);
                 */
            }
        },anyo,mes,dia);
        dpd.show();
    }

    //FUNCION PARA REGISTRAR DATOS
    public void Registrar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "db_jornadas", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase(); //instancio BBDD

        //creo variables para luego agregar a la BBDD
        String fecha = tvFecha.getText().toString();
        String valorHora1 = et_valor1.getText().toString();
        String horasTrabajadas1 = et_horas1.getText().toString();

        //Gestiono los valores del segundo intervalo
        String valorHora2 = et_valor2.getText().toString();
        String horasTrabajadas2 = et_horas2.getText().toString();
        if (valorHora2.isEmpty()){
            valorHora2 = "0";
        }
        if (horasTrabajadas2.isEmpty()){
            horasTrabajadas2 = "0";
        }

        //Gestiono los valores del tercer intervalo
        String valorHora3 = et_valor3.getText().toString();
        String horasTrabajadas3 = et_horas3.getText().toString();
        if (valorHora3.isEmpty()){
            valorHora3 = "0";
        }
        if (horasTrabajadas3.isEmpty()){
            horasTrabajadas3 = "0";
        }

        //Gestiono los valores del cuarto intervalo
        String valorHora4 = et_valor4.getText().toString();
        String horasTrabajadas4 = et_horas4.getText().toString();
        if (valorHora4.isEmpty()){
            valorHora4 = "0";
        }
        if (horasTrabajadas4.isEmpty()){
            horasTrabajadas4 = "0";
        }

        //Gestiono los valores del plus diario
        String valorPlus = et_plusDiario.getText().toString();
        if (valorPlus.isEmpty()){
            valorPlus = "0";
        }

        //condicion para validar los campos ingresados
        if (!fecha.isEmpty() && !valorHora1.isEmpty() && !horasTrabajadas1.isEmpty()){
            //convierto para operar y multiplico para agregar multiplicacion a BBDD
            Double multiplicacion1 = Double.parseDouble(valorHora1) * Double.parseDouble(horasTrabajadas1);
            Double multiplicacion2 = Double.parseDouble(valorHora2) * Double.parseDouble(horasTrabajadas2);
            Double multiplicacion3 = Double.parseDouble(valorHora3) * Double.parseDouble(horasTrabajadas3);
            Double multiplicacion4 = Double.parseDouble(valorHora4) * Double.parseDouble(horasTrabajadas4);
            Double valorPlusDiario = Double.parseDouble(valorPlus);

            ContentValues registro = new ContentValues();

            //Agrego los datos al ContentValue
            registro.put("fecha", codigo);
            registro.put("valor_hora_1", valorHora1);
            registro.put("horas_trabajadas_1", horasTrabajadas1);
            registro.put("codigo_mes", codigoMes);
            registro.put("multiplicacion_1", multiplicacion1);
            registro.put("valor_hora_2", valorHora2);
            registro.put("horas_trabajadas_2", horasTrabajadas2);
            registro.put("multiplicacion_2", multiplicacion2);

            registro.put("valor_hora_3", valorHora3);
            registro.put("horas_trabajadas_3", horasTrabajadas3);
            registro.put("multiplicacion_3", multiplicacion3);

            registro.put("valor_hora_4", valorHora4);
            registro.put("horas_trabajadas_4", horasTrabajadas4);
            registro.put("multiplicacion_4", multiplicacion4);

            registro.put("valor_plus_diario", valorPlusDiario);

            //Agrego los datos del ContentValue a la tabla que está dentro de mi BBDD (jornadas)
            Long reg = baseDeDatos.insert("jornadas", "valor_hora_1", registro);

            baseDeDatos.close();

            tvFecha.setText("");

            et_valor1.setText("");
            et_valor2.setText("");
            et_valor3.setText("");
            et_valor4.setText("");

            et_horas1.setText("");
            et_horas2.setText("");
            et_horas3.setText("");
            et_horas4.setText("");

            et_plusDiario.setText("");


            Toast.makeText(getApplicationContext(), "Registrado correctamente '"+codigo+"'", Toast.LENGTH_SHORT).show();
            codigo = 0;
            codigoMes = 0;

        }else{
            Toast.makeText(getApplicationContext(), "Completa correctamente los campos", Toast.LENGTH_SHORT).show();
        }
    }

    //FUNCION PARA VISUALIZAR REGISTROS
    public void Visualizar(View view){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "db_jornadas", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        String fecha = tvFecha.getText().toString();

        if (!fecha.isEmpty()){
            Cursor fila = baseDeDatos.rawQuery
                    ("select valor_hora_1, horas_trabajadas_1, multiplicacion_1, " +
                            "valor_hora_2, horas_trabajadas_2, multiplicacion_2, " +
                            "valor_hora_3, horas_trabajadas_3, multiplicacion_3, " +
                            "valor_hora_4, horas_trabajadas_4, multiplicacion_4, " +
                            "codigo_mes, valor_plus_diario from jornadas where fecha= ' "+codigo+" ' ", null);
            if (fila.moveToFirst()){
                et_valor1.setText(fila.getString(0));
                et_horas1.setText(fila.getString(1));

                et_valor2.setText(fila.getString(3));
                et_horas2.setText(fila.getString(4));

                et_valor3.setText(fila.getString(6));
                et_horas3.setText(fila.getString(7));

                et_valor4.setText(fila.getString(9));
                et_horas4.setText(fila.getString(10));

                et_plusDiario.setText(fila.getString(13));

                baseDeDatos.close();
            }else{
                Toast.makeText(getApplicationContext(), "No hay horas registradas para esta fecha", Toast.LENGTH_SHORT).show();
                baseDeDatos.close();
            }


        }else{
            Toast.makeText(getApplicationContext(), "Primero introduce la fecha que deseas visualizar", Toast.LENGTH_SHORT).show();
        }

    }


    //FUNCION PARA BORRAR REGISTRO  ¡DEBERÍA AGREGAR CUADRO DE DIALOGO PARA EVITAR BORRAR POR ERROR!
    public void Borrar(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "db_jornadas", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        String fecha = tvFecha.getText().toString();

        if (!fecha.isEmpty()){
            int cantidad = baseDeDatos.delete("jornadas", "fecha= ' "+codigo+" '  ", null);

            baseDeDatos.close();

            et_valor1.setText("");
            et_valor2.setText("");
            et_valor3.setText("");
            et_valor4.setText("");

            et_horas1.setText("");
            et_horas2.setText("");
            et_horas3.setText("");
            et_horas4.setText("");

            et_plusDiario.setText("");

            codigo = 0;
            codigoMes = 0;




            if (cantidad==1){
                Toast.makeText(getApplicationContext(), "Fue borrado el registro", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getApplicationContext(), "No existe registro para esta fecha", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(getApplicationContext(), "Primero introduce la fecha que desea borrar", Toast.LENGTH_SHORT).show();
        }


    }

    //FUNCION PARA ACTUALIZAR REGISTROS
    public void Actualizar(View view) {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "db_jornadas", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase(); //instancio BBDD

        //creo variables para luego agregar a la BBDD
        String fecha = tvFecha.getText().toString();
        String valorHora1 = et_valor1.getText().toString();
        String horasTrabajadas1 = et_horas1.getText().toString();
        //Gestiono los valores del segundo intervalo
        String valorHora2 = et_valor2.getText().toString();
        String horasTrabajadas2 = et_horas2.getText().toString();
        if (valorHora2.isEmpty()){
            valorHora2 = "0";
        }
        if (horasTrabajadas2.isEmpty()){
            horasTrabajadas2 = "0";
        }

        //Gestiono los valores del 3er intervalo
        String valorHora3 = et_valor3.getText().toString();
        String horasTrabajadas3 = et_horas3.getText().toString();
        if (valorHora3.isEmpty()){
            valorHora3 = "0";
        }
        if (horasTrabajadas3.isEmpty()){
            horasTrabajadas3 = "0";
        }

        //Gestiono los valores del 4to intervalo
        String valorHora4 = et_valor4.getText().toString();
        String horasTrabajadas4 = et_horas4.getText().toString();
        if (valorHora4.isEmpty()){
            valorHora4 = "0";
        }
        if (horasTrabajadas4.isEmpty()){
            horasTrabajadas4 = "0";
        }

        //Gestiono los valores del plus diario
        String valorPlus = et_plusDiario.getText().toString();
        if (valorPlus.isEmpty()){
            valorPlus = "0";
        }

        //condicion para validar los campos ingresados
        if (!fecha.isEmpty() && !valorHora1.isEmpty() && !horasTrabajadas1.isEmpty()) {
            //convierto para agregar multiplicacion a BBDD
            Double multiplicacion1 = Double.parseDouble(valorHora1) * Double.parseDouble(horasTrabajadas1);
            Double multiplicacion2 = Double.parseDouble(valorHora2) * Double.parseDouble(horasTrabajadas2);
            Double multiplicacion3 = Double.parseDouble(valorHora3) * Double.parseDouble(horasTrabajadas3);
            Double multiplicacion4 = Double.parseDouble(valorHora4) * Double.parseDouble(horasTrabajadas4);
            Double valorPlusDiario = Double.parseDouble(valorPlus);

            ContentValues registro = new ContentValues();

            //Agrego los datos al ContentValue
            registro.put("fecha", codigo);
            registro.put("valor_hora_1", valorHora1);
            registro.put("horas_trabajadas_1", horasTrabajadas1);
            registro.put("codigo_mes", codigoMes);
            registro.put("multiplicacion_1", multiplicacion1);
            registro.put("valor_hora_2", valorHora2);
            registro.put("horas_trabajadas_2", horasTrabajadas2);
            registro.put("multiplicacion_2", multiplicacion2);

            registro.put("valor_hora_3", valorHora3);
            registro.put("horas_trabajadas_3", horasTrabajadas3);
            registro.put("multiplicacion_3", multiplicacion3);

            registro.put("valor_hora_4", valorHora4);
            registro.put("horas_trabajadas_4", horasTrabajadas4);
            registro.put("multiplicacion_4", multiplicacion4);

            registro.put("valor_plus_diario", valorPlusDiario);

            int cantidad = baseDeDatos.update("jornadas", registro,  "fecha= ' "+codigo+" '  ", null);
            baseDeDatos.close();

            if (cantidad==1){
                tvFecha.setText("");

                codigo = 0;
                codigoMes = 0;

                et_valor1.setText("");
                et_valor2.setText("");
                et_valor3.setText("");
                et_valor4.setText("");

                et_horas1.setText("");
                et_horas2.setText("");
                et_horas3.setText("");
                et_horas4.setText("");

                et_plusDiario.setText("");

                Toast.makeText(getApplicationContext(), "Registro actualizado", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(getApplicationContext(), "No hay registro para la fecha seleccionada, debes registrar uno primero", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(getApplicationContext(), "Completa correctamente todos los campos", Toast.LENGTH_SHORT).show();
            baseDeDatos.close();
        }

    }

    public void CuadroPrevio(View view){
        String fecha = tvFecha.getText().toString();
        if (!fecha.isEmpty()){
            showDialog(0);
        }else{
            Toast.makeText(getApplicationContext(), "Primero introduce la fecha que desea borrar", Toast.LENGTH_SHORT).show();
        }

    }


    //Esta función crea el cuadro de dialogo y llama al objeto listener (del tipo DListener) para manejar las acciones
    //del botón que el usuario selecione dentro del cuadro de diálogo.
    protected Dialog onCreateDialog(int id){
        Dialog dialog = null; //creo el cuadro de dialogo
        DListener listener = new DListener(); //creo nueva variable del tipo DListener
        AlertDialog.Builder builder = new AlertDialog.Builder(this); //creo nueva variable del tipo Alert...
        builder = builder.setMessage("¿Quieres borrar el registro seleccionado?"); //a la variable creada del tipo Alert...le coloco mensaje
        builder.setPositiveButton("Si", listener); //a la variable también le seteo el botón positivo (texto, llamada a la clase)
        builder.setNegativeButton("No", listener);//lo mismo pero para el botón negativo
        dialog = builder.create();//creo o relleno el cuadro de dialogo a partir de la variable del tipo AlertDialog.Builder
        return dialog;
    }

    //Esta clase maneja las acciones para cada boton que el usuario selecciona del cuadro de Dialogo
    class DListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialog, int which){
            //en este if manejo lo que hará la app si se selecciona el boton positivo, cerrará sesión e irá a la activity
            //de autenticación de usuario
            if (which == DialogInterface.BUTTON_POSITIVE){
                Borrar(); //LLAMO A LA FUNCION BORRAR
            }

            //en este if manejo el caso del boton negativo, simplemente cierra el cuadro de dialogo
            if (which == DialogInterface.BUTTON_NEGATIVE){
                dialog.dismiss();
            }
        }
    }



}