package mx.ambmultimedia.brillamexico.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import mx.ambmultimedia.brillamexico.R;

public class MainActivity extends ActionBarActivity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "YRX5xRqc91Yxv8wuBOICIgH0h";
    private static final String TWITTER_SECRET = "SRYfHQtI0hXczFoRA0EGCO0rzEAxCC9bsdi2mnSdgtpnCCIwnj";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, UserProfile.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(this, UserProfile.class);
        startActivity(intent);
    }
}
