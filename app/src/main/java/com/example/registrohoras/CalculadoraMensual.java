package com.example.registrohoras;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.ajts.androidmads.library.SQLiteToExcel;
import com.google.firebase.auth.FirebaseAuth;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;



public class CalculadoraMensual extends AppCompatActivity {

    TextView tvMes, tvResultado, tvDias;

    TextView tvPath; //DEBUG

    int codigoMes_Calculadora = 0;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora_mensual);

        //relaciono layout con lógica
        tvMes = findViewById(R.id.tvMes);
        tvResultado = findViewById(R.id.tvResultado);
        tvDias = findViewById(R.id.tvDias);





        //Instancia firebase autentication
        mAuth = FirebaseAuth.getInstance();

        /*NO ME FUNCIONA, VER CICLOS DE ACTIVITIES
        //LLamo a la función para que se actualicen los datos sin necesidad de volver a seleccionar fecha
        String s_tvMes = tvMes.getText().toString();
        if (!s_tvMes.isEmpty()){
            CalcularSueldo();
        }
        */
    }

    //FUNCION PARA CAMBIAR EL TITULO QUE SE VE EN LA ACTIVITY (LA REALIZA CUANDO SE EJECUTA)
    @Override
    protected void onStart() {
        super.onStart();
        setTitle("Calcular sueldo mensual");

    }

    //FUNCION PARA MOSTRAR CALENDARIO
    public void MuestraCalendario(View view){
        // obtengo instancia del calendario
        final Calendar today = Calendar.getInstance();

        //El codigo de GitHub
        MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(CalculadoraMensual.this,
                new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {
                        String month_year = (selectedMonth+1) + "/" + selectedYear;
                        tvMes.setText(month_year);
                        codigoMes_Calculadora = Integer.parseInt(month_year.replaceAll("/", ""));

                        //Llamo a la función de calcular sueldo
                        CalcularSueldo();
                    }

                    }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

        builder.setActivatedMonth(today.get(Calendar.MONTH))
                .setMinYear(2017)
                .setActivatedYear(today.get(Calendar.YEAR))
                .setMaxYear(2025)
                //.setMinMonth(Calendar.APRIL)
                //.setMaxMonth(Calendar.OCTOBER)
                .setTitle("Seleccionar el mes para calcular sueldo")
                .build().show();
    }

    public void CalcularSueldo(){
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "db_jornadas", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        String fecha = tvMes.getText().toString();
        List<Double> lista = new ArrayList<>(); //creo lista donde iré agregando los datos que me arroje la BBDD

        if (!fecha.isEmpty()){

            //obtengo los valores de la columna multiplicacion_1,2,3 y 4 de la BBDD, de los registros que tengan el codigo_mes seleccionado en calendario
            //es decir, todos los registros de multiplicacion_1,2,3,y 4 que haya en el mes.
            // Para esto creo un Cursor que se moverá por la BBDD

            Cursor fila = baseDeDatos.rawQuery
                    ("select multiplicacion_1, multiplicacion_2, multiplicacion_3, multiplicacion_4, valor_plus_diario " +
                            "from jornadas where codigo_mes= ' "+codigoMes_Calculadora+" ' ", null);

            if (fila.moveToFirst()){ //condicional si es que el cursor se mueve a un primer registro dentro de mi BBDD
                //ACA VOY REGISTRANDO TODAS LAS multiplicacion_1,2,3 y 4 Y LUEGO LAS SUMO
                int i = 0;
                do {
                    //agrego a lista el dato que obtengo del cursor, obtenido gracias a la Query realizada antes
                    lista.add(fila.getDouble(0)); //será el dato correspondiente a multiplicacion_1
                    lista.add(fila.getDouble(1)); //será el dato correspondiente a multiplicacion_2
                    lista.add(fila.getDouble(2)); //será el dato correspondiente a multiplicacion_3
                    lista.add(fila.getDouble(3)); //será el dato correspondiente a multiplicacion_4
                    lista.add(fila.getDouble(4)); //será el dato correspondiente a valor_plus_diario
                    i++;
                }while (fila.moveToNext()); // el codigo dentro del do lo hago mientras que el cursor
                                            // se mueva a un siguiente registro en mi BBDD

                fila.close(); //cierro el cursor
                baseDeDatos.close(); //cierro la instancia de la BBDD abierta anteriormente

                //En este for, para cada item dentro de la lista, voy incrementando el valor del sueldo
                //es decir, voy sumando los valores que tenga la lista
                Double sumaMensual = 0.0;
                for (Double item : lista){
                    sumaMensual += item;
                }

                tvResultado.setText("€ "+Double.toString(sumaMensual)+"");
                tvDias.setText("Tienes registrados "+Integer.toString(i)+" días de trabajo en el mes seleccionado");


            }else {
                Toast.makeText(this, "No hay ningún registro para el mes seleccionado", Toast.LENGTH_SHORT).show();
                baseDeDatos.close(); //cierro la instancia de la BBDD abierta anteriormente
                tvResultado.setText("Sin registros");
                tvDias.setText("");
            }
        }else{
            Toast.makeText(this, "Primero selecciona el mes que quieres calcular", Toast.LENGTH_SHORT).show();
            tvResultado.setText("");
            tvDias.setText("");
        }

    }

    //Al presionar sueldo extra
    public  void Sueldo_extra(View view){
        Intent i = new Intent(this,Extras.class);
        startActivity(i);
    }

    //Al presionar botton de BOLSA HORAS
    public void Bolsa_horas(View view){
        Intent i = new Intent(this,BolsaHoras.class);
        startActivity(i);
    }

    //Al presionar botton de REGISTRO DE JORNADA
    public void Registros_jornadas(View view){
        Intent i = new Intent(this,GuardarJornada.class);
        startActivity(i);
    }

    //Para cerrar sesión. También agregar cuadro de diálogo
    public void CerrarSesion(View view){
        showDialog(0);
    }

    //Esta función crea el cuadro de dialogo y llama al objeto listener (del tipo DListener) para manejar las acciones
    //del botón que el usuario selecione dentro del cuadro de diálogo.
    protected Dialog onCreateDialog(int id){
        Dialog dialog = null; //creo el cuadro de dialogo
        DListener listener = new DListener(); //creo nueva variable del tipo DListener
        AlertDialog.Builder builder = new AlertDialog.Builder(this); //creo nueva variable del tipo Alert...
        builder = builder.setMessage("Tu sesión se cerrará ¿Continuar?"); //a la variable creada del tipo Alert...le coloco mensaje
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
                mAuth.signOut(); //Cierro sesión del usuario
                Toast.makeText(getApplicationContext(),"Cerrando sesión... ",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MainActivity.class)); //voy a la activity MainActivity
                finish(); //finalizo el proceso, o algo asi
            }

            //en este if manejo el caso del boton negativo, simplemente cierra el cuadro de dialogo
            if (which == DialogInterface.BUTTON_NEGATIVE){
                dialog.dismiss();
            }
        }
    }

    //AL HACER CLICK EN EL BOTON DE EXCEL
    public void ButtonExcel(View view){
        Exportar_a_Excel();
    }

    /*IMPLEMENTACIÓN FALLIDA DEL BOTON PARA ENVIAR MAIL CON ARCHIVO ADJUNTO EXCEL

    public void EnviarMail (View view){

        Exportar_a_Excel();

        //Creo el Uri
        /*
        File file = new File(tvPath.getText().toString());
        Uri uri = Uri.fromFile(file);
        //tampoco me funciona
        File file = new File("Redmi Note 9S/Android/data/com.example.registrohoras/files/jornadas.xlsx");
        Uri uri = Uri.fromFile(file);




        Intent email = new Intent();
        email.setAction(Intent.ACTION_SEND);
        email.setData(Uri.parse("mailto:"));
        email.setType("application/excel");
        email.putExtra(Intent.EXTRA_EMAIL, "alanfmarcos@gmail.com");
        email.putExtra(Intent.EXTRA_SUBJECT,"Consulta control de horas.");
        email.putExtra(Intent.EXTRA_TEXT,"Hola, adjunto documento Excel donde llevo registro de los días" +
                "trabajados y sus respectivas horas de realizadas, incluso especificando el valor (en euros) de dichas horas.");
        //email.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(email,"Send Email"));

    }*/


    //EXPORTA LA BBDD A UN EXCEL QUE SE GUARDA EN AlmacenamientoInterno-->Android-->data-->com.example.RegistroHoras-->files-->jornadas.xlsx
    //para poder realizar esto tuve que colocar una DEPENDENCIA de un repositorio de GitHub de una persona
    public void Exportar_a_Excel(){

       String path = getExternalFilesDir(null).getAbsolutePath(); //para Android 10 o superiores, en Android menores quizas no funcione


        SQLiteToExcel sqLiteToExcel = new SQLiteToExcel(getApplicationContext(), "db_jornadas", path);
        sqLiteToExcel.exportSingleTable("jornadas", "jornadas.xlsx", new SQLiteToExcel.ExportListener() {
            @Override
            public void onStart() {
                Toast.makeText(getApplicationContext(), "Generando reporte...", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onCompleted(String filePath) {
                Toast.makeText(getApplicationContext(), "Reporte creado en la dirección: '"+filePath+"'", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onError(Exception e) {
                Toast.makeText(getApplicationContext(), "Error al crear reporte:'"+e.getMessage()+"'", Toast.LENGTH_LONG).show();

            }
        });
    }

}