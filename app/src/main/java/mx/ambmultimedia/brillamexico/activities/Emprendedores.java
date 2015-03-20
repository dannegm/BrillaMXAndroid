package mx.ambmultimedia.brillamexico.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.ambmultimedia.brillamexico.fragments.NavDrawerFrag;
import mx.ambmultimedia.brillamexico.R;
import mx.ambmultimedia.brillamexico.utils.Config;
import mx.ambmultimedia.brillamexico.utils.DrawerUtils;


public class Emprendedores extends ActionBarActivity {
    Context ctx;
    Config config;

    NavDrawerFrag navDrawerFragment;
    DrawerLayout drawer_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emprendedores);
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
        GeneralEvents();
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

    public void GeneralEvents() {
        LinearLayout toEmp1 = (LinearLayout) findViewById(R.id.toEmp1);
        toEmp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Emprendedores.this, Emp1.class);
                startActivity(intent);
            }
        });
        LinearLayout toEmp2 = (LinearLayout) findViewById(R.id.toEmp2);
        toEmp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Emprendedores.this, Emp2.class);
                startActivity(intent);
            }
        });
        LinearLayout toEmp3 = (LinearLayout) findViewById(R.id.toEmp3);
        toEmp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Emprendedores.this, Emp3.class);
                startActivity(intent);
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
