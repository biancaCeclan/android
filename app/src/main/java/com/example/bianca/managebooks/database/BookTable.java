package com.example.bianca.managebooks.database;

import android.database.sqlite.SQLiteDatabase;

public class BookTable {
    // Database table
    public static final String TABLE_BOOK = "book";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_AUTHOR = "author";
    public static final String COLUMN_PUBLICATION_YEAR = "publicationYear";
    public static final String COLUMN_PRICE = "price";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_BOOK
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_AUTHOR + " text not null, "
            + COLUMN_PUBLICATION_YEAR + " INT not null, "
            + COLUMN_PRICE + " INT not null"
            + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOK);
        onCreate(database);
    }
}
