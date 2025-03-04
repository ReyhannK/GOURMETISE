package com.example.gourmetisemobile.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.gourmetisemobile.GourmetiseHelper
import com.example.gourmetisemobile.dataclass.Bakery

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
        v.put("postal_code", bakery.postalCode)
        v.put("city", bakery.city)
        v.put("telephone_number", bakery.telephoneNumber)
        v.put("bakery_description", bakery.bakeryDescription)
        v.put("products_decription", bakery.productsDecription)
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
            bakery.postalCode = postalCode
            bakery.city = city
            bakery.telephoneNumber = telephoneNumber
            bakery.bakeryDescription = bakery_description
            bakery.productsDecription = products_decription
            bakeries.add(bakery)
            curseur.moveToNext()
        }
        curseur.close()
        return bakeries
    }
}