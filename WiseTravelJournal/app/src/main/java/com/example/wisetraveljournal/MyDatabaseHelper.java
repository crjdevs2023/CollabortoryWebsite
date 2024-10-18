package com.example.wisetraveljournal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private final Context context;
    private static final String DATABASE_NAME = "Journal_db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_Journals";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "journal_title";
    private static final String COLUMN_DATE = "journal_date";
    private static final String COLUMN_DESC = "journal_description";

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                COLUMN_DESC + " TEXT);";
        db.execSQL(query);
        Log.d("DB", "Table created with query: " + query);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME );
        onCreate(db);
    }
    void addJournal(String journal_title, String journal_date, String journal_description){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, journal_title);
        cv.put(COLUMN_DATE, journal_date);
        cv.put(COLUMN_DESC, journal_description);

        long result = db.insert(TABLE_NAME, null, cv);

        if(result == -1){
            Toast.makeText(context, "Failed to add Journal", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(context, "Journal Added Successfully", Toast.LENGTH_SHORT).show();
        }
    }
    Cursor readAllData(){
        String query = " SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }
}
