package com.twu.login;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.UUID;


public class AudioActivity extends AppCompatActivity {

    static final int REQUEST_PERMISSION_CODE = 1000;
    private Button play, stop, record;
    private MediaRecorder myAudioRecorder;
    private String outputFile;
    private static Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_audio);
        record = (Button) findViewById(R.id.record);
        play = (Button) findViewById(R.id.play);
        stop = (Button) findViewById(R.id.stop);


        if (checkedPermissionFromDevice())
        {
            record.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    outputFile = mContext.getExternalFilesDir(null)
                            .getAbsolutePath() + "/"
                            + UUID.randomUUID().toString() + "_audio_record.3gp";
                    setupMediaRecorder();
                    try {
                        myAudioRecorder.prepare();
                        myAudioRecorder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    play.setEnabled(false);


                    Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
                }
            });

            stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myAudioRecorder.stop();
                    stop.setEnabled(false);
                    play.setEnabled(true);
                    record.setEnabled(true);
                }
            });

            play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    stop.setEnabled(true);
                    record.setEnabled(false);
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    try {
                        mediaPlayer.setDataSource(outputFile);
                        mediaPlayer.prepare();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.start();
                    Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG).show();
                }
            });

        }
        else
        {
            requestPermission();
        }
    }

    private void setupMediaRecorder() {
        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);
    }

    private void requestPermission() {
      ActivityCompat.requestPermissions(this,new String[]{
              Manifest.permission.WRITE_EXTERNAL_STORAGE,
              Manifest.permission.RECORD_AUDIO

      },REQUEST_PERMISSION_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }

    private boolean checkedPermissionFromDevice() {
        int write_external_storage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO);
        return write_external_storage == PackageManager.PERMISSION_GRANTED;
    }
}
