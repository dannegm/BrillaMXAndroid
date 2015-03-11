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

import com.cuneytayyildiz.widget.PullRefreshLayout;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;


public class LeaderBoard extends ActionBarActivity {
    Context ctx;
    Config config;

    PullRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leader_board);
        ctx = this;
        config = new Config(ctx);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        NavDrawerFrag navDrawerFragment = (NavDrawerFrag) getSupportFragmentManager().findFragmentById(R.id.navDrawer);
        DrawerLayout drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout2);
        navDrawerFragment.setUp(R.id.navDrawer, drawer_layout, toolbar);

        DrawableEvents();
        GeneralEvents();
        BuildProfile();
        GetLeaderBoard();

        refreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetLeaderBoard();
            }
        });
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

    public void GetLeaderBoard () {
        String campoDeAccion = config.get("CampoDeAccion", "2");
        AsyncHttpClient client = new AsyncHttpClient();

        String hostname = getString(R.string.hostname);
        client.get(hostname + "/users/leaderboard/" + campoDeAccion, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    final JSONArray users = response;

                    ListLeaderBoard adapter = new ListLeaderBoard(ctx, users);
                    ExtendableListView listUsers = (ExtendableListView) findViewById(R.id.lisLeaderBoard);

                    listUsers.setAdapter(adapter);
                    listUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                JSONObject user = users.getJSONObject(position);
                                String userID = user.getString("fbid");

                                Intent intent = new Intent(LeaderBoard.this, UserViewer.class);
                                intent.putExtra("userID", userID);
                                startActivity(intent);
                            } catch (JSONException e) {}
                        }
                    });

                    refreshLayout.setRefreshing(false);

                } catch (Exception e) {
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable e) {}
        });
    }

    public void GeneralEvents () {
        FloatingActionButton toSelfie = (FloatingActionButton) findViewById(R.id.toSelfie);
        toSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaderBoard.this, Foto.class);
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
                Intent intent = new Intent(LeaderBoard.this, UserProfile.class);
                startActivity(intent);
            }
        });

        // Actividad
        LinearLayout toActivity = (LinearLayout) findViewById(R.id.dw_activity);
        toActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "Ya estás aquí", Toast.LENGTH_SHORT).show();
            }
        });

        // Noticias
        LinearLayout toNoticias = (LinearLayout) findViewById(R.id.dw_news);
        toNoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaderBoard.this, Noticias.class);
                startActivity(intent);
            }
        });

        // Emprendedores
        LinearLayout toEmp = (LinearLayout) findViewById(R.id.dw_emp);
        toEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaderBoard.this, Emprendedores.class);
                startActivity(intent);
            }
        });

        // Salir
        LinearLayout toSalir = (LinearLayout) findViewById(R.id.dw_salir);
        toSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeaderBoard.this, Logout.class);
                startActivity(intent);
            }
        });
    }
}
