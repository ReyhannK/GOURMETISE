package com.example.gourmetisemobile

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class GourmetiseHelper (context : Context)
    : SQLiteOpenHelper (context, "gourmetise.db", null, 1){
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("PRAGMA foreign_keys = ON;");

        db.execSQL("CREATE TABLE bakery ("
                + "siret TEXT NOT NULL PRIMARY KEY,"
                + "name TEXT NOT NULL,"
                + "street TEXT NOT NULL,"
                + "postal_code TEXT NOT NULL,"
                + "city TEXT NOT NULL,"
                + "telephone_number TEXT NOT NULL,"
                + "bakery_description TEXT NOT NULL,"
                + "products_decription TEXT NOT NULL,"
                + "code_ticket TEXT NOT NULL,"
                + "date_evaluation TEXT NOT NULL);");

        db.execSQL("CREATE TABLE criteria ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "label TEXT NOT NULL);");

        db.execSQL("CREATE TABLE contestParams ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "startRegistration TEXT NOT NULL,"
                + "endRegistration TEXT NOT NULL,"
                + "startEvaluation TEXT NOT NULL,"
                + "endEvaluation TEXT NOT NULL);");

        db.execSQL("CREATE TABLE note ("
                + "bakery_siret TEXT NOT NULL,"
                + "criteria_id INTEGER NOT NULL,"
                + "value INTEGER NOT NULL,"
                + "PRIMARY KEY(bakery_siret, criteria_id),"
                + "FOREIGN KEY(bakery_siret) REFERENCES bakery(siret),"
                + "FOREIGN KEY(criteria_id) REFERENCES criteria(id));");

        db.execSQL("INSERT INTO criteria (label) VALUES ('reception');");
        db.execSQL("INSERT INTO criteria (label) VALUES ('product');");
        db.execSQL("INSERT INTO criteria (label) VALUES ('decoration');");

    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS bakery;");
        db.execSQL("DROP TABLE IF EXISTS contestParams;");
        onCreate(db);
    }
}