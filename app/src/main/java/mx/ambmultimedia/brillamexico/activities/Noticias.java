package mx.ambmultimedia.brillamexico.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

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
import mx.ambmultimedia.brillamexico.adapters.ListNoticias;
import mx.ambmultimedia.brillamexico.customViews.ExtendableListView;
import mx.ambmultimedia.brillamexico.fragments.NavDrawerFrag;
import mx.ambmultimedia.brillamexico.utils.Config;
import mx.ambmultimedia.brillamexico.utils.DrawerUtils;


public class Noticias extends ActionBarActivity {
    Context ctx;
    Config config;

    NavDrawerFrag navDrawerFragment;
    DrawerLayout drawer_layout;

    ExtendableListView listNoticias;
    PullRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticias);
        ctx = this;
        config = new Config(ctx);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        navDrawerFragment = (NavDrawerFrag) getSupportFragmentManager().findFragmentById(R.id.navDrawer);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout2);
        navDrawerFragment.setUp(R.id.navDrawer, drawer_layout, toolbar);

        DrawerUtils drawerutils = new DrawerUtils(this, this);
        drawerutils.Navigation(drawer_layout);

        BuildProfile();
        GetNoticias(1);

        listNoticias = (ExtendableListView) findViewById(R.id.listNoticias);

        refreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetNoticias(1);
            }
        });
    }

    public void BuildProfile () {
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

    public void GetNoticias (int page) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://brillamexico.org/api.php?page=" + page, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    final JSONArray noticias = response;
                    ListNoticias adapter = new ListNoticias(ctx, noticias);

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

                    refreshLayout.setRefreshing(false);
                } catch (Exception e) { }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable e) {
                String msg = "[" + statusCode + "|b/noticias] " + e.getMessage();
                Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed () {
        if (drawer_layout.isDrawerOpen(Gravity.LEFT)){
            drawer_layout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
    }
}
