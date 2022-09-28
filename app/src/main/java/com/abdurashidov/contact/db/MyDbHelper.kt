package com.abdurashidov.contact.db

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build.ID
import com.abdurashidov.contact.db.MyConst.DB_NAME
import com.abdurashidov.contact.db.MyConst.ID
import com.abdurashidov.contact.db.MyConst.NAME
import com.abdurashidov.contact.db.MyConst.NUMBER
import com.abdurashidov.contact.db.MyConst.TABLE_NAME
import com.abdurashidov.contact.models.MyContact

class MyDbHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, 1), MyDbInterface {
    override fun onCreate(db: SQLiteDatabase?) {
        val query =
            "create table $TABLE_NAME (${MyConst.ID} integer not null primary key autoincrement unique, $NAME text not null, $NUMBER text not null)"
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }

    override fun addContact(myContact: MyContact) {
        val database = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(NAME, myContact.name)
        contentValues.put(NUMBER, myContact.number)
        database.insert("$TABLE_NAME", null, contentValues)
        database.close()
    }

    @SuppressLint("Recycle")
    override fun getAllContact(): ArrayList<MyContact> {
        val list = ArrayList<MyContact>()
        val database = readableDatabase
        val query = "select * from $TABLE_NAME"
        val cursor = database.rawQuery(query, null)
        if (cursor.moveToFirst()) {
            do {
                val myContact = MyContact(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2)
                )
                list.add(myContact)
            } while (cursor.moveToNext())
        }
        return list
    }

    override fun deleteContact(myContact: MyContact) {
        val database = this.writableDatabase
        database.delete(TABLE_NAME, "${MyConst.ID} = ?", arrayOf(myContact.id.toString()))
        database.close()
    }

    override fun editContact(myContact: MyContact) {
        val database = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(MyConst.ID, myContact.id)
        contentValues.put(NAME, myContact.name)
        contentValues.put(NUMBER, myContact.number)
        database.update(TABLE_NAME, contentValues, "${MyConst.ID} = ?", arrayOf(myContact.id.toString()))
    }
}