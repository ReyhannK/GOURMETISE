package com.example.gourmetisemobile.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.gourmetisemobile.GourmetiseHelper
import com.example.gourmetisemobile.dataclass.Note

class NoteDAO(context : Context) {
    lateinit var DataBase : SQLiteDatabase
    lateinit var Helper : GourmetiseHelper

    init {
        Helper = GourmetiseHelper(context);
        DataBase = Helper.writableDatabase;
    }

    fun addNote(note: Note) {
        val v = ContentValues()
        v.put("bakery_siret", note.bakerySiret)
        v.put("criteria_id", note.criteriaId)
        v.put("value", note.value)

        DataBase.insert("note", null, v)
    }
}