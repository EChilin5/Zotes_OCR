package com.eachilin.zotes.DBhelper

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.eachilin.zotes.modal.CartModal
import java.lang.Exception

class CartHelper(context: Context):SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER) {

    companion object{
        private val DATABASE_VER = 1
        private val DATABASE_NAME ="PokeShopping.db"

        //
        private val TABLE_NAME="cart"
        private val ID ="id"
        private val NAME = "name"
        private val POKEID = "pokeID"
        private val ORDERPLACE = "orderPlace"
        private val PURCHASE ="purchaseDate"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TABLE_QUERY : String = ("CREATE TABLE " + TABLE_NAME +
                "(" + ID + " INTEGER PRIMARY KEY,  " + NAME + " TEXT, "  +
                POKEID + " TEXT,  " + ORDERPLACE + " TEXT, " + PURCHASE + " TEXT "+")" )
        db?.execSQL(CREATE_TABLE_QUERY)
    }




    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS  $TABLE_NAME")
        onCreate(db)
    }


    fun deleteStudentById(id: Int) : Int{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(ID, id)

        val success = db.delete(TABLE_NAME, "id=$id", null)
        db.close()
        return success

    }


    fun insertStudent(std: CartModal) :Long{
        val db = this.writableDatabase

        val contentValues = ContentValues()
        contentValues.put(ID, std.id)
        contentValues.put(NAME, std.name)
        contentValues.put(POKEID, std.pokeID)
        contentValues.put(ORDERPLACE, std.orderPlace)
        contentValues.put(PURCHASE, std.purchaseDate)

        val success = db.insert(TABLE_NAME, null, contentValues )
        db.close()
       return success

    }

    @SuppressLint("Range")
    fun getAllPokemon(): ArrayList<CartModal>{
        val pkList:ArrayList<CartModal> = ArrayList()
        val selectQuery = "Select * FROM $TABLE_NAME"
        val db = this.readableDatabase

        val cursor:Cursor?

        try{
            cursor = db.rawQuery(selectQuery, null)
        }catch(e: Exception){
            e.printStackTrace()
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var name:String
        var pokeId:String
        var order:String
        var purchaseDate :String

        if(cursor != null && cursor.count > 0){
            if(cursor.moveToFirst()){
                do{
                    id = cursor.getInt(cursor.getColumnIndex("id"))
                    name = cursor.getString(cursor.getColumnIndex("name"))
                    pokeId = cursor.getString(cursor.getColumnIndex("pokeID"))
                    order = cursor.getString(cursor.getColumnIndex("orderPlace"))
                    purchaseDate = cursor.getString(cursor.getColumnIndex("purchaseDate"))

                    val pk = CartModal(id=id, name=name, orderPlace = order, pokeID = pokeId, purchaseDate = purchaseDate)
                    pkList.add(pk)
                }while(cursor.moveToNext())
            }
        }
        return pkList

    }

}