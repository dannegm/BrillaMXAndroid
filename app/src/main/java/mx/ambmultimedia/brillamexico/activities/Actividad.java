package mx.ambmultimedia.brillamexico.activities;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.widget.TextView;

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
import mx.ambmultimedia.brillamexico.utils.DrawerUtils;


public class Actividad extends ActionBarActivity {
    Context ctx;
    Config config;

    NavDrawerFrag navDrawerFragment;
    DrawerLayout drawer_layout;

    ViewPager activityPager;
    SlidingTabLayout activityTabs;

    String CampoDeAccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actividad);
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

        activityPager = (ViewPager) findViewById(R.id.activityPager);
        activityPager.setAdapter(new ActividadPagerApadter(ctx, getSupportFragmentManager(), CampoDeAccion));

        activityTabs = (SlidingTabLayout) findViewById(R.id.activityTabs);
        activityTabs.setViewPager(activityPager);
        activityTabs.setSelectedIndicatorColors(R.color.bmx_purple);

    }

    @Override
    public void onBackPressed () {
        if (drawer_layout.isDrawerOpen(Gravity.LEFT)){
            drawer_layout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
        }
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

            CampoDeAccion = user.getString("fieldaction_id");
        } catch (JSONException e) { }

        CircleImageView ImgDrawerAvatar = (CircleImageView) findViewById(R.id.UserAvatar);
        String _avatarUrl = getString(R.string.fb_avatar_link);
        String miniAvatarUrl = _avatarUrl.replaceAll("__fbid__", fbID);
        Picasso.with(ctx)
                .load(miniAvatarUrl)
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .into(ImgDrawerAvatar);
    }

    class ActividadPagerApadter extends FragmentPagerAdapter {
        Context superCtx;
        String[] tabText = getResources().getStringArray(R.array.tabs_actividad);
        int cdAction = 0;

        String[] leaderBoardTitle = {
          "Leaderboard", "Jovenes y adultos", "Emprendedores", "Empresarios"
        };

        public ActividadPagerApadter (Context _ctx, FragmentManager fm, String _cdAction) {
            super(fm);
            superCtx = _ctx;
            tabText = getResources().getStringArray(R.array.tabs_actividad);
            cdAction = Integer.valueOf(_cdAction);
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
            if (position == 0) {
                return leaderBoardTitle[cdAction];
            } else {
                return tabText[position];
            }
        }
    }
}
