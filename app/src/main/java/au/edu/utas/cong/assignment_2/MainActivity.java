package au.edu.utas.cong.assignment_2;


import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import au.edu.utas.cong.assignment_2.SQLite.JournalEntryManager;

import static au.edu.utas.cong.assignment_2.Camera.REQUEST_IMAGE_CAPTURE;


public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_IMAGE_CAPETURE = 1;
    private static final int REQUEST_SHARE = 2;
    private static final int REQUEST_CHOOSE_IMAGE = 3;
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private static Uri photoURI = null;
    private static String mCurrentPhotoPath;
    //private final  int IMAGE_CODE = 0;
    private ImageView myImageView;
    private int moodLevel;

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

        ImageButton cameraTurn = findViewById(R.id.imgBCamera);
        cameraTurn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //requestToTakeAPicture();
                if (takePhotoIntent.resolveActivity(getPackageManager())!=null){
                    startActivityForResult(takePhotoIntent,REQUEST_IMAGE_CAPETURE);
                }
            }
        });

        ImageView addImage = findViewById(R.id.imgBAdd);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSystemImage();
            }
        });

        //Grap all the Mood imageView into  one list
        //moodLevel 0-6
        final ArrayList<ImageView> moodList = new ArrayList<ImageView>();
        final ArrayList imgList = new ArrayList();
        imgList.add(R.drawable.extremesad);
        imgList.add(R.drawable.verysad);
        imgList.add(R.drawable.sad);
        imgList.add(R.drawable.chill);
        imgList.add(R.drawable.happy);
        imgList.add(R.drawable.veryhappy);
        imgList.add(R.drawable.extremehappy);

        moodList.add((ImageView) findViewById(R.id.imgESad));   //moodLevel =0
        moodList.add((ImageView) findViewById(R.id.imgVSad)); //moodLevel =1
        moodList.add((ImageView) findViewById(R.id.imgSad)); //moodLevel =2
        moodList.add((ImageView) findViewById(R.id.imgChill));//moodLevel =3
        moodList.add((ImageView) findViewById(R.id.imgHappy));//moodLevel =4
        moodList.add((ImageView) findViewById(R.id.imgVHappy));//moodLevel =5
        moodList.add((ImageView) findViewById(R.id.imgEHappy));//moodLevel =6
        moodList.get(0).setTag(imgList.get(0));
        moodList.get(1).setTag(imgList.get(1));
        moodList.get(2).setTag(imgList.get(2));
        moodList.get(3).setTag(imgList.get(3));
        moodList.get(4).setTag(imgList.get(4));
        moodList.get(5).setTag(imgList.get(5));
        moodList.get(6).setTag(imgList.get(6));

        for (int i =0;i<moodList.size();i++){
            final ImageView mood=moodList.get(i);
            final int j=i;
            mood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //check any other mood is verified or not
                    //if any another has checked, check same level or not, same level, change image, not same, change other into mood, change this into verified.

                    int a;
                    boolean flag = false;
                    for (a = 0; a < moodList.size(); a++) {
                        if (moodList.get(a).getTag().equals( R.drawable.verified)) {
                            //已经选择过了
                            //a: 之前对号的
                            //j：现在需要变成对号的
                            Log.e("Selected", String.valueOf(a));
                            //当前点击 和之前verified是同一个的话
                            //把当前换成imglist里面的
                            if (a == j) {
                                mood.setImageResource((Integer) imgList.get(a));
                                mood.setTag(imgList.get(j));
                                Log.e("Checked", "2");
                                flag = true;

                            }else{
                                //不是一个的话，把moodlist a换成imglist a， 然后把moodlist j 换成verified
                                moodList.get(a).setImageResource((Integer) imgList.get(a));
                                moodList.get(a).setTag(imgList.get(a));
                                moodList.get(j).setImageResource(R.drawable.verified);
                                moodList.get(j).setTag(R.drawable.verified);
                                Log.e("Checked", "1");
                            }
                            break;
                        }
                    }
                    if (flag==false) {
                        mood.setImageResource(R.drawable.verified);
                        mood.setTag(R.drawable.verified);
                    }
                }

            });
        }






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
        startActivityForResult(localIntent,REQUEST_CHOOSE_IMAGE);
    }
    //step 4
    private void requestToTakeAPicture(){
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CAMERA},
                REQUEST_IMAGE_CAPTURE);

    }
    //step 5
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case REQUEST_IMAGE_CAPTURE:
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    takeAPicture();
                }else{

                }
                return;
            case REQUEST_SHARE:{
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    shareLastImage();
                }
                return;
            }
        }
    }

    //step 6
    private  void takeAPicture(){
        Intent takePictureIntent  = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager())!=null){
           try{
                File photoFile = createImageFile();  //Create file for saving photo
                photoURI = FileProvider.getUriForFile(this,
                       "au.edu.utas.cong.assignment_2",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);//将用于输出的文件Uri传递给相机
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);

            }
            catch (IOException io){
                io.printStackTrace();
            }
        }
    }
    Bitmap lastImage;
    private File createImageFile() throws IOException{

        //create the image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".png", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        TextView path = findViewById(R.id.txtVPath);
        path.setText(mCurrentPhotoPath);
        return  image;
    }
    private  void setPic(ImageView myImageView, String path){

        int targetW = myImageView.getWidth();
        int targetH = myImageView.getHeight();

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath,bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW/targetW,photoH/targetH);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, bmOptions);
        myImageView.setImageBitmap(bitmap);
        lastImage = bitmap;

    }
    private  void  requestToShareLastImage(){
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_SHARE);
    }

    void shareLastImage(){
        String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(),lastImage,"title", null);
        Uri bitmapUri = Uri.parse(bitmapPath);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM,bitmapUri);
        shareIntent.setType("image/jpeg");
        startActivity(Intent.createChooser(shareIntent,"Share Via..."));
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            Bitmap bm = null;
            ContentResolver resolver = getContentResolver();
        //Open camera
        if (requestCode == REQUEST_IMAGE_CAPETURE &&resultCode == RESULT_OK) {
            // to get the thumbnail

            Bundle extras = data.getExtras();
            bm = (Bitmap) extras.get("data");
            myImageView = findViewById(R.id.myImageView);
            //myImageView.setVisibility(View.INVISIBLE);
            myImageView.setImageBitmap(Bitmap.createScaledBitmap(bm, myImageView.getWidth(), myImageView.getHeight(), false));
            myImageView.setVisibility(View.VISIBLE);
            String path = savePhotoAtLoal(bm);
            TextView txtVPath = findViewById(R.id.txtVPath);
            txtVPath.setText(path);

        }
        //Share Function
        if (requestCode ==REQUEST_SHARE && resultCode ==RESULT_OK){

        }


        //choose picture from gallery
        if (requestCode == REQUEST_CHOOSE_IMAGE) {
            try {
                Uri originaUri = data.getData();
                bm = MediaStore.Images.Media.getBitmap(resolver, originaUri);
                myImageView = findViewById(R.id.myImageView);
                //myImageView.setVisibility(View.INVISIBLE);
                myImageView.setImageBitmap(Bitmap.createScaledBitmap(bm, myImageView.getWidth(), myImageView.getHeight(), false));
                myImageView.setVisibility(View.VISIBLE);
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(originaUri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                String path = cursor.getString(column_index);
                TextView tv = findViewById(R.id.txtVPath);
                tv.setText(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                Log.e("Tag-->Error", e.toString());
            } finally {
                return;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);


    }
    private String savePhotoAtLoal(Bitmap bm){
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.i("TestFile",
                    "SD card is not avaiable/writeable right now.");
            return "No SD card";
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String name = "JPEG_" + timeStamp + ".jpg";
        Toast.makeText(this, name, Toast.LENGTH_LONG).show();
        FileOutputStream b = null;
        File file = new File("/sdcard/Image/");
        file.mkdirs();// 创建文件夹
        String fileName = "/sdcard/Image/" + name;
        try {
            b = new FileOutputStream(fileName);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return name;

    }



}
