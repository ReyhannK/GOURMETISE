package com.example.gourmetisemobile

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class GourmetiseHelper (context : Context)
    : SQLiteOpenHelper (context, "gourmetise.db", null, 1){
    override fun onCreate(db: SQLiteDatabase) {
        // création de la table BAKERY
        db.execSQL("CREATE TABLE bakery ("
                + "siret TEXT NOT NULL PRIMARY KEY,"
                + "name TEXT NOT NULL,"
                + "street TEXT NOT NULL,"
                + "postal_code TEXT NOT NULL,"
                + "city TEXT NOT NULL,"
                + "telephone_number TEXT NOT NULL,"
                + "bakery_description TEXT NOT NULL,"
                + "products_decription TEXT NOT NULL);");
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS bakery;");
        onCreate(db);
    }
}