package mx.ambmultimedia.brillamexico.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.ambmultimedia.brillamexico.utils.Config;
import mx.ambmultimedia.brillamexico.fragments.LeaderBoard;
import mx.ambmultimedia.brillamexico.fragments.NavDrawerFrag;
import mx.ambmultimedia.brillamexico.R;
import mx.ambmultimedia.brillamexico.fragments.Selfies;
import mx.ambmultimedia.brillamexico.customViews.SlidingTabLayout;
import mx.ambmultimedia.brillamexico.fragments.TopUsers;


public class Actividad extends ActionBarActivity {
    Context ctx;
    Config config;

    ViewPager activityPager;
    SlidingTabLayout activityTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad);
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

        activityPager = (ViewPager) findViewById(R.id.activityPager);
        activityPager.setAdapter(new ActividadPagerApadter(ctx, getSupportFragmentManager()));

        activityTabs = (SlidingTabLayout) findViewById(R.id.activityTabs);
        activityTabs.setViewPager(activityPager);
        activityTabs.setSelectedIndicatorColors(R.color.bmx_purple);

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

    public void DrawableEvents () {
        // My Perfil
        LinearLayout toMyProfile = (LinearLayout) findViewById(R.id.dw_myprofile);
        toMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Actividad.this, UserProfile.class);
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
                Intent intent = new Intent(Actividad.this, Noticias.class);
                startActivity(intent);
            }
        });

        // Emprendedores
        LinearLayout toEmp = (LinearLayout) findViewById(R.id.dw_emp);
        toEmp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Actividad.this, Emprendedores.class);
                startActivity(intent);
            }
        });

        // Otros

        // Bases
        LinearLayout toBases = (LinearLayout) findViewById(R.id.dw_bases);
        toBases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Actividad.this, Bases.class);
                startActivity(intent);
            }
        });

        // Privacidad
        LinearLayout toPrivacy = (LinearLayout) findViewById(R.id.dw_privacy);
        toPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Actividad.this, Privacy.class);
                startActivity(intent);
            }
        });

        // Salir
        LinearLayout toSalir = (LinearLayout) findViewById(R.id.dw_salir);
        toSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Actividad.this, Logout.class);
                startActivity(intent);
            }
        });
    }

    class ActividadPagerApadter extends FragmentPagerAdapter {
        Context superCtx;
        String[] tabText = getResources().getStringArray(R.array.tabs_actividad);

        public ActividadPagerApadter (Context _ctx, FragmentManager fm) {
            super(fm);
            superCtx = _ctx;
            tabText = getResources().getStringArray(R.array.tabs_actividad);
        }
        public Fragment getItem (int position) {
            if (position == 0) {
                LeaderBoard lborad = new LeaderBoard(superCtx);
                return lborad;
            }
            else if (position == 1) {
                TopUsers tUsers = new TopUsers(superCtx);
                return tUsers;
            }
            else if (position == 2) {
                Selfies fSelfies = new Selfies(superCtx);
                return fSelfies;
            } else {
                LeaderBoard lborad = new LeaderBoard(superCtx);
                return lborad;
            }
        }

        @Override
        public int getCount() {
            return tabText.length;
        }

        public CharSequence getPageTitle (int position) {
            return tabText[position];
        }
    }
}
