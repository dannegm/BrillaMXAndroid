package mx.ambmultimedia.brillamexico;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

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
        String name = config.get("Nombre", "unknown");
        String points = config.get("Puntos", "0");

        TextView DrawerUserName = (TextView) findViewById(R.id.UserName);
        DrawerUserName.setText(name);
        TextView DrawerCountPuntos = (TextView) findViewById(R.id.UserPoints);
        DrawerCountPuntos.setText( points + " puntos" );

        CircleImageView ImgDrawerAvatar = (CircleImageView) findViewById(R.id.UserAvatar);
        String avatarUrl = getString(R.string.fb_avatar_link);
        avatarUrl = avatarUrl.replaceAll("__fbid__", fbID);

        Picasso.with(ctx)
                .load(avatarUrl)
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .into(ImgDrawerAvatar);
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

        // Salir
        LinearLayout toSalir = (LinearLayout) findViewById(R.id.dw_salir);
        toSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Noticias.this, Logout.class);
                startActivity(intent);
            }
        });
    }
}
