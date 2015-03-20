package mx.ambmultimedia.brillamexico.activities;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.ambmultimedia.brillamexico.utils.Config;
import mx.ambmultimedia.brillamexico.fragments.NavDrawerFrag;
import mx.ambmultimedia.brillamexico.R;
import mx.ambmultimedia.brillamexico.utils.DrawerUtils;


public class Bases extends ActionBarActivity {
    Context ctx;
    Config config;

    NavDrawerFrag navDrawerFragment;
    DrawerLayout drawer_layout;

    VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bases);
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

        String uriPath = "android.resource://mx.ambmultimedia.brillamexico/raw/bmx_video";
        Uri uri = Uri.parse(uriPath);

        video = (VideoView) findViewById(R.id.videoView);
        video.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(video);

        final FloatingActionButton playVideo = (FloatingActionButton) findViewById(R.id.playVideo);
        playVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo.setVisibility(View.INVISIBLE);
                video.start();
            }
        });

        float videoWidth = (float) video.getWidth();
        float videoHeight = videoWidth * 0.5625f;
        video.layout(0, 0, (int) videoWidth, (int) videoHeight);

        video.setMediaController(mediaController);
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer arg) {
                video.start();
                video.pause();
            }
        });
        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer vmp) {
                playVideo.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        video.stopPlayback();
    }

    @Override
    protected void onPause() {
        super.onPause();
        video.pause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        video.stopPlayback();
    }

    @Override
    public void onBackPressed () {
        if (drawer_layout.isDrawerOpen(Gravity.LEFT)){
            drawer_layout.closeDrawer(Gravity.LEFT);
        } else {
            super.onBackPressed();
            this.finish();
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
        } catch (JSONException e) { }

        CircleImageView ImgDrawerAvatar = (CircleImageView) findViewById(R.id.UserAvatar);
        String _avatarUrl = getString(R.string.fb_avatar_link);
        String miniAvatarUrl = _avatarUrl.replaceAll("__fbid__", fbID);
        Picasso.with(ctx)
                .load(miniAvatarUrl)
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .into(ImgDrawerAvatar);
    }
}
