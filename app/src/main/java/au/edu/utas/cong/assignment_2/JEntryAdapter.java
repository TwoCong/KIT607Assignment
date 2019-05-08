package au.edu.utas.cong.assignment_2;

import android.app.Service;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
        TextView title = row.findViewById(R.id.eTxtTitle);
        ImageView share = row.findViewById(R.id.imgVShare);
        TextView bodyText = row.findViewById(R.id.txtVBodyText);
        ImageView img = row.findViewById(R.id.imgVPicture);
        TextView location = row.findViewById(R.id.txtVLocation);
        //Format 2019-10-10 20:00 to Time: 20:00, Date 2019 10 10

        title.setText(jE.getTitle());
        bodyText.setText(jE.getBodyText()+" + "+ jE.getLocation()+" + "+jE.getImage());
        location.setText(jE.getLocation());
        // Format time
        String[] a = jE.getDate().split(" ");
        date.setText(a[0]);
        time.setText(a[1]);
        // Mood
        ArrayList imgList = new ArrayList();
        imgList.add(R.drawable.extremesad);
        imgList.add(R.drawable.verysad);
        imgList.add(R.drawable.sad);
        imgList.add(R.drawable.chill);
        imgList.add(R.drawable.happy);
        imgList.add(R.drawable.veryhappy);
        imgList.add(R.drawable.extremehappy);
        mood.setImageResource((Integer) imgList.get(positon1));


        share.setImageResource(R.drawable.share);
        // Image

        String path = "/sdcard/Image/" + jE.getImage();
        img.setImageURI(Uri.fromFile(new File(path)));

        return row;
    }
}
