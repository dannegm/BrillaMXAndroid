package mx.ambmultimedia.brillamexico.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LikeView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.ambmultimedia.brillamexico.R;
import mx.ambmultimedia.brillamexico.utils.Config;


public class Selfie extends ActionBarActivity {
    Context ctx;
    Activity atx;
    Config config;

    private UiLifecycleHelper uiHelper;
    private LikeView like_view;

    public String refer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie);
        ctx = this;
        atx = this;
        config = new Config(ctx);
        refer = config.get("Refer", "none");
                config.set("Refer", "none");

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        final String selfieID = bundle.getString("selfieID");

        final AsyncHttpClient client = new AsyncHttpClient();
        final String hostname = getString(R.string.hostname);
        client.get(hostname + "/user/selfie/" + selfieID, null, new JsonHttpResponseHandler() {
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

                    // Share Action
                    final String link = hostname + "/selfie/" + selfieID;
                    FloatingActionButton share = (FloatingActionButton) findViewById(R.id.shareContent);
                    share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");

                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, link);

                            startActivity(Intent.createChooser(sharingIntent, "Compartir"));
                        }
                    });

                    /**
                     * Facebook like
                     */

                    uiHelper = new UiLifecycleHelper(atx, mStatusCallback);

                    like_view = (LikeView) findViewById(R.id.likeSelfie);
                    like_view.setObjectId("http://api.brillamexico.org/selfie/" + selfieID);
                    like_view.setLikeViewStyle(LikeView.Style.STANDARD);
                    like_view.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
                    like_view.setHorizontalAlignment(LikeView.HorizontalAlignment.LEFT);

                } catch (JSONException e) {}
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                String msg = "[" + statusCode + "|u/selfie] " + e.getMessage();
                Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
            }
        });

        Button toCamara = (Button) findViewById(R.id.toCamara);
        toCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Selfie.this, Compromisos.class);
                startActivity(intent);
            }
        });

        if (refer != "ShareActivity") {
            toCamara.setVisibility(View.INVISIBLE);
        }
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

    Session.StatusCallback mStatusCallback = new Session.StatusCallback (){
        @Override
        public void call(Session session, SessionState state, Exception exception) {

        }
    };

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, null);
    }

    @Override
    public void onBackPressed() {
        if (refer == "ShareActivity") {
            Intent intent = new Intent(Selfie.this, UserProfile.class);
            startActivity(intent);
        } else {
            this.finish();
        }
    }
}
