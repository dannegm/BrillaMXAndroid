package mx.ambmultimedia.brillamexico;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;


public class UserProfile extends ActionBarActivity {
    Context ctx;
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ctx = this;
        config = new Config(ctx);

        Boolean isLogin = Boolean.valueOf(config.get("isLogin", "false"));

        if (!isLogin) {
            Intent intent = new Intent(UserProfile.this, LoginStep1.class);
            startActivity(intent);
        } else {
            Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            NavDrawerFrag navDrawerFragment = (NavDrawerFrag) getSupportFragmentManager().findFragmentById(R.id.navDrawer);
            DrawerLayout drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navDrawerFragment.setUp(R.id.navDrawer, drawer_layout, toolbar);

            DrawableEvents();
            BuildProfile();
        }
    }

    public void BuildProfile () {
        String fbID = config.get("fbID", "0");
        AsyncHttpClient client = new AsyncHttpClient();

        String avatarUrl = getString(R.string.fb_avatar_link);
        avatarUrl = avatarUrl.replaceAll("__fbid__", fbID);
        String[] allowedContentTypes = new String[] { "image/png", "image/jpeg", "image/gif" };
        client.get(avatarUrl, new BinaryHttpResponseHandler(allowedContentTypes) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                    Bitmap UserAvatar = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);

                    CircleImageView ImgUserAvatar = (CircleImageView) findViewById(R.id.UserAvatar);
                    ImgUserAvatar.setImageBitmap(UserAvatar);

                    CircleImageView ImgDrawerAvatar = (CircleImageView) findViewById(R.id.ImgUserAvatar);
                    ImgDrawerAvatar.setImageBitmap(UserAvatar);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) { }
        });

        String hostname = "http://api.brillamexico.org";
        client.get(hostname + "/user/" + fbID, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject user = response;

                    TextView LabelUserName = (TextView) findViewById(R.id.LabelUserName);
                    TextView LabelCountFotos = (TextView) findViewById(R.id.LabelCountFotos);
                    TextView LabelCountPuntos = (TextView) findViewById(R.id.LabelCountPuntos);
                    TextView LabelCountLogros = (TextView) findViewById(R.id.LabelCountLogros);

                    TextView DrawerUserName = (TextView) findViewById(R.id.UserName);
                    TextView DrawerCountPuntos = (TextView) findViewById(R.id.UserPoints);

                    LabelUserName.setText( user.getString("name") );
                    DrawerUserName.setText( user.getString("name") );

                    LabelCountPuntos.setText( user.getString("points") );
                    DrawerCountPuntos.setText( user.getString("points") + " puntos" );

                    LabelCountFotos.setText("0");
                    LabelCountLogros.setText("0");

                } catch (JSONException e) {}
            }
        });
    }

    public void DrawableEvents () {
        // My Perfil
        LinearLayout toMyProfile = (LinearLayout) findViewById(R.id.dw_myprofile);
        toMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "Ya estás aquí", Toast.LENGTH_SHORT).show();
            }
        });

        // Actividad
        LinearLayout toActivity = (LinearLayout) findViewById(R.id.dw_activity);
        toActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "Click en Actividad", Toast.LENGTH_SHORT).show();
            }
        });

        // Noticias
        LinearLayout toNews = (LinearLayout) findViewById(R.id.dw_news);
        toNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "Click en Noticias", Toast.LENGTH_SHORT).show();
            }
        });

        // Tutorial
        LinearLayout toTuto = (LinearLayout) findViewById(R.id.dw_tuto);
        toTuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "Click en Tutorial", Toast.LENGTH_SHORT).show();
            }
        });

        // Privacidad
        LinearLayout toPrivacy = (LinearLayout) findViewById(R.id.dw_privacy);
        toPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "Click en Privacidad", Toast.LENGTH_SHORT).show();
            }
        });

        // Créditos
        LinearLayout toCredits = (LinearLayout) findViewById(R.id.dw_credits);
        toCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "Click en Créditos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
