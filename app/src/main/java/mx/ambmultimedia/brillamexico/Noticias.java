package mx.ambmultimedia.brillamexico;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;


public class Noticias extends ActionBarActivity {
    Context ctx;
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);
        ctx = this;
        config = new Config(ctx);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        NavDrawerFrag navDrawerFragment = (NavDrawerFrag) getSupportFragmentManager().findFragmentById(R.id.navDrawer);
        DrawerLayout drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout2);
        navDrawerFragment.setUp(R.id.navDrawer, drawer_layout, toolbar);

        BuildProfile();
        DrawableEvents();
        GetNoticias();
    }

    public void BuildProfile () {
        String fbID = config.get("fbID", "0");
        AsyncHttpClient client = new AsyncHttpClient();

        String hostname = "http://api.brillamexico.org";
        client.get(hostname + "/user/" + fbID, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject user = response;

                    TextView DrawerUserName = (TextView) findViewById(R.id.UserName);
                    TextView DrawerCountPuntos = (TextView) findViewById(R.id.UserPoints);

                    DrawerUserName.setText( user.getString("name") );
                    DrawerCountPuntos.setText( user.getString("points") + " puntos" );

                } catch (JSONException e) {}
            }
        });

        String avatarUrl = "https://graph.facebook.com/__fbid__/picture";
        avatarUrl = avatarUrl.replaceAll("__fbid__", fbID);
        String[] allowedContentTypes = new String[]{"image/png", "image/jpeg", "image/gif"};

        client.get(avatarUrl, new BinaryHttpResponseHandler(allowedContentTypes) {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                Bitmap selfieThumb = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);

                CircleImageView imageView = (CircleImageView) findViewById(R.id.UserAvatar);
                imageView.setImageBitmap(selfieThumb);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
            }
        });
    }

    public void GetNoticias () {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://brillamexico.org/api.php?page=1", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    final JSONArray noticias = response;

                    ListNoticias adapter = new ListNoticias(ctx, noticias);
                    ExtendableListView listNoticias = (ExtendableListView) findViewById(R.id.listNoticias);

                    listNoticias.setAdapter(adapter);
                    listNoticias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                JSONObject noticia = noticias.getJSONObject(position);
                                String noticiaID = noticia.getString("id");

                                Intent intent = new Intent(Noticias.this, NoticiaSelf.class);
                                intent.putExtra("noticiaID", noticiaID);
                                startActivity(intent);
                            } catch (JSONException e) {}
                        }
                    });
                } catch (Exception e) { }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable e) { }
        });
    }

    public void DrawableEvents () {
        // My Perfil
        LinearLayout toMyProfile = (LinearLayout) findViewById(R.id.dw_myprofile);
        toMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Noticias.this, UserProfile.class);
                startActivity(intent);
            }
        });

        // Actividad
        LinearLayout toActivity = (LinearLayout) findViewById(R.id.dw_activity);
        toActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Noticias.this, LeaderBoard.class);
                startActivity(intent);
            }
        });

        // Actividad
        LinearLayout toNoticias = (LinearLayout) findViewById(R.id.dw_news);
        toNoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "Ya estás aquí", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
