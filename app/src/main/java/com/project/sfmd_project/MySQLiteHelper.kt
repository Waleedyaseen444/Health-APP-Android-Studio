package com.project.sfmd_project

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MySQLiteHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "OfflineData.db"
        private const val TABLE_NAME = "YourTableName"
        private const val COLUMN_ID = "id"
        private const val COLUMN_DATA = "data"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE = ("CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_DATA TEXT)")
        db?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertData(data: String): Long {
        val values = ContentValues()
        values.put(COLUMN_DATA, data)
        val db = this.writableDatabase
        val id = db.insert(TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun getAllData(): ArrayList<String> {
        val dataList = ArrayList<String>()
        val query = "SELECT * FROM $TABLE_NAME"
        val db = this.readableDatabase
        val cursor: Cursor?

        try {
            cursor = db.rawQuery(query, null)
        } catch (e: Exception) {
            e.printStackTrace()
            db.execSQL(query)
            return ArrayList()
        }

        if (cursor != null) {
            val dataColumnIndex = cursor.getColumnIndex(COLUMN_DATA)
            while (cursor.moveToNext()) {
                val data = if (dataColumnIndex != -1) cursor.getString(dataColumnIndex) else ""
                dataList.add(data)
            }
            cursor.close()
        }
        db.close()
        return dataList
    }


    fun deleteAllData(): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_NAME, null, null)
        db.close()
        return success
    }

}
