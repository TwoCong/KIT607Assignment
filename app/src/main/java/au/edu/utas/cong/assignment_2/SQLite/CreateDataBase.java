package au.edu.utas.cong.assignment_2.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class CreateDataBase extends SQLiteOpenHelper {

    private  Context mContext;


    public static final String CREATE_JOURNALENTRY = "create table JOURNALENTRY("+
            "id integer primary key autoincrement, "+
            "Title text,"+
            "BodyText text, "+
            "Mood integer, "+
            "Date text, "+
            "Location text, "+
            //if multiple images have been added
            // "ImageNo ingeter, "+
            "Image blob)";


    public CreateDataBase( Context context,  String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_JOURNALENTRY);
        Toast.makeText(mContext,"Create DB successfully", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
