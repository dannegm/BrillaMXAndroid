package mx.ambmultimedia.brillamexico;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;


public class Selfie extends ActionBarActivity {
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie);
        ctx = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        final String selfieID = bundle.getString("selfieID");

        final AsyncHttpClient client = new AsyncHttpClient();
        final String hostname = "http://danielgarcia.biz";
        client.get(hostname + "/selfie/" + selfieID, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject selfieObj = response;
                    final String authorID = selfieObj.getString("user_id");

                    TextView LabelPoints = (TextView) findViewById(R.id.authorPoints);
                    LabelPoints.setText(selfieObj.getString("description").toString());

                    JSONObject userObj = selfieObj.getJSONObject("user");
                    TextView LabelUserName = (TextView) findViewById(R.id.authorName);
                    LabelUserName.setText(userObj.getString("name").toString());

                    String avatarUrl = getString(R.string.fb_avatarmini_link);
                    avatarUrl = avatarUrl.replaceAll("__fbid__", authorID);
                    CircleImageView ImgUserAvatar = (CircleImageView) findViewById(R.id.authorAvatar);
                    Picasso.with(ctx)
                            .load(avatarUrl)
                            .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                            .into(ImgUserAvatar);

                    String urlPicture = hostname + "/pictures/" + selfieObj.getString("picture");
                    ImageView ImgSelfie = (ImageView) findViewById(R.id.selfiePicture);
                    Picasso.with(ctx)
                            .load(urlPicture)
                            .placeholder(R.drawable.foto_placeholder)
                            .error(R.drawable.foto_error)
                            .into(ImgSelfie);

                    LinearLayout toProfile = (LinearLayout) findViewById(R.id.toProfile);
                    toProfile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Selfie.this, UserViewer.class);
                            intent.putExtra("userID", authorID);
                            startActivity(intent);
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
