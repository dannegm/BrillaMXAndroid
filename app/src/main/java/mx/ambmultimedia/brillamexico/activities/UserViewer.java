package mx.ambmultimedia.brillamexico.activities;

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

import com.cuneytayyildiz.widget.PullRefreshLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.ambmultimedia.brillamexico.R;
import mx.ambmultimedia.brillamexico.adapters.GridSelfies;
import mx.ambmultimedia.brillamexico.adapters.ListLogros;
import mx.ambmultimedia.brillamexico.customViews.ExtendableGridView;
import mx.ambmultimedia.brillamexico.fragments.NavDrawerFrag;
import mx.ambmultimedia.brillamexico.utils.Config;


public class UserViewer extends ActionBarActivity {
    Context ctx;
    Config config;

    String userID;
    Toolbar toolbar;
    PullRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_viewer);
        ctx = this;
        config = new Config(ctx);

        Bundle bundle = getIntent().getExtras();
        userID = bundle.getString("userID");

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        NavDrawerFrag navDrawerFragment = (NavDrawerFrag) getSupportFragmentManager().findFragmentById(R.id.navDrawer);
        DrawerLayout drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navDrawerFragment.setUp(R.id.navDrawer, drawer_layout, toolbar);

        DrawableEvents();
        GeneralEvents();
        BuildSelfProfile();

        BuildProfile();
        GetSelfies();

        refreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                BuildProfile();
                GetSelfies();
            }
        });
    }

    void BuildProfile () {
        AsyncHttpClient client = new AsyncHttpClient();
        String hostname = getString(R.string.hostname);
        client.get(hostname + "/user/" + userID, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject user = response;
                    toolbar.setTitle("Perfil de " + user.getString("name"));

                    TextView LabelUserName = (TextView) findViewById(R.id.LabelUserName);
                    LabelUserName.setText( user.getString("name") );

                    TextView LabelUserBio = (TextView) findViewById(R.id.LabelUserBio);
                    LabelUserBio.setText( user.getString("bio") );

                    TextView LabelCountPuntos = (TextView) findViewById(R.id.LabelCountPuntos);
                    LabelCountPuntos.setText( user.getString("points") + " puntos" );

                    TextView LabelCountLogros = (TextView) findViewById(R.id.LabelCountLogros);
                    String nLogros = user.getJSONArray("achievement").length() + "";
                    LabelCountLogros.setText(nLogros);

                    ListLogros adapter = new ListLogros(ctx, user.getJSONArray("achievement"));
                    ExtendableGridView listAchievements = (ExtendableGridView) findViewById(R.id.list_logros);
                    listAchievements.setAdapter(adapter);
                } catch (JSONException e) {
                }
            }
        });

        CircleImageView ImgUserAvatar = (CircleImageView) findViewById(R.id.ImgUserAvatar);
        String _avatarUrl = getString(R.string.fb_avatar_link);
        String avatarUrl = _avatarUrl.replaceAll("__fbid__", userID);
        Picasso.with(ctx)
                .load(avatarUrl)
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .into(ImgUserAvatar);
    }

    public void BuildSelfProfile () {
        String fbID = config.get("fbID", "0");
        String _user = config.get("user", "null");

        final TextView DrawerUserName = (TextView) findViewById(R.id.UserName);
        final TextView DrawerCountPuntos = (TextView) findViewById(R.id.UserPoints);

        try {
            JSONObject user = new JSONObject(_user);
            DrawerUserName.setText(user.getString("name"));
            DrawerCountPuntos.setText(user.getString("points") + " puntos");
        } catch (JSONException e) { }

        CircleImageView ImgDrawerAvatar = (CircleImageView) findViewById(R.id.UserAvatar);
        String _avatarUrl = getString(R.string.fb_avatar_link);
        String miniAvatarUrl = _avatarUrl.replaceAll("__fbid__", fbID);
        Picasso.with(ctx)
                .load(miniAvatarUrl)
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .into(ImgDrawerAvatar);
    }

    public void GetSelfies () {
        AsyncHttpClient client = new AsyncHttpClient();

        String hostname = getString(R.string.hostname);
        client.get(hostname + "/user/selfies/" + userID, null, new JsonHttpResponseHandler() {
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

                                Intent intent = new Intent(UserViewer.this, Selfie.class);
                                intent.putExtra("selfieID", selfieID);
                                startActivity(intent);
                            } catch (JSONException e) {}
                        }
                    });

                    TextView LabelCountFotos = (TextView) findViewById(R.id.LabelCountFotos);
                    String nFotos = String.valueOf(selfies.length());
                    LabelCountFotos.setText(nFotos);

                    refreshLayout.setRefreshing(false);

                } catch (Exception e) {}
            }
        });
    }

    public void GeneralEvents () {
    }

    public void DrawableEvents () {
        // My Perfil
        LinearLayout toMyProfile = (LinearLayout) findViewById(R.id.dw_myprofile);
        toMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserViewer.this, UserProfile.class);
                startActivity(intent);
            }
        });

        // Actividad
        LinearLayout toActivity = (LinearLayout) findViewById(R.id.dw_activity);
        toActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserViewer.this, Actividad.class);
                startActivity(intent);
            }
        });

        // Noticias
        LinearLayout toNoticias = (LinearLayout) findViewById(R.id.dw_news);
        toNoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserViewer.this, Noticias.class);
                startActivity(intent);
            }
        });

        // Emprendedores
        LinearLayout toEmp = (LinearLayout) findViewById(R.id.dw_emp);
        toEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserViewer.this, Emprendedores.class);
                startActivity(intent);
            }
        });

        // Otros

        // Bases
        LinearLayout toBases = (LinearLayout) findViewById(R.id.dw_bases);
        toBases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserViewer.this, Bases.class);
                startActivity(intent);
            }
        });

        // Privacidad
        LinearLayout toPrivacy = (LinearLayout) findViewById(R.id.dw_privacy);
        toPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserViewer.this, Privacy.class);
                startActivity(intent);
            }
        });

        // Salir
        LinearLayout toSalir = (LinearLayout) findViewById(R.id.dw_salir);
        toSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserViewer.this, Logout.class);
                startActivity(intent);
            }
        });
    }
}
