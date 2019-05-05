package au.edu.utas.cong.assignment_2;


import android.Manifest;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static au.edu.utas.cong.assignment_2.Camera.REQUEST_IMAGE_CAPTURE;


public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_IMAGE_CAPETURE = 1;
    public static final int REQUEST_SHARE_IMAGE = 2;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private final  int IMAGE_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button locationPageButton = findViewById(R.id.btnLogtimeline);
        locationPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TimeLine.class);
                startActivity(i);
            }


        });

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(calendar.getTime());
        ///  String time = "Current Time;" + format.format(calendar.getTime());


        TextView textView = findViewById(R.id.txtdate);
        //  TextView textshow = (TextView) findViewById(id.time);

        TextView textViewDate = findViewById(R.id.txtdate);
        textViewDate.setText(currentDate);

        ImageButton cameraTurn = findViewById(R.id.imgBCameraTurn);
        cameraTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Camera.class);
                startActivity(i);
                //requestToTakeAPicture();
            }
        });
        ImageView addImage = findViewById(R.id.imgBAdd);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSystemImage();
            }
        });

       // ImageButton btnShare = findViewById(R.id.imgBShare);


        ImageView myImageView = findViewById(R.id.myImageView);
    }

    /**
     * Camera function
     * Open camera function, share image   /  then share entry with text info and image together.
     */

    /**
     * Get system images
     */
    private void getSystemImage(){
        Intent localIntent = new Intent();
        localIntent.setType(IMAGE_UNSPECIFIED);
        localIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(localIntent,IMAGE_CODE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //Log.e(TAG, "onActivityResultForOpenGallery: Finish taking photo",resultCode );
        Bitmap bm = null;
        ContentResolver resolver = getContentResolver();

        if (requestCode == IMAGE_CODE){
            try {
                Uri originaUri = data.getData();
                bm = MediaStore.Images.Media.getBitmap(resolver,originaUri);
                ImageView myImageView = findViewById(R.id.myImageView);

                myImageView.setImageBitmap(Bitmap.createScaledBitmap(bm,myImageView.getWidth(),myImageView.getHeight(),false));



                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(originaUri, proj,null,null,null);

                int column_index= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(column_index);
                TextView tv= findViewById(R.id.txtVPath);
                tv.setText(path);



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
               Log.e("Tag-->Error", e.toString());
            }
            finally {
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    String mCurrentPhotoPath;
    private  void setPic(ImageView myImageView, String path){





    }

}
