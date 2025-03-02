package com.example.gourmetisemobile

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

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
        val curseur = DataBase.rawQuery("SELECT * from bakery",arrayOf())

        curseur.moveToFirst()
        while (!curseur.isAfterLast()) {
            val siret = curseur.getString(curseur.getColumnIndex("siret"))
            val name = curseur.getString(curseur.getColumnIndex("name"))
            val street = curseur.getString(curseur.getColumnIndex("street"))
            val postalCode = curseur.getString(curseur.getColumnIndex("postal_code"))
            val city = curseur.getString(curseur.getColumnIndex("city"))
            val telephoneNumber = curseur.getString(curseur.getColumnIndex("telephone_number"))
            val bakery_description = curseur.getString(curseur.getColumnIndex("bakery_description"))
            val products_decription = curseur.getString(curseur.getColumnIndex("products_decription"))
            var bakery = Bakery()
            bakery.siret = siret
            bakery.name = name
            bakery.street = street
            bakery.postal_code = postalCode
            bakery.city = city
            bakery.telephone_number = telephoneNumber
            bakery.bakery_description = bakery_description
            bakery.products_decription = products_decription
            bakeries.add(bakery)
            curseur.moveToNext()
        }
        curseur.close()
        return bakeries
    }
}