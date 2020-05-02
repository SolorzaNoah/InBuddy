package com.twu.login;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**

 Assignment Notes: Add code here to make the call either the camera or
 the image library to update the imageView on the screen.  Note that there
 are NUMEROUS different ways to do this, some better than others, and many
 don't work well together at all.

 I used the following tutorials:
 https://androidkennel.org/android-camera-access-tutorial/
 https://medium.com/@hasangi/capture-image-or-choose-from-gallery-photos-implementation-for-android-a5ca59bc6883
 */

public class CameraActivity extends AppCompatActivity {

    static final int TAKE_PHOTO_PERMISSION = 1;
    static final int REQUEST_TAKE_PHOTO = 2;
    static final int PICK_IMAGE_REQUEST = 3;

    ImageView imageView;
    Button takePictureButton;

    Uri file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        takePictureButton = (Button) findViewById(R.id.takePictureButton);
        imageView = (ImageView) findViewById(R.id.imageView);

        // We are giving you the code that checks for permissions
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            takePictureButton.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[] { android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE }, TAKE_PHOTO_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // This is called when permissions are either granted or not for the app
        // You do not need to edit this code.

        if (requestCode == TAKE_PHOTO_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                takePictureButton.setEnabled(true);
            }
        }
    }

    public void takePicture(View view) {
        // Add code here to start the process of taking a picture
        // Note you can send an intent to the camera to take a picture...
        // So start by considering that!

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, REQUEST_TAKE_PHOTO);
    }

    public void getImageFromLibrary(View view) {
        // Add code here to start the process of getting a picture from the library
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // Add code here to handle results from both taking a picture or pulling
        // from the image gallery.

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO) {
            // Add here.
            if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(photo);
            }
        } else if (requestCode == PICK_IMAGE_REQUEST) {
            //Add here.
            if (resultCode == RESULT_OK && data != null) {
                Uri selectedImage =  data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                if (selectedImage != null) {
                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String picturePath = cursor.getString(columnIndex);
                        imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                        cursor.close();
                    }
                }

            }

        }
    }

    // Add other methods as necessary here
}