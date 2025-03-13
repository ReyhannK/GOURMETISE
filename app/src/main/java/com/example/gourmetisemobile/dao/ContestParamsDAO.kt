package com.example.gourmetisemobile.dao

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.gourmetisemobile.GourmetiseHelper
import com.example.gourmetisemobile.dataclass.Bakery
import com.example.gourmetisemobile.dataclass.ContestParams
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

class ContestParamsDAO (context : Context) {
    lateinit var DataBase : SQLiteDatabase
    lateinit var Helper : GourmetiseHelper

    init {
        Helper = GourmetiseHelper(context);
        DataBase = Helper.writableDatabase;
    }

    fun addContestParams(contestParams: ContestParams) {
        val v = ContentValues()
        v.put("startRegistration", contestParams.startRegistration)
        v.put("endRegistration", contestParams.endRegistration)
        v.put("startEvaluation", contestParams.startEvaluation)
        v.put("endEvaluation", contestParams.endEvaluation)
        DataBase.insert("contestParams", null, v)
    }

    @SuppressLint("Range")
    fun getContestParams(): ContestParams {
        val contestParams = ContestParams()

        val curseur = DataBase.rawQuery("SELECT * FROM contestParams WHERE id = (SELECT MAX(id) FROM contestParams)",arrayOf())
        curseur.moveToFirst()
        while (!curseur.isAfterLast()) {
            val startRegistration = curseur.getString(curseur.getColumnIndex("startRegistration"))
            val endRegistration = curseur.getString(curseur.getColumnIndex("endRegistration"))
            val startEvaluation = curseur.getString(curseur.getColumnIndex("startEvaluation"))
            val endEvaluation = curseur.getString(curseur.getColumnIndex("endEvaluation"))

            contestParams.startRegistration = startRegistration
            contestParams.endRegistration = endRegistration
            contestParams.startEvaluation = startEvaluation
            contestParams.endEvaluation = endEvaluation

            curseur.moveToNext()
        }
        curseur.close()
        return contestParams
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isOutsidePeriod(): Boolean{
        val endEvaluationString = this.getContestParams().endEvaluation
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX")
        val endEvaluationDate = ZonedDateTime.parse(endEvaluationString, formatter)

        val currentDateTime = ZonedDateTime.now()

        return currentDateTime.isAfter(endEvaluationDate)
    }
}