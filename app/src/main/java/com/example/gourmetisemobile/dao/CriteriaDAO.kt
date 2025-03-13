package com.example.gourmetisemobile.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.gourmetisemobile.GourmetiseHelper
import com.example.gourmetisemobile.dataclass.Note

class CriteriaDAO(context: Context) {
    lateinit var DataBase : SQLiteDatabase
    lateinit var Helper : GourmetiseHelper

    init {
        Helper = GourmetiseHelper(context);
        DataBase = Helper.writableDatabase;
    }

    @SuppressLint("Range")
    fun getAllCriteria(): Map<Int,Pair<String,Int>>  {
        val cursor = DataBase.rawQuery("SELECT id, label FROM criteria", null)

        val criteriaMap = mutableMapOf<Int, Pair<String,Int>>()

        cursor.use{
            while(cursor.moveToNext()){
                val label = cursor.getString(cursor.getColumnIndex("label"))
                val id = cursor.getString(cursor.getColumnIndex("id"))
                criteriaMap[id.toInt()] = Pair(label,1)
            }
        }

        return criteriaMap
    }
}