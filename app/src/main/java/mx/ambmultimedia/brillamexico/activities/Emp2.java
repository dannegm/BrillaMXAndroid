package mx.ambmultimedia.brillamexico.activities;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LikeView;
import com.getbase.floatingactionbutton.FloatingActionButton;

import mx.ambmultimedia.brillamexico.R;


public class Emp2 extends ActionBarActivity {
    Context ctx;

    private UiLifecycleHelper uiHelper;
    private LikeView like_view;
    private String like_url = "http://www.brillamexico.org/jahir-mojica-hernandez/";

    VideoView video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp2);
        ctx = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**
         * Facebook like
         */

        uiHelper = new UiLifecycleHelper(this, mStatusCallback);

        like_view = (LikeView) findViewById(R.id.likeEmp1);
        like_view.setObjectId(like_url);
        like_view.setLikeViewStyle(LikeView.Style.STANDARD);
        like_view.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
        like_view.setHorizontalAlignment(LikeView.HorizontalAlignment.LEFT);

        /**
         * Video
         */

        String uriPath = "android.resource://mx.ambmultimedia.brillamexico/raw/bmx_emp2";
        Uri uri = Uri.parse(uriPath);

        final ImageView videoPreview = (ImageView) findViewById(R.id.videoPreview);
        video = (VideoView) findViewById(R.id.videoView);
        video.setVideoURI(uri);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(video);
        video.setMediaController(mediaController);

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
         * Compartir emprendedor
         */

        final String title = "ABRAHAM CHETEWY";
        final String link = "http://www.brillamexico.org/abraham-chetewy/";
        FloatingActionButton share = (FloatingActionButton) findViewById(R.id.shareContent);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");

                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, link);

                startActivity(Intent.createChooser(sharingIntent, "Compartir"));
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
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    Session.StatusCallback mStatusCallback = new Session.StatusCallback (){
        @Override
        public void call(Session session, SessionState state, Exception exception) {

        }
    };

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, null);
    }
}
