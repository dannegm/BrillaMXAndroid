package mx.ambmultimedia.brillamexico;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;


public class Selfie extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        final String selfieID = bundle.getString("selfieID");

        final AsyncHttpClient client = new AsyncHttpClient();
        final String hostname = "http://api.brillamexico.org";
        client.get(hostname + "/user/selfie/" + selfieID, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject selfieObj = response;

                    String urlUser = hostname + "/user/" + selfieObj.getString("user_id");
                    String urlPicture = hostname + "/pictures/" + selfieObj.getString("picture");

                    String avatarUrl = getString(R.string.fb_avatarmini_link);
                    avatarUrl = avatarUrl.replaceAll("__fbid__", selfieObj.getString("user_id"));
                    String[] allowedContentTypes = new String[] { "image/png", "image/jpeg", "image/gif" };

                    client.get(avatarUrl, new BinaryHttpResponseHandler(allowedContentTypes) {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                            Bitmap UserAvatar = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
                            CircleImageView ImgUserAvatar = (CircleImageView) findViewById(R.id.authorAvatar);
                            ImgUserAvatar.setImageBitmap(UserAvatar);
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) { }
                    });

                    client.get(urlPicture, new BinaryHttpResponseHandler(allowedContentTypes) {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                            Bitmap BitmapSelfie = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
                            ImageView ImgSelfie = (ImageView) findViewById(R.id.selfiePicture);
                            ImgSelfie.setImageBitmap(BitmapSelfie);
                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) { }
                    });

                    client.get(urlUser, null, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            try {
                                TextView LabelUserName = (TextView) findViewById(R.id.authorName);
                                TextView LabelPoints = (TextView) findViewById(R.id.authorPoints);

                                LabelUserName.setText(response.getString("name").toString());
                                LabelPoints.setText(response.getString("points").toString());
                            } catch (JSONException e) {}
                        }
                    });

                } catch (JSONException e) {}
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
