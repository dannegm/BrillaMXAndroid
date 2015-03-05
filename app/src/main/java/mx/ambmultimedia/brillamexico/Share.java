package mx.ambmultimedia.brillamexico;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

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

        ImageView preview = (ImageView) findViewById(R.id.imageSelfie);
        preview.setImageURI(pictureUri);

        Button sendFoto = (Button) findViewById(R.id.sendPhoto);
        sendFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fbID = config.get("fbID", "0");
                AsyncHttpClient client = new AsyncHttpClient();

                EditText pieDeFoto = (EditText) findViewById(R.id.pieDeFoto);
                File photo = new File(pictureUri.getPath());

                RequestParams params = new RequestParams();
                params.put("x", "0");
                params.put("x", "0");
                params.put("width", "0");
                params.put("height", "0");


                params.put("engagement_id", "1");
                params.put("description", pieDeFoto.getText().toString());
                try {
                    params.put("picture", photo);
                } catch(FileNotFoundException e) {}

                Toast.makeText(ctx, "Subiendo selfie...", Toast.LENGTH_LONG).show();
                String hostname = "http://api.brillamexico.org";
                client.post(hostname + "/user/selfie/" + fbID, params, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        try {
                            String selfieID = response.getString("id");

                            Intent intent = new Intent(Share.this, Selfie.class);
                            intent.putExtra("selfieID", selfieID);
                            startActivity(intent);
                        } catch (JSONException e) {}
                    }
                });
            }
        });
    }

    @Override
     public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
