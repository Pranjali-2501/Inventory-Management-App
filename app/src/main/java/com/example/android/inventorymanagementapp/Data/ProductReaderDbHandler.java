package com.example.android.inventorymanagementapp.Data;

import android.content.ContentUris;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.inventorymanagementapp.Data.Product_Contract.ProductEntry;
public class ProductReaderDbHandler extends SQLiteOpenHelper {
    public static final String LOG_TAG = ProductReaderDbHandler.class.getName();
    public static final String DATABASE_NAME = "Inventory.db";
    public static final int DATABASE_VERSION = 1;
    public ProductReaderDbHandler(Context context)
    {
        super(context , DATABASE_NAME,null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(LOG_TAG,"in database first");
        String SQL_CREATE_ENTERIES = "CREATE TABLE " + ProductEntry.TABLE_NAME + " (" +
                ProductEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                ProductEntry.COLUMN_NAME + " TEXT NOT NULL," +
                ProductEntry.COLUMN_COMPANY + " TEXT," + ProductEntry.COLUMN_SNAME +
                " TEXT," + ProductEntry.COLUMN_SMAIL + " TEXT," + ProductEntry.COLUMN_SNUMBER +
                " INTEGER," + ProductEntry.COLUMN_CODE + " TEXT ," + ProductEntry.COLUMN_PRICE
                + " INTEGER NOT NULL," + ProductEntry.COLUMN_AMOUNT + " INTEGER NOT NULL DEFAULT 0);";
        Log.e(LOG_TAG,"in database second");
        db.execSQL(SQL_CREATE_ENTERIES);
        Log.e(LOG_TAG,"in database third");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
