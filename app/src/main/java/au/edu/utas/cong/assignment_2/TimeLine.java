package au.edu.utas.cong.assignment_2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import au.edu.utas.cong.assignment_2.MainActivity;
import au.edu.utas.cong.assignment_2.R;

public class TimeLine extends AppCompatActivity {

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

    }

}
