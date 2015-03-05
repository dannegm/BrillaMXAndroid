package mx.ambmultimedia.brillamexico;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;


public class LoginStep1 extends ActionBarActivity {
    Context ctx;
    Config config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_step1);
        ctx = this;
        config = new Config(ctx);

        Boolean isLogin = Boolean.valueOf(config.get("isLogin", "false"));
        if (isLogin) {
            Intent intent = new Intent(LoginStep1.this, UserProfile.class);
            startActivity(intent);
        } else {
            RelativeLayout toStep2 = (RelativeLayout) findViewById(R.id.goToStep2);
            toStep2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LoginStep1.this, LoginStep2.class);
                    startActivity(intent);
                }
            });
        }
    }
}
