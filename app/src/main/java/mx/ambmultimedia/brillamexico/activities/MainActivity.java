package mx.ambmultimedia.brillamexico.activities;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import mx.ambmultimedia.brillamexico.R;

public class MainActivity extends ActionBarActivity {
    private Class actx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actx = UnitTestActivity.class;

        Intent intent = new Intent(this, actx);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(this, actx);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        Intent intent = new Intent(this, actx);
        startActivity(intent);
    }
}
