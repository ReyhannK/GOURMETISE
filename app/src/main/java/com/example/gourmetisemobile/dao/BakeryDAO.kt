package com.example.gourmetisemobile.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.util.Log
import com.example.gourmetisemobile.GourmetiseHelper
import com.example.gourmetisemobile.dataclass.Bakery
import com.google.gson.Gson
import java.util.Date
import java.util.Locale

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
        v.put("code_ticket", bakery.codeTicket)
        v.put("date_evaluation", bakery.dateEvaluation)
        DataBase.insert("bakery", null, v)
    }

    @SuppressLint("Range")
    fun getBakeries(): MutableList<Bakery> {
        val bakeries = mutableListOf<Bakery>()
        val curseur = DataBase.rawQuery("SELECT * FROM bakery",arrayOf())

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

    fun updateBakery(bakery: Bakery, codeTicket: String) {

        val cursor = DataBase.query(
            "bakery",
            arrayOf("siret"),
            "siret = ?",
            arrayOf(bakery.siret),
            null, null, null
        )

        if (cursor.moveToFirst()) {
            cursor.close()

            val v = ContentValues()
            v.put("code_ticket", codeTicket)
            val currentDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault()).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }.format(Date())
            v.put("date_evaluation", currentDate)

            DataBase.update("bakery", v, "siret = ?", arrayOf(bakery.siret))
        }
        else {
            cursor.close()
            Log.e("SQLITE_ERROR", "Aucune boulangerie trouvée avec ce siret : ${bakery.siret}")
        }
    }

    @SuppressLint("Range")
    fun getSumNotes(bakery: Bakery): Int {
        var sum = 0
        val curseur = DataBase.rawQuery("SELECT value FROM note WHERE bakery_siret = ?",arrayOf(bakery.siret))

        curseur.moveToFirst()
        while (!curseur.isAfterLast()) {
            val value = curseur.getInt(curseur.getColumnIndex("value"))
            sum += value
            curseur.moveToNext()
        }
        curseur.close()
        return sum
    }

    @SuppressLint("Range")
    fun isCodeUse(code : String): Boolean {
        var used = false
        val cursor = DataBase.rawQuery("SELECT code_ticket FROM bakery WHERE code_ticket = ?",arrayOf(code))

        cursor.use{
            if(it.moveToFirst()){
                used = true
            }
        }
        return used
    }

    @SuppressLint("Range")
    fun getBakeryWithNotes(noteDAO: NoteDAO): List<Bakery> {
        val bakeryList = mutableListOf<Bakery>()

        val bakeryCursor: Cursor = DataBase.rawQuery(
            "SELECT DISTINCT bakery.* FROM bakery JOIN note ON bakery.siret = note.bakery_siret",
            null
        )

        if (bakeryCursor.moveToFirst()) {
            do {
                val siret = bakeryCursor.getString(bakeryCursor.getColumnIndex("siret"))
                val codeTicket = bakeryCursor.getString(bakeryCursor.getColumnIndex("code_ticket"))
                val dateEvaluation = bakeryCursor.getString(bakeryCursor.getColumnIndex("date_evaluation"))

                val notes = noteDAO.getNotesForBakery(siret)

                bakeryList.add(Bakery(siret= siret, codeTicket= codeTicket, dateEvaluation = dateEvaluation, notes = notes))
            } while (bakeryCursor.moveToNext())
        }
        bakeryCursor.close()

        return bakeryList
    }
}
