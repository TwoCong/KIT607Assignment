package au.edu.utas.cong.assignment_2;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import au.edu.utas.cong.assignment_2.SQLite.JournalEntry;

public class JEntryAdapter extends ArrayAdapter<JournalEntry> {
    private int mLayoutResourceID;

    public JEntryAdapter(Context context, int resource, List<JournalEntry> objects) {
        super(context, resource,objects);
        this.mLayoutResourceID=resource;
    }
    @Override
    public View getView(int positon1, View convertView, ViewGroup parent){
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
        View row = layoutInflater.inflate(mLayoutResourceID,parent,false);
        JournalEntry jE = this.getItem(positon1);
        ImageView mood = row.findViewById(R.id.imgVMood);
        TextView date = row.findViewById(R.id.txtVDate);
        TextView time = row.findViewById(R.id.txtVTime);
        TextView title = row.findViewById(R.id.txtVTitle);
        ImageButton share = row.findViewById(R.id.imgBShare);
        TextView bodyText = row.findViewById(R.id.txtVBodyText);
        ImageView picture = row.findViewById(R.id.imgVPicture);
        date.setText(jE.getDate());


        return row;
    }
}