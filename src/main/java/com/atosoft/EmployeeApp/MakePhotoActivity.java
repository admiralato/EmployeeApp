package com.atosoft.EmployeeApp;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Joe on 9/30/13.
 */
public class MakePhotoActivity extends Activity implements SurfaceHolder.Callback {

    public final static String DEBUG_TAG = "MakePhotoActivity";
    private Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Camera.PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    Camera.PictureCallback jpegCallback;

    private int cameraId = 0;
    private SurfaceView surface_view;
    private Camera mCamera;
    SurfaceHolder surface_holder        = null;
    SurfaceHolder.Callback sh_callback  = null;

    private String empCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            empCode = extras.getString("EMP_CODE");
        }

        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        surface_view = new SurfaceView(getApplicationContext());
        Button takePicture = new Button(getApplicationContext());
        takePicture.setText("Capture");
        takePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(shutterCallback, rawCallback, jpegCallback);
            }
        });

        addContentView(surface_view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT));
        addContentView(takePicture, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));


        if (surface_holder == null) {
            surface_holder = surface_view.getHolder();
        }

        sh_callback = this;
        surface_holder.addCallback(sh_callback);

        rawCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.d("Log", "onPictureTaken - raw");
            }
        };

        /** Handles data for jpeg picture */
        shutterCallback = new Camera.ShutterCallback() {
            public void onShutter() {
                Log.i("Log", "onShutter'd");
            }
        };
        jpegCallback = new Camera.PictureCallback() {
            public void onPictureTaken(byte[] data, Camera camera) {
                File pictureFileDir = Utilities.getDir();

                if (!pictureFileDir.exists() && !pictureFileDir.mkdirs()) {

                    Log.d(MakePhotoActivity.DEBUG_TAG, "Can't create directory to save image.");
                    Toast.makeText(getApplicationContext(), "Can't create directory to save image.",
                            Toast.LENGTH_LONG).show();
                    return;

                }

//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
//                String date = dateFormat.format(new Date());
                String photoFile = "Picture_" + empCode + ".jpg";

                String filename = pictureFileDir.getPath() + File.separator + photoFile;

                File pictureFile = new File(filename);

                try {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, null);
                    Matrix matrix = new Matrix();
                    matrix.postRotate(180); // Rotate image 180 degrees
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);

                    FileOutputStream fos = new FileOutputStream(pictureFile);
                    fos.write(byteArrayOutputStream.toByteArray());
                    fos.close();

                    Toast.makeText(getApplicationContext(), "New Image saved:" + photoFile,
                            Toast.LENGTH_LONG).show();
                    Thread.sleep(3000);

                    finish();

                } catch (Exception error) {
                    Log.d(MakePhotoActivity.DEBUG_TAG, "File" + filename + "not saved: "
                            + error.getMessage());
                    Toast.makeText(getApplicationContext(), "Image could not be saved.",
                            Toast.LENGTH_LONG).show();
                }
            }
        };
    }

    private int findBackFacingCamera() {
        int cameraId = -1;
        // Search for the front facing camera
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            Camera.CameraInfo info = new Camera.CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                Log.d(DEBUG_TAG, "Camera found");
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        cameraId = findBackFacingCamera();
        try {
            Log.d(DEBUG_TAG, "surfaceCreated(), holder"+holder.toString());
            mCamera = null;
            mCamera = Camera.open();
            Log.d(DEBUG_TAG, "surfaceCreated(), mCamera="+mCamera);
            mCamera.setDisplayOrientation(180);
            mCamera.setPreviewDisplay(holder);
            Camera.Parameters params= mCamera.getParameters();
            params.set("jpeg-quality", 72);
            params.set("rotation", 90);
            params.set("orientation", "portrait");
            params.setPictureFormat(PixelFormat.JPEG);
            mCamera.setParameters(params);

        } catch (Exception e) {
            Toast.makeText(this,
                    " surfaceCreated " + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

}
