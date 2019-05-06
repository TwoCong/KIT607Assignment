package au.edu.utas.cong.assignment_2.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class JournalEntryManager {
    private JournalEntryHelper jEntryHelper;
    private SQLiteDatabase db;
    public JournalEntryManager(Context context){
        jEntryHelper = new JournalEntryHelper(context);
        db = jEntryHelper.getWritableDatabase();
    }

    /**
     * Add a new entry to database
     * @param jEntry
     */
    public void addEntry(JournalEntry jEntry){
        db.beginTransaction();
        try {

                db.execSQL("INSERT into JournalEntry values(null,?,?,?,?,?,?)",
                        new Object[]{
                                jEntry.title,
                                jEntry.bodyText,
                                jEntry.mood,
                                jEntry.date,
                                jEntry.location,
                                jEntry.image});

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void updateTitle (JournalEntry jEntry){
        ContentValues cv= new ContentValues();
        cv.put("title", jEntry.getTitle());
        db.update("JournalEntry", cv, "_id=?", new String[]{String.valueOf(jEntry.get_id())});
    }
    /**
     * query all entries, return list
     * For pratical use, add parameter,
     * @return
     */
    public ArrayList<JournalEntry> query(){
        ArrayList<JournalEntry> jEntryList = new ArrayList<JournalEntry>();
        Cursor c=queryTheCursor();
        while (c.moveToNext()){
            JournalEntry jEntry = new JournalEntry();
            jEntry._id=c.getInt(c.getColumnIndex("_id"));
            jEntry.title=c.getString(c.getColumnIndex("title"));
            jEntry.bodyText = c.getString(c.getColumnIndex("bodyText"));
            jEntry.mood = c.getInt(c.getColumnIndex("mood"));
            jEntry.location = c.getString(c.getColumnIndex("location"));
            jEntry.image = c.getString(c.getColumnIndex("image"));
            jEntryList.add(jEntry);
        }
        c.close();
        return jEntryList;

    }

    /**
     * query all entries
     * @return cursor
     */
    public Cursor queryTheCursor(){
        Cursor c = db.rawQuery("Select * from JournalEntry",null);
        return c;
    }

    /**
     * close Database
     */
    public  void closeDB(){
        db.close();
    }
}
