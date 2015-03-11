package mx.ambmultimedia.brillamexico;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluejamesbond.text.DocumentView;
import com.bluejamesbond.text.style.TextAlignment;
import com.fmsirvent.ParallaxEverywhere.PEWImageView;
import com.fmsirvent.ParallaxEverywhere.PEWTextView;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class NoticiaSelf extends ActionBarActivity {
    Context ctx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noticia_self);
        ctx = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String noticiaID = bundle.getString("noticiaID");

        /*
        WebView noticiasViewer = (WebView) findViewById(R.id.noticiasViewer);
        noticiasViewer.loadUrl("http://brillamexico.org/api.php?id=" + noticiaID);
        */

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
}
