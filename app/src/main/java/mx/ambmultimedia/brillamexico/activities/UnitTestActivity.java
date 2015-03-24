package mx.ambmultimedia.brillamexico.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import mx.ambmultimedia.brillamexico.R;

public class UnitTestActivity extends ActionBarActivity {

    private Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unit_test);
        ctx = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void logro1 (View v) {
        Intent intent = new Intent(ctx, Logro.class);
        intent.putExtra("Reference", "Register");
        intent.putExtra("LogroID", "1");
        startActivity(intent);
    }

    public void logro2 (View v) {
        Intent intent = new Intent(ctx, Logro.class);
        intent.putExtra("Reference", "EditUser");
        intent.putExtra("LogroID", "2");
        startActivity(intent);
    }

    public void logro3 (View v) {
        Intent intent = new Intent(ctx, Logro.class);
        intent.putExtra("Reference", "TwitterLinked");
        intent.putExtra("LogroID", "6");
        startActivity(intent);
    }

    public void logro4 (View v) {
        Intent intent = new Intent(ctx, Logro.class);
        intent.putExtra("selfieID", "165");
        intent.putExtra("Reference", "Selfie");
        intent.putExtra("LogroID", "3");
        startActivity(intent);
    }

    public void logro5 (View v) {
        Intent intent = new Intent(ctx, Logro.class);
        intent.putExtra("selfieID",  "165");
        intent.putExtra("Reference", "Selfie");
        intent.putExtra("LogroID", "4");
        startActivity(intent);
    }

    public void logro6 (View v) {
        Intent intent = new Intent(ctx, Logro.class);
        intent.putExtra("Reference", "None");
        intent.putExtra("LogroID", "5");
        startActivity(intent);
    }
}
