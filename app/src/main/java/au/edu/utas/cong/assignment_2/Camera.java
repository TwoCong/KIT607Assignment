package au.edu.utas.cong.assignment_2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Camera extends AppCompatActivity {

    static  final int REQUEST_IMAGE_CAPTURE =1;
    static  final int REQUEST_SHARE_IMAGE = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);


        Button btnCamera = findViewById(R.id.btnCamera);

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestToTakeAPicture();
            }
        });

        Button btnShare = findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestToShareLastImage();
            }
        });

    }
    //step 4
    private void requestToTakeAPicture(){
        ActivityCompat.requestPermissions(Camera.this,
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
            case REQUEST_SHARE_IMAGE:{
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
                File photoFile = createImageFile();
                Uri photoURI = FileProvider.getUriForFile(this,
                        "au.edu.utas.cong.assignment_2",photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,photoURI);
                startActivityForResult(takePictureIntent,REQUEST_IMAGE_CAPTURE);
            }catch (IOException io){

            }

        }
    }
    String mCurrentPhotoPath;
    private File createImageFile() throws IOException{

        //create the image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".png", storageDir);
        mCurrentPhotoPath = image.getAbsolutePath();
        return  image;
    }
    //step 7
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            // to get the thumbnail
            ImageView myImageView = findViewById(R.id.myImageView);
            setPic(myImageView,mCurrentPhotoPath);
        }
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

    Bitmap lastImage;
    private  void  requestToShareLastImage(){
        ActivityCompat.requestPermissions(Camera.this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_SHARE_IMAGE);
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
}
