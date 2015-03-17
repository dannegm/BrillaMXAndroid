package mx.ambmultimedia.brillamexico;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.dd.processbutton.iml.ActionProcessButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Share extends ActionBarActivity {
    Context ctx;
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        ctx = this;
        config = new Config(ctx);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Uri pictureUri = getIntent().getData();
        final Bitmap pictureBitmap = cropImage(pictureUri,
                    0, 0, 512);

        ImageView preview = (ImageView) findViewById(R.id.imageSelfie);
        //preview.setImageURI(pictureUri);
        preview.setImageBitmap(pictureBitmap);

        final ActionProcessButton sendFoto = (ActionProcessButton) findViewById(R.id.sendPhoto);
        sendFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fbID = config.get("fbID", "0");

                EditText pieDeFoto = (EditText) findViewById(R.id.pieDeFoto);
                File photo = new File(pictureUri.getPath());

                RequestParams params = new RequestParams();
                params.put("x", "0");
                params.put("x", "0");
                params.put("width", pictureBitmap.getWidth());
                params.put("height", pictureBitmap.getHeight());

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

    public Bitmap cropImage (Uri image, int bleft, int btop, int size) {
        Bitmap bMap = BitmapFactory.decodeFile(image.getPath());

        int nwidth = size;
        int nheight = size;

        //if (bMap.getWidth() < nwidth || bMap.getHeight() < nheight) {
            if (bMap.getWidth() > bMap.getHeight()) {
                nwidth = bMap.getHeight();
                nheight = bMap.getHeight();
            }
            else if (bMap.getWidth() < bMap.getHeight()) {
                nwidth = bMap.getWidth();
                nheight = bMap.getWidth();
            }
            else {
                nwidth = bMap.getWidth();
                nheight = bMap.getHeight();
            }
       /* }
        else {
            nwidth = size;
            nheight = size;
        }*/

        Bitmap croppedBitmap = Bitmap.createBitmap(bMap, bleft, btop, nwidth, nheight);

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        //Bitmap rotatedBitmap = Bitmap.createBitmap(croppedBitmap , 0, 0, nwidth, nheight, matrix, true);
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
}
