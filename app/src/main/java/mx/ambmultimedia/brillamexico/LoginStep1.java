package mx.ambmultimedia.brillamexico;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class LoginStep1 extends ActionBarActivity {
    Context ctx;
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_step1);
        ctx = this;
        config = new Config(ctx);

        WebView youtube = (WebView) findViewById(R.id.videoYoutube);
        youtube.setWebChromeClient(new WebChromeClient());

        WebSettings ws = youtube.getSettings();
        ws.setBuiltInZoomControls(true);
        ws.setJavaScriptEnabled(true);
        youtube.loadUrl("https://www.youtube.com/embed/t1UPLPS419E");

        Boolean isLogin = Boolean.valueOf(config.get("isLogin", "false"));
        if (isLogin) {
            Intent intent = new Intent(LoginStep1.this, UserProfile.class);
            startActivity(intent);
        } else {
            Button toStep2 = (Button) findViewById(R.id.toStepp22);
            toStep2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginStep1.this, LoginStep2.class);
                    startActivity(intent);
                }
            });

            TextView toLogin = (TextView) findViewById(R.id.toLogin);
            toLogin.setText(Html.fromHtml(toLogin.getText().toString()));
            toLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginStep1.this, LoginStep5.class);

                    intent.putExtra("ReturnUser", "true");
                    intent.putExtra("CampoDeAccion", "");
                    intent.putExtra("Nombre", "unknown");

                    startActivity(intent);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
