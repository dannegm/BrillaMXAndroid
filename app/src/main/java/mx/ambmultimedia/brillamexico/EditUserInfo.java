package mx.ambmultimedia.brillamexico;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditUserInfo extends ActionBarActivity {
    Context ctx;
    Config config;
    String fbID;

    EditText nName;
    EditText nBio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);
        ctx = this;
        config = new Config(ctx);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fbID = config.get("fbID", "0");
        CircleImageView ImgUserAvatar = (CircleImageView) findViewById(R.id.ImgUserAvatar);
        String avatarUrl = getString(R.string.fb_avatar_link);
        avatarUrl = avatarUrl.replaceAll("__fbid__", fbID);

        Picasso.with(ctx)
                .load(avatarUrl)
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .into(ImgUserAvatar);

        nName = (EditText) findViewById(R.id.editName);
        nBio = (EditText) findViewById(R.id.editBio);

        nName.setHint(config.get("name", ""));
        nBio.setHint(config.get("bio", ""));

        Button saveData = (Button) findViewById(R.id.saveData);
        saveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateData();
            }
        });
    }


    public void UpdateData () {
        fbID = config.get("fbID", "0");
        final Boolean isCompleteProfile = Boolean.valueOf(config.get("isCompleteProfile", "false"));

        RequestParams nuevoUsuario = new RequestParams();
        nuevoUsuario.put("name", nName.getText().toString());
        nuevoUsuario.put("bio", nBio.getText().toString());

        final String hostname = getString(R.string.hostname);
        final AsyncHttpClient client = new AsyncHttpClient();
        client.post(hostname + "/user/edit/" + fbID, nuevoUsuario, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                if (!isCompleteProfile) {
                    RequestParams points = new RequestParams();
                    points.put("points", "50");
                    client.post(hostname + "/user/points/" + fbID, points, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                            Toast.makeText(ctx, "Has ganado 50 puntos", Toast.LENGTH_LONG).show();
                            config.get("isCompleteProfile", "true");
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, String response, Throwable e) {
                        }
                    });
                }
                config.set("Nombre", "unknown");
                config.set("Bio", nBio.getText().toString());

                Intent intent = new Intent(EditUserInfo.this, UserProfile.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable e) {
                Toast.makeText(ctx, "¡Ups! Something was wrong :(", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
