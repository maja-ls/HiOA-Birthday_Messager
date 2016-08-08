package com.example.s198515_mappe2.databasen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import java.util.ArrayList;


/**
 * Databasehåndterer som jobber mot db
 */
public class DBHandler extends SQLiteOpenHelper {


    private static final String MYDEBUG = "DBHandler.java";

    private static int DATABASE_VERSION = 1;
    private static String DATABASE_NAME = "BursdagsDB";

    private static String TABLE_PERSONER = "Personer";

    private static String KEY_ID = "_ID";
    private static String KEY_FNAME = "Fornavn";
    private static String KEY_LNAME = "Etternavn";
    private static String KEY_MESSAGE = "Melding";
    private static String KEY_PHONE = "Telefon";
    private static String KEY_DATE = "Dato";




    public DBHandler (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String LAG_TABELL = "CREATE TABLE " + TABLE_PERSONER + "(" +
                KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                KEY_FNAME + " TEXT," +
                KEY_LNAME + " TEXT," +
                KEY_MESSAGE + " TEXT," +
                KEY_PHONE + " TEXT," +
                KEY_DATE + " TEXT" + ");";

        Log.d(MYDEBUG, "\n********\n\n LAG_TABELL = " + LAG_TABELL + " *******");

        db.execSQL(LAG_TABELL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSONER);

        onCreate(db);
    }


    public boolean leggTilPerson(Person p) {

        boolean ok;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        try {
            values.put(KEY_FNAME, p.getFornavn());
            values.put(KEY_LNAME, p.getEtternavn());
            values.put(KEY_MESSAGE, p.getMelding());
            values.put(KEY_PHONE, p.getTelefon());
            values.put(KEY_DATE, p.getBursdagForMaskin());

            db.insert(TABLE_PERSONER, null, values);


            Log.d(MYDEBUG, "\n********\n\n PERSON SATT INN OK *******");
            ok = true;
        }
        catch (Exception e) {
            Log.d(MYDEBUG, "\n********\n\n FEIL VED INNSETTING *******");
            ok = false;
        }


        db.close();
        return ok;
    }

    public boolean endrePerson(Person p) {

        boolean ok;
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        try {
            values.put(KEY_FNAME, p.getFornavn());
            values.put(KEY_LNAME, p.getEtternavn());
            values.put(KEY_MESSAGE, p.getMelding());
            values.put(KEY_PHONE, p.getTelefon());
            values.put(KEY_DATE, p.getBursdagForMaskin());

            int antallEndret = db.update(TABLE_PERSONER, values, KEY_ID + "= ?", new String[] {String.valueOf(p.getId())});

            Log.d(MYDEBUG, "\n********\n\n PERSON ENDRET OK *******");
            ok = true;
        }
        catch (Exception e) {
            Log.d(MYDEBUG, "\n********\n\n FEIL VED ENDRING *******");
            ok = false;
        }


        db.close();
        return ok;
    }


    public boolean slettPerson(Person p) {

        boolean ok;
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            db.delete(TABLE_PERSONER, KEY_ID + "= ?", new String[] {String.valueOf(p.getId())});

            Log.d(MYDEBUG, "\n********\n\n PERSON SLETTET OK *******");
            ok = true;
        }
        catch (Exception e) {
            Log.d(MYDEBUG, "\n********\n\n FEIL VED SLETTING *******");
            ok = false;
        }

        db.close();
        return ok;
    }


    public ArrayList<Person> finnAlleEllerNoenPersoner(String bursdagsdato) {
        ArrayList<Person> personer = new ArrayList<>();

        String sql = "SELECT * FROM " + TABLE_PERSONER;

        if (bursdagsdato != null) {
            sql += " WHERE " + KEY_DATE + " LIKE '%" + bursdagsdato + "'";
        }

        sql += " ORDER BY " + KEY_FNAME;


        Log.d(MYDEBUG, "\n********\n\n finnAlleSQL = " + sql + " *******");

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                Person p = new Person();
                p.setId(cursor.getInt(0));
                p.setFornavn(cursor.getString(1));
                p.setEtternavn(cursor.getString(2));
                p.setMelding(cursor.getString(3));
                p.setTelefon(cursor.getString(4));
                p.setBursdagMedStringForMaskin(cursor.getString(5));

                personer.add(p);
            }
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return personer;
    }







}
