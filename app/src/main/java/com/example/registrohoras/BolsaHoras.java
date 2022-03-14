
package com.example.registrohoras;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.util.SystemOutLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BolsaHoras extends AppCompatActivity {

    TextView tvHorasTotal, tvDiasTotal, tvExtras;
    //DEBUG
    TextView tvSumaHorasMes_debug;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bolsa_horas);

        //UI CON BACKEND
        tvHorasTotal = findViewById(R.id.tv_horasTotal);
        tvDiasTotal = findViewById(R.id.tv_diasTotal);
        tvExtras = findViewById(R.id.tv_extras);
        //DEBUG
        tvSumaHorasMes_debug = findViewById(R.id.tv_sumaHorasMes_debug);
        //BARRA CON FLECHA HACIA ATRÁS
        ActionBar barra_accion = getSupportActionBar(); //Hago que aparezca la
        barra_accion.setDisplayHomeAsUpEnabled(true);   //flecha en la barra de accion
        //LLAMO A LA FUNCIÓN VisualizarHoras
        VisualizarHoras();
    }

    //FUNCION PARA CAMBIAR EL TITULO QUE SE VE EN LA ACTIVITY (LA REALIZA CUANDO SE EJECUTA)
    @Override
    protected void onStart() {
        super.onStart();
        setTitle("Bolsa de horas extra");
    }

    //FUNCION PARA VISUALIZAR TOTAL DE HORAS
    public void VisualizarHoras() {
        AdminSQLiteOpenHelper admin = new AdminSQLiteOpenHelper(this, "db_jornadas", null, 1);
        SQLiteDatabase baseDeDatos = admin.getWritableDatabase();

        //Obtengo lista de tuplas. Cada tupla tendrá la info (sumaHoras, codigoMes) de cada jornada que haya registrada
        Double sumaTotal = 0.0;//para saber el total de horas trabajadas
        Integer diasTrabajados = 0;//total de dias trabajados
        List<Pair<Double, Integer>> listaJornadas = new ArrayList<>();//almacenará tupla con (horas,codigoMes) por cada jornada

        Cursor fila = baseDeDatos.rawQuery
                ("select horas_trabajadas_1, " +
                        "horas_trabajadas_2, " +
                        "horas_trabajadas_3, " +
                        "horas_trabajadas_4, " +
                        "codigo_mes " +
                        "from jornadas", null);
        if (fila.moveToFirst()) {
            do{
                Double dHoras1 = Double.parseDouble(fila.getString(0));
                Double dHoras2 = Double.parseDouble(fila.getString(1));
                Double dHoras3 = Double.parseDouble(fila.getString(2));
                Double dHoras4 = Double.parseDouble(fila.getString(3));
                Integer codigoMesJornada = Integer.parseInt(fila.getString(4));

                Double sumaJornada = dHoras1 + dHoras2 + dHoras3 + dHoras4;//para total de horas por jornada
                Pair<Double, Integer> tuplaJornada = Pair.create(sumaJornada, codigoMesJornada);//new Pair<Double, Integer>(sumaJornada, codigoMesJornada);//tupla con suma de la jornada y su codigo mes
                listaJornadas.add(tuplaJornada);
                sumaTotal += (dHoras1 + dHoras2 + dHoras3 + dHoras4);
                diasTrabajados++;
            }while (fila.moveToNext());
            fila.close();
            baseDeDatos.close();

            //Obtengo una Lista con los meses que tienen jornadas registradas
            List<Integer> listaCodigosMes = new ArrayList<Integer>();//almacenará los meses que contienen datos, para luego iterar
            for (Pair<Double, Integer> elemento : listaJornadas)
            {
                if (!listaCodigosMes.contains(elemento.second)){
                    listaCodigosMes.add(elemento.second);
                }
            }

            /*DEBUG. Muestro lista de jornadas
            System.out.println("Lista de jornadas: "+listaJornadas);
            System.out.println("Lista meses sin repetir: "+listaCodigosMes);
            System.out.println("*****************************");
             */

            //Itero sobre lista de meses
            Double extrasTotales = 0.0;
            for (Integer mes : listaCodigosMes){
                Double sumaHorasMes = 0.0;
                Double extrasMes = 0.0;
                //Itero sobre lista de jornadas
                for (Pair<Double, Integer> elemento : listaJornadas){
                    //si el segundo valor del elemento Pair es igual al mes del 1er for
                    if (elemento.second == mes.intValue()){
                        sumaHorasMes += elemento.first;
                        /*DEBUG
                        System.out.println("elemento first de "+elemento.second+": "+elemento.first);
                         */
                        /*DEBUG
                        System.out.println("tupla de "+elemento.second+": "+sumaMes_codigoMes);
                        System.out.println("lista provisoria "+elemento.second+": "+listaSumasMes);
                        System.out.println("*****************************");
                         */
                    }
                }
                extrasMes = sumaHorasMes - 164.0;
                if (extrasMes > 0.0){
                    extrasTotales += extrasMes;
                }
            }
            /*DEBUG
            System.out.println("*****************************");
            System.out.println("FINALextrasTotales: "+extrasTotales);
             */
            //Printeo en pantalla
            tvHorasTotal.setText(sumaTotal.toString());
            tvDiasTotal.setText(diasTrabajados.toString());
            tvExtras.setText(extrasTotales.toString());
        }else{
            Toast.makeText(getApplicationContext(), "No hay horas registradas", Toast.LENGTH_SHORT).show();
            fila.close();
            baseDeDatos.close();
            tvHorasTotal.setText("");
            tvDiasTotal.setText("");
            tvExtras.setText("");
        }
    }
}








   