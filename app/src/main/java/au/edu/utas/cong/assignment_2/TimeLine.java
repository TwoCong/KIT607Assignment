package au.edu.utas.cong.assignment_2;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import au.edu.utas.cong.assignment_2.MainActivity;
import au.edu.utas.cong.assignment_2.R;
import au.edu.utas.cong.assignment_2.SQLite.JournalEntry;
import au.edu.utas.cong.assignment_2.SQLite.JournalEntryManager;

public class TimeLine extends AppCompatActivity {


    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        ListView entryList = findViewById(R.id.listViewEntry);

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

        JournalEntryManager mgr = new JournalEntryManager(TimeLine.this);
        ArrayList<JournalEntry> jeList = mgr.query();

        //定义一个HashMap构成的列表以键值对的方式存放数据
        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String,Object>>();
//循环填充数据
        for(int i=0;i<jeList.size();i++)        {
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("title", jeList.get(i).getTitle());
            map.put("bodyText", jeList.get(i).getBodyText());
            map.put("date", jeList.get(i).getDate());
            map.put("mood", jeList.get(i).getMood());
            map.put("location",jeList.get(i).getLocation());
            map.put("image",jeList.get(i).getImage());
            listItem.add(map);
        }

//构造SimpleAdapter对象，设置适配器
        SimpleAdapter mSimpleAdapter = new SimpleAdapter(this,
                listItem,//需要绑定的数据
                R.layout.entry_list_item,//每一行的布局
                new String[] {"date","date"},
//数组中的数据源的键对应到定义布局的View中
                new int[] {R.id.txtVTitle,R.id.txtVBodyText});

//为ListView绑定适配器
        entryList.setAdapter(mSimpleAdapter);



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
