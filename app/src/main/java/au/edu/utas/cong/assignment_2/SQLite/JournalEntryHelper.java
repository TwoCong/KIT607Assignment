package au.edu.utas.cong.assignment_2.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JournalEntryHelper extends SQLiteOpenHelper {
    private static  final int VERSION =1;
    private static final String DATABASE_NAME = "journalEntryBase.db";
    public JournalEntryHelper(Context context) {
        super(context, DATABASE_NAME,null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table  JournalEntry (" +
                "_id integer primary key autoincrement, " +
                "title TEXT , " +
                "bodyText TEXT, " +
                "mood integer, " +
                "date TEXT, " +
                "location TEXT, " +
                "image blob)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
