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
        val curseur = DataBase.rawQuery("SELECT name,street, postal_code, city,telephone_number from bakery",arrayOf())

        curseur.moveToFirst()
        while (!curseur.isAfterLast()) {
            val name = curseur.getString(curseur.getColumnIndex("name"))
            val street = curseur.getString(curseur.getColumnIndex("street"))
            val postalCode = curseur.getString(curseur.getColumnIndex("postal_code"))
            val city = curseur.getString(curseur.getColumnIndex("city"))
            val telephoneNumber = curseur.getString(curseur.getColumnIndex("telephone_number"))
            var bakery = Bakery()
            bakery.name = name
            bakery.street = street
            bakery.postal_code = postalCode
            bakery.city = city
            bakery.telephone_number = telephoneNumber
            bakeries.add(bakery)
            curseur.moveToNext()
        }
        curseur.close()
        return bakeries
    }

    @SuppressLint("Range")
    fun getBakeriesByName(name: String): MutableList<Bakery> {
        val bakeries = mutableListOf<Bakery>()
        val cursor = DataBase.rawQuery(
            "SELECT name, street, postal_code, city, telephone_number FROM bakery WHERE name = ?",
            arrayOf(name)
        )

        try {
            cursor.moveToFirst()
            while (!cursor.isAfterLast) {
                val bakeryName = cursor.getString(cursor.getColumnIndex("name"))
                val street = cursor.getString(cursor.getColumnIndex("street"))
                val postalCode = cursor.getString(cursor.getColumnIndex("postal_code"))
                val city = cursor.getString(cursor.getColumnIndex("city"))
                val telephoneNumber = cursor.getString(cursor.getColumnIndex("telephone_number"))

                val bakery = Bakery().apply {
                    this.name = bakeryName
                    this.street = street
                    this.postal_code = postalCode
                    this.city = city
                    this.telephone_number = telephoneNumber
                }
                bakeries.add(bakery)
                cursor.moveToNext()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor.close()
        }

        return bakeries
    }


}