package mx.ambmultimedia.brillamexico;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Share extends ActionBarActivity {
    private Context ctx;
    private Config config;
    private Uri fileUri;

    int CampoDeAccion;
    int compromisoID;

    private ImageView preview;
    private Bitmap previewFoto;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private String selectedImagePath = "";
    private String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ctx = this;
        config = new Config(ctx);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        CampoDeAccion = bundle.getInt("CampoDeAccion");
        compromisoID = bundle.getInt("compromisoID");

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        CreateFrame();
        preview = (ImageView) findViewById(R.id.imageSelfie);

        final ActionProcessButton sendFoto = (ActionProcessButton) findViewById(R.id.sendPhoto);
        sendFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fbID = config.get("fbID", "0");

                File photo = new File(selectedImagePath);
                EditText pieDeFoto = (EditText) findViewById(R.id.pieDeFoto);

                RequestParams params = new RequestParams();
                params.put("x", "0");
                params.put("x", "0");
                params.put("width", previewFoto.getWidth());
                params.put("height", previewFoto.getHeight());

                params.put("engagement_id", "1");
                params.put("description", pieDeFoto.getText().toString());
                try {
                    params.put("picture", photo);
                } catch(FileNotFoundException e) {}

                final String hostname = getString(R.string.hostname);
                final AsyncHttpClient client = new AsyncHttpClient();
                client.post(hostname + "/user/selfie/" + fbID, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            String selfieID = response.getString("id");

                            RequestParams points = new RequestParams();
                            points.put("points", "10");
                            client.post(hostname + "/user/points/" + fbID, points, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                    Toast.makeText(ctx, "Has ganado 10 puntos", Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String response, Throwable e) { }
                            });

                            Integer nselfies = Integer.valueOf(config.get("NSelfies", "0")) + 1;
                            config.set("NSelfies", nselfies.toString());

                            if (nselfies == 5) {
                                Intent intent = new Intent(Share.this, Logro.class);
                                intent.putExtra("Reference", "Selfie");
                                intent.putExtra("LogroID", "3");
                                config.set("Refer", "ShareActivity");
                                startActivity(intent);
                            } else if (nselfies == 10) {
                                Intent intent = new Intent(Share.this, Logro.class);
                                intent.putExtra("Reference", "Selfie");
                                intent.putExtra("LogroID", "4");
                                config.set("Refer", "ShareActivity");
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(Share.this, Selfie.class);
                                intent.putExtra("selfieID", selfieID);
                                config.set("Refer", "ShareActivity");
                                startActivity(intent);
                            }
                        } catch (JSONException e) {}
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable e) {
                        String msg = "[" + statusCode + "]" + e.getMessage();
                        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
                        Log.i("[Client]", msg);
                    }

                    @Override
                    public void onProgress(int bytesWritten, int totalSize) {
                        int progress = ((bytesWritten / totalSize) / 10) / 10;
                        sendFoto.setProgress(progress);
                    }

                });
            }
        });
    }
    private void CreateFrame () {
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

        ImageView imageFrame = (ImageView) findViewById(R.id.imageMarco);
        imageFrame.setImageDrawable(frameDraw);
    }

    public Bitmap cropImage (Bitmap _bMap) {
        Bitmap bMap = _bMap;
        int nwidth, nheight;

        if (bMap.getWidth() > bMap.getHeight()) {
            nwidth = bMap.getHeight();
            nheight = bMap.getHeight();
        } else if (bMap.getWidth() < bMap.getHeight()) {
            nwidth = bMap.getWidth();
            nheight = bMap.getWidth();
        } else {
            nwidth = bMap.getWidth();
            nheight = bMap.getHeight();
        }

        Bitmap croppedBitmap = Bitmap.createBitmap(bMap, 0, 0, nwidth, nheight);
        return croppedBitmap;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //

    private static Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                selectedImagePath = getImagePath();
                previewFoto = decodeFile(selectedImagePath);

                previewFoto = cropImage(previewFoto);
                preview.setImageBitmap(previewFoto);
            } else if (resultCode == RESULT_CANCELED) {
                Intent intent = new Intent(Share.this, Compromisos.class);
                startActivity(intent);
            } else {
                Toast.makeText(ctx, "algo ha salido mal", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Share.this, Compromisos.class);
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
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, o);
            // The new size we want to scale to
            final int REQUIRED_SIZE = 512;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE
                    && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;

            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeFile(path, o2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
