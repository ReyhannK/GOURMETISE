package com.example.gourmetisemobile

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

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
}