package mx.ambmultimedia.brillamexico.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.ambmultimedia.brillamexico.R;
import mx.ambmultimedia.brillamexico.utils.Config;


public class EraseAcount extends ActionBarActivity {
    Context ctx;
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_erase_acount);
        ctx = this;
        config = new Config(ctx);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        BuildProfile();

        Button cancel = (Button) findViewById(R.id.deleteCancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EraseAcount.this, Logout.class);
                startActivity(intent);
            }
        });
        Button doit = (Button) findViewById(R.id.deleteConfirm);
        doit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String hostname = getString(R.string.hostname);
                AsyncHttpClient client = new AsyncHttpClient();
                String fbID = config.get("fbID", "0");
                client.post(hostname + "/user/delete/" + fbID, null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        config.clear();
                        Intent intent = new Intent(EraseAcount.this, LoginStep1.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String response, Throwable e) {
                        String msg = "[" + statusCode + "|u/delete] " + e.getMessage();
                        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void BuildProfile () {
        String fbID = config.get("fbID", "0");
        String _user = config.get("user", "null");

        final TextView DrawerUserName = (TextView) findViewById(R.id.l_userName);

        try {
            JSONObject user = new JSONObject(_user);
            DrawerUserName.setText(user.getString("name"));
        } catch (JSONException e) { }

        CircleImageView ImgDrawerAvatar = (CircleImageView) findViewById(R.id.l_usersAvatar);
        String _avatarUrl = getString(R.string.fb_avatar_link);
        String miniAvatarUrl = _avatarUrl.replaceAll("__fbid__", fbID);
        Picasso.with(ctx)
                .load(miniAvatarUrl)
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .into(ImgDrawerAvatar);
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
}
