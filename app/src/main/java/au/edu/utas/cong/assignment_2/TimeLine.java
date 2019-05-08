package au.edu.utas.cong.assignment_2;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class TimeLine extends AppCompatActivity {

    private static final int REQUEST_MODIFY_ENTRY = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);


        ImageButton newentry = (ImageButton)findViewById(R.id.btnNewentry);
        newentry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TimeLine.this,MainActivity.class);
                startActivity(i);
            }
        });

        final Button Graph = (Button)findViewById(R.id.btnGraph_T);
        Graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TimeLine.this, Graph.class);
                startActivity(i);
            }
        });
        Database databaseConnection = new Database(this);
        final SQLiteDatabase db = databaseConnection.open();
        final ArrayList<JournalEntry> jeList = JournalEntryTable.selectAll(db);
        Log.e("有",jeList.size()+"个");
        for (int i = 0;i<jeList.size();i++){
            Log.d("Title",jeList.get(i).getTitle());
            Log.d("Image",jeList.get(i).getImage());
            Log.d("Mood",jeList.get(i).getMood()+"");
            Log.d("Location",jeList.get(i).getLocation());

            Log.d("----","---------");
        }

//        //定义一个HashMap构成的列表以键值对的方式存放数据
//        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
////循环填充数据
//        for(int i=0;i<jeList.size();i++)        {
//            HashMap<String,Object> map = new HashMap<String,Object>();
//            map.put("title", jeList.get(i).getTitle());
//            map.put("bodyText", jeList.get(i).getBodyText());
//            map.put("date", jeList.get(i).getDate());
//            map.put("mood", jeList.get(i).getMood());
//            map.put("location",jeList.get(i).getLocation());
//            map.put("image",jeList.get(i).getImage());
//            listItem.add(map);
//        }
//
////构造SimpleAdapter对象，设置适配器
//        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this,
//                listItem,//需要绑定的数据
//                R.layout.entry_list_item,//每一行的布局
//                new String[] {"bodyText","date","location"},
////数组中的数据源的键对应到定义布局的View中
//                new int[] {R.id.txtVBodyText,R.id.txtVDate,R.id.txtVLocation});
//
////为ListView绑定适配器
//        entryList.setAdapter(mSimpleAdapter);

        final ListView entryList = findViewById(R.id.listViewEntry);
        final JEntryAdapter jEntryAdapter = new JEntryAdapter(
                getApplicationContext(),R.layout.entry_list_item,jeList
        );
        entryList.setAdapter(jEntryAdapter);

        //Detecting item tap
        entryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(TimeLine.this);
//                builder.setMessage(jeList.get(i).get_id()).setTitle("Tapped");
//                AlertDialog dialog =builder.create();
//                dialog.show();
                Log.e("Tapped","Succe");
                final JournalEntry j = jeList.get(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(TimeLine.this);
                builder.setTitle("Update");
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        JournalEntryTable.delete(db,j.get_id());
                        ArrayList<JournalEntry> jeList = JournalEntryTable.selectAll(db);
                        JEntryAdapter jEntryAdapter = new JEntryAdapter(
                                getApplicationContext(),R.layout.entry_list_item,jeList
                        );
                        entryList.setAdapter(jEntryAdapter);
                    }
                });
                builder.setNegativeButton("Modify", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(TimeLine.this,MainActivity.class);
                        startActivityForResult(intent,REQUEST_MODIFY_ENTRY);
                    }
                });
                builder.create().show();
            }
        });

//        final ArrayList<JournalEntry> items = new ArrayList<JournalEntry>();
//
//        ArrayAdapter<JournalEntry> myListAdapter  = new ArrayAdapter<JournalEntry>(
//                getApplicationContext(),
//                R.layout.entry_list_item,
//                items
//        );
//        entryList.setAdapter(myListAdapter);
//
//
//        entryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                AlertDialog.Builder builder = new AlertDialog.Builder(TimeLine.this);
////                builder.setMessage(items.get(position)).setTitle("Item tapped");
////                AlertDialog dialog = builder.create();
////                dialog.show();
//            }
//        });



    }

}
