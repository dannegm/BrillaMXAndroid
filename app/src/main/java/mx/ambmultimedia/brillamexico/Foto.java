package mx.ambmultimedia.brillamexico;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Foto extends ActionBarActivity {
    private Context ctx;

    private Camera mCamera;
    private CameraPreview mPreview;

    int CampoDeAccion;
    int compromisoID;

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        private String TAG = "[Camera]";
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = getOutputMediaFile();
            if (pictureFile == null) {
                Log.d(TAG, "Error creating media file, check storage permissions");
                return;
            }

            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();

                Uri ofinalPicture = Uri.fromFile(pictureFile);
                Intent intent = new Intent(Foto.this, Share.class);
                intent.putExtra("CampoDeAccion", CampoDeAccion);
                intent.putExtra("compromisoID", compromisoID);
                intent.setData(ofinalPicture);
                startActivity(intent);
            } catch (FileNotFoundException e) {
                Log.d(TAG, "File not found: " + e.getMessage());
            } catch (IOException e) {
                Log.d(TAG, "Error accessing file: " + e.getMessage());
            }
        }
    };

    private boolean isLight = false;
    private Config config;

    private static final int SELECT_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foto);
        ctx = this;
        config = new Config(ctx);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        CampoDeAccion = bundle.getInt("CampoDeAccion");
        compromisoID = bundle.getInt("compromisoID");

        Drawable frameDraw = getResources().getDrawable(R.drawable.marco_1);

        if (CampoDeAccion == 1) {
            switch (compromisoID) {
                case 0: frameDraw = getResources().getDrawable(R.drawable.marco_1); break;
                case 1: frameDraw = getResources().getDrawable(R.drawable.marco_2); break;
                case 2: frameDraw = getResources().getDrawable(R.drawable.marco_3); break;
                case 3: frameDraw = getResources().getDrawable(R.drawable.marco_4); break;
                case 4: frameDraw = getResources().getDrawable(R.drawable.marco_5); break;
                case 5: frameDraw = getResources().getDrawable(R.drawable.marco_6); break;
                case 6: frameDraw = getResources().getDrawable(R.drawable.marco_7); break;
                case 7: frameDraw = getResources().getDrawable(R.drawable.marco_8); break;
                case 8: frameDraw = getResources().getDrawable(R.drawable.marco_9); break;
                case 9: frameDraw = getResources().getDrawable(R.drawable.marco_10); break;
                case 10: frameDraw = getResources().getDrawable(R.drawable.marco_11); break;
                case 11: frameDraw = getResources().getDrawable(R.drawable.marco_12); break;
                case 12: frameDraw = getResources().getDrawable(R.drawable.marco_13); break;
                case 13: frameDraw = getResources().getDrawable(R.drawable.marco_14); break;
            }
        }
        else if (CampoDeAccion == 2) {
            switch (compromisoID) {
                case 0: frameDraw = getResources().getDrawable(R.drawable.marco_15); break;
                case 1: frameDraw = getResources().getDrawable(R.drawable.marco_16); break;
                case 2: frameDraw = getResources().getDrawable(R.drawable.marco_17); break;
                case 3: frameDraw = getResources().getDrawable(R.drawable.marco_18); break;
            }
        }
        else if (CampoDeAccion == 3) {
            switch (compromisoID) {
                case 0: frameDraw = getResources().getDrawable(R.drawable.marco_19); break;
                case 1: frameDraw = getResources().getDrawable(R.drawable.marco_20); break;
                case 2: frameDraw = getResources().getDrawable(R.drawable.marco_21); break;
                case 3: frameDraw = getResources().getDrawable(R.drawable.marco_22); break;
                case 4: frameDraw = getResources().getDrawable(R.drawable.marco_23); break;
                case 5: frameDraw = getResources().getDrawable(R.drawable.marco_24); break;
                case 6: frameDraw = getResources().getDrawable(R.drawable.marco_25); break;
                case 7: frameDraw = getResources().getDrawable(R.drawable.marco_26); break;
                case 8: frameDraw = getResources().getDrawable(R.drawable.marco_27); break;
            }
        }

        ImageView imageFrame = (ImageView) findViewById(R.id.imageFrame);
        imageFrame.setImageDrawable(frameDraw);

        mCamera = getCameraInstance();
        mPreview = new CameraPreview(this, this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.imageCamera);
        ViewGroup.LayoutParams params = preview.getLayoutParams();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        params.width = dm.widthPixels;
        params.height = dm.heightPixels;

        preview.setLayoutParams(params);
        preview.addView(mPreview);

        FloatingActionButton captureButton = (FloatingActionButton) findViewById(R.id.takePicture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        mCamera.takePicture(null, null, mPicture);
                    }
                }
        );

        ImageView browseImages = (ImageView) findViewById(R.id.browseImages);
        browseImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_PICTURE);
            }
        });

        final ImageView turnOnFlash = (ImageView) findViewById(R.id.toggleFalsh);
        turnOnFlash.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        Camera.Parameters p = mCamera.getParameters();

                        if (!isLight) {
                            p.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                            mCamera.setParameters(p);
                            turnOnFlash.setImageResource(R.drawable.ic_flash);
                            isLight = true;
                        } else {
                            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                            mCamera.setParameters(p);
                            turnOnFlash.setImageResource(R.drawable.ic_flash_off);
                            isLight = false;
                        }
                    }
                }
        );
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && null != data) {
            //Uri ofinalPicture = data.getData();
            Uri ofinalPicture = getOutputMediaFileUri();
            Intent intent = new Intent(Foto.this, Share.class);
            intent.putExtra("CampoDeAccion", CampoDeAccion);
            intent.putExtra("compromisoID", compromisoID);
            intent.setData(ofinalPicture);
            startActivity(intent);
        }
    }

    public static Camera getCameraInstance () {
        Camera c = null;
        try { c = Camera.open(); }
        catch (Exception e){ }
        return c;
    }

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri (){ return Uri.fromFile(getOutputMediaFile()); }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile () {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "BrillaMX");

        if (!mediaStorageDir.exists()) {
            if (! mediaStorageDir.mkdirs()) {
                Log.d("[Camera]", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "BrillaMX_"+ timeStamp + ".jpg");

        return mediaFile;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_foto, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.flip_camera:
                // Aquí giramos la cámara
                mPreview.flipCamera();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPreview.PausePreview();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPreview.PausePreview();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPreview.PausePreview();
    }
}
