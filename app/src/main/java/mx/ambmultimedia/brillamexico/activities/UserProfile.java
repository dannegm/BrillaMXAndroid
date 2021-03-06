package mx.ambmultimedia.brillamexico.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
import mx.ambmultimedia.brillamexico.R;
import mx.ambmultimedia.brillamexico.adapters.GridSelfies;
import mx.ambmultimedia.brillamexico.adapters.ListLogros;
import mx.ambmultimedia.brillamexico.customViews.ExtendableGridView;
import mx.ambmultimedia.brillamexico.fragments.NavDrawerFrag;
import mx.ambmultimedia.brillamexico.utils.Config;
import mx.ambmultimedia.brillamexico.utils.DrawerUtils;


public class UserProfile extends ActionBarActivity {
    Context ctx;
    Config config;

    PullRefreshLayout refreshLayout;
    DrawerLayout drawer_layout;
    NavDrawerFrag navDrawerFragment;

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

            navDrawerFragment = (NavDrawerFrag) getSupportFragmentManager().findFragmentById(R.id.navDrawer);
            drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);
            navDrawerFragment.setUp(R.id.navDrawer, drawer_layout, toolbar);

            DrawerUtils drawerutils = new DrawerUtils(this, this);
            drawerutils.Navigation(drawer_layout);

            GeneralEvents();
            BuildProfile(false);
            GetSelfies();

            refreshLayout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
            refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    BuildProfile(true);
                    GetSelfies();
                }
            });
        }
    }

    public void BuildProfile (Boolean refresh) {
        String fbID = config.get("fbID", "0");
        String _user = config.get("user", "null");
        Boolean isReload = Boolean.valueOf(config.get("isReload", "false"));

        if (isReload || refresh) {
            AsyncHttpClient client = new AsyncHttpClient();
            String hostname = getString(R.string.hostname);
            client.get(hostname + "/user/" + fbID, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    JSONObject user = response;
                    MakeProfile(user);
                    config.set("user", user.toString());
                    config.get("isReload", "false");
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                    super.onFailure(statusCode, headers, responseString, e);
                    refreshLayout.setRefreshing(false);
                    String msg = "[" + statusCode + "|u/get] " + e.getMessage();
                    Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            try {
                JSONObject cacheUser = new JSONObject(_user);
                MakeProfile(cacheUser);
            }
            catch (JSONException e) { }
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

    public void MakeProfile (JSONObject user) {
        final TextView LabelUserName = (TextView) findViewById(R.id.LabelUserName);
        final TextView LabelUserBio = (TextView) findViewById(R.id.LabelUserBio);

        final TextView LabelCountPuntos = (TextView) findViewById(R.id.LabelCountPuntos);
        final TextView LabelCountLogros = (TextView) findViewById(R.id.LabelCountLogros);

        final TextView DrawerUserName = (TextView) findViewById(R.id.UserName);
        final TextView DrawerCountPuntos = (TextView) findViewById(R.id.UserPoints);

        try {
            LabelUserName.setText(user.getString("name"));
            LabelUserBio.setText(user.getString("bio"));
            DrawerUserName.setText(user.getString("name"));

            LabelCountPuntos.setText(user.getString("points"));
            DrawerCountPuntos.setText(user.getString("points") + " puntos");

            String nLogros = user.getJSONArray("achievement").length() + "";
            LabelCountLogros.setText(nLogros);


            ListLogros adapter = new ListLogros(ctx, user.getJSONArray("achievement"));
            ExtendableGridView listAchievements = (ExtendableGridView) findViewById(R.id.list_logros);
            listAchievements.setAdapter(adapter);

        } catch (JSONException e) { }
    }

    public void GetSelfies () {
        String fbID = config.get("fbID", "0");
        AsyncHttpClient client = new AsyncHttpClient();

        String hostname = getString(R.string.hostname);
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

                    refreshLayout.setRefreshing(false);

                } catch (Exception e) {}
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
                refreshLayout.setRefreshing(false);
                String msg = "[" + statusCode + "|u/selfies] " + e.getMessage();
                Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void GeneralEvents () {
        FloatingActionButton toSelfie = (FloatingActionButton) findViewById(R.id.toSelfie);
        toSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, Compromisos.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.edit_profile:
                Intent intent = new Intent(UserProfile.this, EditUserInfo.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(Gravity.LEFT)) {
            drawer_layout.closeDrawer(Gravity.LEFT);
        } else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}
