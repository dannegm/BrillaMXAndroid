package mx.ambmultimedia.brillamexico.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LikeView;
import com.fmsirvent.ParallaxEverywhere.PEWImageView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import mx.ambmultimedia.brillamexico.R;
import mx.ambmultimedia.brillamexico.utils.URLImageParser;


public class NoticiaSelf extends ActionBarActivity {
    Context ctx;
    Activity atx;

    private UiLifecycleHelper uiHelper;
    private LikeView like_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia_self);
        ctx = this;
        atx = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String noticiaID = bundle.getString("noticiaID");

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://brillamexico.org/api.php?post=" + noticiaID, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    // Cover
                    String cover = response.getString("imagen");
                    PEWImageView nCover = (PEWImageView) findViewById(R.id.nCover);
                    Picasso.with(ctx)
                            .load(cover)
                            .placeholder(R.drawable.img_placeholder)
                            .error(R.drawable.paisaje_error)
                            .into(nCover);

                    // Título
                    final String title = response.getString("title");
                    TextView nTitle = (TextView) findViewById(R.id.nTitle);
                    nTitle.setText(title);

                    // Fecha
                    String date = response.getString("date");
                    TextView nDate = (TextView) findViewById(R.id.nDate);
                    nDate.setText(date);

                    // Contenido
                    String htmlText = response.getString("content");
                    TextView documentView = (TextView) findViewById(R.id.nContent);

                    URLImageParser p = new URLImageParser(documentView, ctx);
                    Spanned htmlSpan = Html.fromHtml(htmlText, p, null);

                    documentView.setText(htmlSpan);

                    // Share Action
                    final String link = response.getString("link");
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

                    /**
                     * Facebook like
                     */

                    uiHelper = new UiLifecycleHelper(atx, mStatusCallback);

                    like_view = (LikeView) findViewById(R.id.likeNoti);
                    like_view.setObjectId(link);
                    like_view.setLikeViewStyle(LikeView.Style.STANDARD);
                    like_view.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
                    like_view.setHorizontalAlignment(LikeView.HorizontalAlignment.LEFT);
                } catch (JSONException e) {
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable e) {
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
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
