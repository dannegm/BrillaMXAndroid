package mx.ambmultimedia.brillamexico;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class AlternativeCamera extends ActionBarActivity {
    private Context ctx;
    private Uri fileUri;

    int CampoDeAccion;
    int compromisoID;

    ImageView resultCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alternative_camera);
        ctx = this;

        Bundle bundle = getIntent().getExtras();
        CampoDeAccion = bundle.getInt("CampoDeAccion");
        compromisoID = bundle.getInt("compromisoID");

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri()); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        resultCamera = (ImageView) findViewById(R.id.resultCamera);
    }

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "BrillaMX");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("[Camera]", "failed to create directory");
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "BrillaMX_" + timeStamp + ".jpg");

        return mediaFile;
    }

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    private String selectedImagePath = "";
    private String imgPath;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {


                selectedImagePath = getImagePath();
                Bitmap ofinalPicture = decodeFile(selectedImagePath);

                Intent intent = new Intent(AlternativeCamera.this, Share.class);
                intent.putExtra("CampoDeAccion", CampoDeAccion);
                intent.putExtra("compromisoID", compromisoID);
                intent.putExtra("compromisoID", ofinalPicture);
                startActivity(intent);
            } else if (resultCode == RESULT_CANCELED) {
                Intent intent = new Intent(AlternativeCamera.this, Compromisos.class);
                startActivity(intent);
            } else {
                Toast.makeText(ctx, "algo ha salido mal", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AlternativeCamera.this, Compromisos.class);
                startActivity(intent);
            }
        }
    }


    public Uri setImageUri() {
        File file = new File(Environment.getExternalStorageDirectory(), "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }

    public String getImagePath() {
        return imgPath;
    }

    public Bitmap decodeFile(String path) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 70;

            // Find the correct scale value. It should be the power of
            // 2.
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE
                    && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
