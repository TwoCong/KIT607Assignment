package au.edu.utas.cong.assignment_2;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class JournalEntryTable {
    public static JournalEntry createFromCursor(Cursor c){
        if (c == null || c.isAfterLast()|| c.isBeforeFirst()){
            return  null;
        }
        else {
            JournalEntry je = new JournalEntry();
            je.set_id(c.getInt(c.getColumnIndex(KEY_ENTRY_ID)));
            je.setTitle(c.getString(c.getColumnIndex(KEY_TITLE)));
            je.setBodyText(c.getString(c.getColumnIndex(KEY_BODYSUIT)));
            je.setMood(c.getInt(c.getColumnIndex(KEY_MOOD)));
            je.setDate(c.getString(c.getColumnIndex(KEY_DATE)));
            je.setLocation(c.getString(c.getColumnIndex(KEY_LOCATION)));
            je.setImage(c.getString(c.getColumnIndex(KEY_IMAGE)));

            return je;
        }
    }
    public static final String TABLE_NAME="journalentry";
    public static final String KEY_ENTRY_ID="entry_id";
    public  static  final String KEY_TITLE = "title";
    public static final String KEY_BODYSUIT ="bodyText";
    public static final String KEY_MOOD="mood";
    public static final String KEY_DATE="date";
    public static final String KEY_LOCATION = "location";
    public static final String KEY_IMAGE = "image";

    public static final String CREATE_STATEMENT="Create table "
            +TABLE_NAME
            + " (" + KEY_ENTRY_ID+" integer primary key autoincrement, "
            +KEY_TITLE+" TEXT not null, "
            + KEY_BODYSUIT + " TEXT not null, "
            +KEY_MOOD+" int not null, "
            +KEY_DATE+ " TEXT, "
            +KEY_LOCATION+ " TEXT, "
            +KEY_IMAGE+ " string not null "
            +");";
    public static void insert(SQLiteDatabase db, JournalEntry j){
        ContentValues values=new ContentValues();
        values.put(KEY_TITLE,j.getTitle());
        values.put(KEY_BODYSUIT,j.getBodyText());
        values.put(KEY_DATE,j.getDate());
        values.put(KEY_MOOD,j.getMood());
        values.put(KEY_LOCATION,j.getLocation());
        values.put(KEY_IMAGE,j.getImage());

        db.insert(TABLE_NAME,null,values);
    }
    public static ArrayList<JournalEntry> selectAll(SQLiteDatabase db){
        ArrayList<JournalEntry> results = new ArrayList<JournalEntry>();
        Cursor c = db.query(TABLE_NAME,null,null,null,null,null,null);
        if (c!=null){
            c.moveToFirst();
            while (!c.isAfterLast()){
                JournalEntry j = createFromCursor(c);
                results.add(j);
                c.moveToNext();
            }
        }
        Log.e("Notice", "select All Successfully");
        return results;
    }

}
