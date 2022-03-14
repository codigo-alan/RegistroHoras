package com.example.registrohoras;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper {
    public AdminSQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creo tabla en BBDD
        db.execSQL
                ("create table jornadas(fecha int primary key, " +
                        "valor_hora_1 real, horas_trabajadas_1 int, multiplicacion_1 real, " +
                        "valor_hora_2 real, horas_trabajadas_2 int, multiplicacion_2 real, " +
                        "valor_hora_3 real, horas_trabajadas_3 int, multiplicacion_3 real, " +
                        "valor_hora_4 real, horas_trabajadas_4 int, multiplicacion_4 real, " +
                        "codigo_mes int, valor_plus_diario real)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS jornadas"); //CREO QUE SI EXISTE UNA TABLA CON ESE NOMBRE, LA ELIMINA
        onCreate(db);
    }
}
