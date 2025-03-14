package com.example.gourmetisemobile.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
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
        v.put("criteria_id", note.criteria_id)
        v.put("value", note.value)

        DataBase.insert("note", null, v)
    }

    @SuppressLint("Range")
    fun getNotesForBakery(siret: String): List<Note> {
        val notesList = mutableListOf<Note>()

        val noteCursor: Cursor = DataBase.rawQuery(
            "SELECT * FROM note WHERE bakery_siret = ?", arrayOf(siret)
        )

        if (noteCursor.moveToFirst()) {
            do {
                val criteriaId = noteCursor.getInt(noteCursor.getColumnIndex("criteria_id"))
                val value = noteCursor.getInt(noteCursor.getColumnIndex("value"))

                notesList.add(Note(bakerySiret = siret,criteria_id = criteriaId, value = value))
            } while (noteCursor.moveToNext())
        }
        noteCursor.close()

        return notesList
    }
}