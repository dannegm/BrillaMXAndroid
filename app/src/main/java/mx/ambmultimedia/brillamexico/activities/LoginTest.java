package mx.ambmultimedia.brillamexico.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import de.hdodenhof.circleimageview.CircleImageView;
import io.fabric.sdk.android.Fabric;
import mx.ambmultimedia.brillamexico.R;


public class LoginTest extends ActionBarActivity {
    Context ctx;

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "YRX5xRqc91Yxv8wuBOICIgH0h";
    private static final String TWITTER_SECRET = "SRYfHQtI0hXczFoRA0EGCO0rzEAxCC9bsdi2mnSdgtpnCCIwnj";

    TwitterLoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_test);
        ctx = this;

        // Inicializa la configuración de Twitter
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        /**
         * Inicializamos vistas del perfil
         */

        final CircleImageView twitterAvatar = (CircleImageView) findViewById(R.id.twitterAvatar);
        final TextView twitterName = (TextView) findViewById(R.id.twitterName);
        final TextView twitterUser = (TextView) findViewById(R.id.twitterUser);
    }

    @Override
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pasamos resultados al activity? Aún no lo entiendo bien :(
        loginButton.onActivityResult(requestCode, resultCode, data);
    }
}
