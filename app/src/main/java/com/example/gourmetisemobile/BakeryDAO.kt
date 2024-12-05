package com.example.gourmetisemobile

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.provider.ContactsContract.Data

class BakeryDAO (context : Context) {
    lateinit var DataBase : SQLiteDatabase
    lateinit var Helper : GourmetiseHelper

    init {
        Helper = GourmetiseHelper(context);
        DataBase = Helper.writableDatabase;
    }

    fun addBakery(bakery: Bakery) {
        val v = ContentValues()
        v.put("siret", bakery.siret)
        v.put("name", bakery.name)
        v.put("street", bakery.street)
        v.put("postal_code", bakery.postal_code)
        v.put("city", bakery.city)
        v.put("telephone_number", bakery.telephone_number)
        v.put("bakery_description", bakery.bakery_description)
        v.put("products_decription", bakery.products_decription)
        DataBase.insert("bakery", null, v)
    }

    @SuppressLint("Range")
    fun getBakeries(): MutableList<Bakery> {
        val bakeries = mutableListOf<Bakery>()
        val curseur = DataBase.rawQuery("SELECT name,street, postal_code, city,telephone_number from bakery",arrayOf())

        curseur.moveToFirst()
        while (!curseur.isAfterLast()) {
            val name = curseur.getString(curseur.getColumnIndex("name"))
            val street = curseur.getString(curseur.getColumnIndex("street"))
            val postal_code = curseur.getString(curseur.getColumnIndex("postal_code"))
            val city = curseur.getString(curseur.getColumnIndex("city"))
            val telephone_number = curseur.getString(curseur.getColumnIndex("telephone_number"))
            var bakery = Bakery()
            bakery.name = name
            bakery.street = street
            bakery.postal_code = postal_code
            bakery.city = city
            bakery.telephone_number = telephone_number
            bakeries.add(bakery)
            curseur.moveToNext()
        }
        curseur.close()
        return bakeries
    }

}