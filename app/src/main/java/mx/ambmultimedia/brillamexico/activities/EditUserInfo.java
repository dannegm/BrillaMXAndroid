package mx.ambmultimedia.brillamexico.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;
import mx.ambmultimedia.brillamexico.R;
import mx.ambmultimedia.brillamexico.utils.Config;


public class EditUserInfo extends ActionBarActivity {
    private Context ctx;
    private Config config;
    private String fbID;

    EditText nName;
    EditText nBio;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "YRX5xRqc91Yxv8wuBOICIgH0h";
    private static final String TWITTER_SECRET = "SRYfHQtI0hXczFoRA0EGCO0rzEAxCC9bsdi2mnSdgtpnCCIwnj";
    private Boolean isTwitterLinked;
    private TwitterLoginButton loginButton;

    // HTTP Config
    private String hostname;
    private AsyncHttpClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        ctx = this;
        config = new Config(ctx);
        fbID = config.get("fbID", "0");

        hostname = getString(R.string.hostname);
        client = new AsyncHttpClient();

        // Inicializa la configuración de Twitter
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        isTwitterLinked = Boolean.valueOf(config.get("isTwitterLinked", "false"));
        TwitterIntegration();

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fbID = config.get("fbID", "0");
        CircleImageView ImgUserAvatar = (CircleImageView) findViewById(R.id.ImgUserAvatar);
        String avatarUrl = getString(R.string.fb_avatar_link);
        avatarUrl = avatarUrl.replaceAll("__fbid__", fbID);

        Picasso.with(ctx)
                .load(avatarUrl)
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .into(ImgUserAvatar);

        nName = (EditText) findViewById(R.id.editName);
        nBio = (EditText) findViewById(R.id.editBio);

        String _user = config.get("user", "null");
        try {
            JSONObject user = new JSONObject(_user);
            nName.setText(user.getString("name"));
            nBio.setText(user.getString("bio"));
        } catch (JSONException e) { }

        Button saveData = (Button) findViewById(R.id.saveData);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateData();
            }
        });
    }

    public void TwitterIntegration () {
        /**
         * Twitter integration
         */
        // Decimos al TwitterButton qué hacer cuando se da click
        loginButton = (TwitterLoginButton)
                findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            // Cuando se inicia sesión (supongo)
            public void success(Result<TwitterSession> result) {
                // Obtenemos la sesión y ...
                TwitterSession session = Twitter.getSessionManager().getActiveSession();

                // Obtenemos los datos
                String twitterID = String.valueOf(session.getUserId());
                UpdateTwitterID(twitterID);
            }

            @Override
            public void failure(TwitterException exception) {
                // Cuando algo sale mal
                Toast.makeText(ctx, "Algo ha salido mal con Twitter", Toast.LENGTH_SHORT);
            }
        });

        View completeText = findViewById(R.id.loginTwComplete);
        if (isTwitterLinked) {
            completeText.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.INVISIBLE);
        } else {
            completeText.setVisibility(View.INVISIBLE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pasamos resultados al activity? Aún no lo entiendo bien :(
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    public void UpdateTwitterID (String twitterID) {
        RequestParams params = new RequestParams();
        params.put("twid", twitterID);
        client.post(hostname + "/user/twitter/" + fbID, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (!isTwitterLinked) {
                    config.set("isTwitterLinked", "true");
                    Intent intent = new Intent(EditUserInfo.this, Logro.class);
                    intent.putExtra("Reference", "TwitterLinked");
                    intent.putExtra("LogroID", "6");
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(ctx, "Fail to connect", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void UpdateData () {
        config.set("isReload", "true");
        final Boolean isCompleteProfile = Boolean.valueOf(config.get("isCompleteProfile", "false"));

        RequestParams params = new RequestParams();
        params.put("name", nName.getText().toString());
        params.put("bio", nBio.getText().toString());
        client.post(hostname + "/user/edit/" + fbID, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (!isCompleteProfile) {
                    config.set("isCompleteProfile", "true");
                    Intent intent = new Intent(EditUserInfo.this, Logro.class);
                    intent.putExtra("Reference", "EditUser");
                    intent.putExtra("LogroID", "2");
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(EditUserInfo.this, UserProfile.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Toast.makeText(ctx, "Fail to connect", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(EditUserInfo.this, UserProfile.class);
        startActivity(intent);
    }
}
