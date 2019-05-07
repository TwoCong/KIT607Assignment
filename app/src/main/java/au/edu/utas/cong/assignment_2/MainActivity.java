package au.edu.utas.cong.assignment_2;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import au.edu.utas.cong.assignment_2.SQLite.JournalEntry;
import au.edu.utas.cong.assignment_2.SQLite.JournalEntryManager;


public class MainActivity extends AppCompatActivity {

//    声明一个类AFactory，里面有静态变量public static Activity A；在A中调用
//
//    AFactory.A = this;
//
//这样在b中就可以直接调用AFactory.A.function();就行了
//
//    http://www.oschina.net/question/912920_83748

    public static final int REQUEST_IMAGE_CAPETURE = 1;
    public static final int REQUEST_SHARE_IMAGE = 2;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private final  int IMAGE_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //If this is no image selected, hide imageView
        ImageView myImageView = findViewById(R.id.myImageView);
        myImageView.setVisibility(View.INVISIBLE);

        Button locationPageButton = findViewById(R.id.btnLogtimeline);
        locationPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TimeLine.class);
                startActivity(i);
            }


        });
        //Get Time data
        SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        final String currentDate =sd.format(new Date(System.currentTimeMillis()));
        TextView date = findViewById(R.id.txtVDate);
        date.setText(currentDate);
        Log.e("Date",currentDate);
        final EditText eTxtTitle = findViewById(R.id.eTxtTitle);
        final EditText eTxtBodytxt = findViewById(R.id.eTxtBodyText);
        final TextView path = findViewById(R.id.txtVPath);

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
        /**
         * get Location info starts
         */
        TextView locationInfo =findViewById(R.id.txtLocation);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        List<String> providerList = lm.getProviders(true);
        Log.e("TAG",providerList.size()+"");
        for (int i = 0; i< providerList.size();i++){
            Log.e("TAG: ",providerList.get(i));
        }

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        String provider = lm.getBestProvider(criteria,true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            locationInfo.setText("No permission");
            return;
        }
        final Location location = lm.getLastKnownLocation(provider);
        updateWithNewLocation(location);
        lm.requestLocationUpdates(provider, 2000, 10, locationListener);

        /**
         * Database Operation
         * 1.When pressing the ok button, first check whether JournalEntry table exists or not
         * 2. if not exists, create a new table, then add Entry object into db
         * 3. if exists, just add into db
         */
       // SQLiteDatabase db = new JournalEntryHelper(MainActivity.this).getWritableDatabase();
        Button btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Test the entry data
                Log.e("Title test ",eTxtTitle.getText().toString());
                Log.e("bodytext test", eTxtBodytxt.getText().toString());
                Log.e("Time test ",currentDate);
                Log.e("location test ",updateWithNewLocation(location));
                Log.e("Path test", path.getText().toString());
//                JournalEntryManager mgr = new JournalEntryManager(MainActivity.this);
//                JournalEntry jEntry = new JournalEntry();
//                jEntry.setTitle("title");
//                jEntry.setBodyText("BodyText");
//                jEntry.setMood(1);
//                jEntry.setDate(currentDate);
//                jEntry.setLocation("20 Sandy Bay");
//                jEntry.setImage("/image/*");
//                mgr.addEntry(jEntry);
               // showToast(mgr);

//                ContentValues cv = new ContentValues();
//                cv.put("date",currentDate);
//                cv.put("title","title");
//                cv.put("bodyText", "bodyText");
//                cv.put("mood",1);
//                cv.put("location","20 Sandy Bay");
//                cv.put("image","/image/*");

            }
        });
    }
    public  void showToast(JournalEntryManager jeM){
         Toast.makeText(this,jeM.query().get(0).image+" ",Toast.LENGTH_LONG).show();

    }
    private  final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            updateWithNewLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {
            updateWithNewLocation(null);
        }
    };
    private String updateWithNewLocation(android.location.Location location) {
        TextView locationInfo = findViewById(R.id.txtLocation);

        if (location != null) {
            double lat = location.getLatitude();
            double lng = location.getLongitude();
            Geocoder geocoder = new Geocoder(this);
            List places = null;
            try {
                places = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 5);
                //  test
                // Toast.makeText(this,places.size()+"", Toast.LENGTH_LONG).show();
                //Log.e("Places size", places.size() + "");
            } catch (IOException e) {
                e.printStackTrace();
            }
            String placename = "";
            if (places != null && places.size() > 0) {
                placename = ((Address) places.get(0)).getAddressLine(0);
            }
            locationInfo.setText(placename);
            //Log.e("current location info: ", ((Address) places.get(0)).getAddressLine(0));
            return placename;
        } else {
            locationInfo.setText("Cant get location info");
            return "";
        }
    }
    /**
     * GET locationINFO function ends
     */




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
                //myImageView.setVisibility(View.INVISIBLE);
                myImageView.setImageBitmap(Bitmap.createScaledBitmap(bm,myImageView.getWidth(),myImageView.getHeight(),false));
                myImageView.setVisibility(View.VISIBLE);
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

}
