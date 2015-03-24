package mx.ambmultimedia.brillamexico.activities;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.bluejamesbond.text.DocumentView;
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

        final ImageView videoPreview = (ImageView) findViewById(R.id.videoPreview);
        video = (VideoView) findViewById(R.id.videoView);
        video.setVideoURI(uri);

        final MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(video);
        video.setMediaController(mediaController);
        drawer_layout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                mediaController.hide();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mediaController.hide();
            }

            @Override
            public void onDrawerClosed(View drawerView) { }

            @Override
            public void onDrawerStateChanged(int newState) {
                mediaController.hide();
            }
        });

        final FloatingActionButton playVideo = (FloatingActionButton) findViewById(R.id.playVideo);
        playVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVideo.setVisibility(View.INVISIBLE);
                videoPreview.setVisibility(View.INVISIBLE);
                video.start();
            }
        });

        float videoWidth = (float) video.getWidth();
        float videoHeight = videoWidth * 0.5625f;
        video.layout(0, 0, (int) videoWidth, (int) videoHeight);
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
                videoPreview.setVisibility(View.VISIBLE);
            }
        });
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                video.pause();
            }
        });

        /**
         * Formateado de texto
         */

        DocumentView documentView = (DocumentView) findViewById(R.id.largeText);
        String htmlText = String.valueOf(
                documentView.getText());

        htmlText = htmlText
                .replaceAll("¬", "<br><br>")
                .replaceAll("\\^", "<br>")

                .replaceAll("\\[nota\\]", "<font color=\"#797979\"><i>")
                .replaceAll("\\[\\/nota\\]", "</i></font>")

                .replaceAll("\\[\\{", "<font color=\"#490256\">")
                .replaceAll("\\}\\]", "</font>")

                .replaceAll("\\[", "<font color=\"#5b026b\"><b>")
                .replaceAll("\\]", ")</b></font>")

                .replaceAll("#####", "<h4><font color=\"#595959\">")
                .replaceAll("#/###", "</font></h4>")

                .replaceAll("####", "<h4>")
                .replaceAll("#/##", "</h4>")

                .replaceAll("###", "<h3><font color=\"#d32393\">")
                .replaceAll("#/#", "</font></h3>")

                .replaceAll("<li>", "<font color=\"#5b026b\">● </font>")
                .replaceAll("</li>", "<br><br>");

        Spanned htmlSpan = Html.fromHtml(htmlText);
        documentView.setText(htmlSpan);
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
