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
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
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
            GeneralEvents();
            BuildProfile();
            GetSelfies();
        }
    }

    public void BuildProfile () {
        String fbID = config.get("fbID", "0");
        String name = config.get("Nombre", "unknown");
        String points = config.get("Puntos", "0");

        final TextView LabelUserName = (TextView) findViewById(R.id.LabelUserName);
        LabelUserName.setText(name);

        final TextView LabelCountPuntos = (TextView) findViewById(R.id.LabelCountPuntos);
            LabelCountPuntos.setText( points + " puntos" );
        final TextView LabelCountLogros = (TextView) findViewById(R.id.LabelCountLogros);
            LabelCountLogros.setText("0");

        final TextView DrawerUserName = (TextView) findViewById(R.id.UserName);
            DrawerUserName.setText(name);
        final TextView DrawerCountPuntos = (TextView) findViewById(R.id.UserPoints);
            DrawerCountPuntos.setText( points + " puntos" );

        if (name == "unknown") {
            AsyncHttpClient client = new AsyncHttpClient();
            String hostname = "http://api.brillamexico.org";
            client.get(hostname + "/user/" + fbID, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    try {
                        JSONObject user = response;

                        LabelUserName.setText(user.getString("name"));
                        DrawerUserName.setText(user.getString("name"));

                        LabelCountPuntos.setText(user.getString("points"));
                        DrawerCountPuntos.setText(user.getString("points") + " puntos");

                        config.set("Nombre", user.getString("name"));
                        config.set("Puntos", user.getString("points"));
                    } catch (JSONException e) {
                    }
                }
            });
        }

        CircleImageView ImgUserAvatar = (CircleImageView) findViewById(R.id.ImgUserAvatar);
        CircleImageView ImgDrawerAvatar = (CircleImageView) findViewById(R.id.UserAvatar);

        String avatarUrl = getString(R.string.fb_avatar_link);
        avatarUrl = avatarUrl.replaceAll("__fbid__", fbID);

        Picasso.with(ctx)
                .load(avatarUrl)
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .into(ImgUserAvatar);
        Picasso.with(ctx)
                .load(avatarUrl)
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .into(ImgDrawerAvatar);
    }

    public void GetSelfies () {
        String fbID = config.get("fbID", "0");
        AsyncHttpClient client = new AsyncHttpClient();

        String hostname = "http://danielgarcia.biz";
        client.get(hostname + "/user/selfies/" + fbID, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    final JSONArray selfies = response;

                    GridSelfies adapter = new GridSelfies(ctx, selfies);
                    ExtendableGridView gridSelfies = (ExtendableGridView) findViewById(R.id.selfiesGrid);
                    gridSelfies.setAdapter(adapter);
                    gridSelfies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                JSONObject selfie = selfies.getJSONObject(position);
                                String selfieID = selfie.getString("id");

                                Intent intent = new Intent(UserProfile.this, Selfie.class);
                                intent.putExtra("selfieID", selfieID);
                                startActivity(intent);
                            } catch (JSONException e) {}
                        }
                    });

                    TextView LabelCountFotos = (TextView) findViewById(R.id.LabelCountFotos);
                    String nFotos = String.valueOf(selfies.length());
                    LabelCountFotos.setText(nFotos);

                } catch (Exception e) {}
            }
        });
    }

    public void GeneralEvents () {
        FloatingActionButton toSelfie = (FloatingActionButton) findViewById(R.id.toSelfie);
        toSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, Foto.class);
                startActivity(intent);
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
                Intent intent = new Intent(UserProfile.this, LeaderBoard.class);
                startActivity(intent);
            }
        });

        // Noticias
        LinearLayout toNoticias = (LinearLayout) findViewById(R.id.dw_news);
        toNoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, Noticias.class);
                startActivity(intent);
            }
        });

        // Emprendedores
        LinearLayout toEmp = (LinearLayout) findViewById(R.id.dw_emp);
        toEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, Emprendedores.class);
                startActivity(intent);
            }
        });

        // Salir
        LinearLayout toSalir = (LinearLayout) findViewById(R.id.dw_salir);
        toSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, Logout.class);
                startActivity(intent);
            }
        });
    }
}
