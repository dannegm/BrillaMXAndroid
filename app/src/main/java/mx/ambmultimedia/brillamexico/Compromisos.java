package mx.ambmultimedia.brillamexico;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;


public class Compromisos extends ActionBarActivity {
    Context ctx;
    Config config;

    ViewPager activityPager;
    String CampoDeAccion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compromisos);
        ctx = this;
        config = new Config(ctx);

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            JSONObject selfuser = new JSONObject(config.get("user", "null"));
            CampoDeAccion = selfuser.getString("fieldaction_id");
        } catch (JSONException e) {}
        int fieldToaction = Integer.parseInt(CampoDeAccion);

        activityPager = (ViewPager) findViewById(R.id.activityPager);
        activityPager.setAdapter(new CompromisoPagerApadter(ctx, getSupportFragmentManager(), fieldToaction));
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

    class CompromisoPagerApadter extends FragmentPagerAdapter {
        Context superCtx;
        String[] compromisos;
        int action;

        public CompromisoPagerApadter (Context _ctx, FragmentManager fm, int FieldToAction) {
            super(fm);
            superCtx = _ctx;
            action = FieldToAction;

            switch (FieldToAction) {
                case 1: compromisos = getResources().getStringArray(R.array.campo1); break;
                case 2: compromisos = getResources().getStringArray(R.array.campo2); break;
                case 3: compromisos = getResources().getStringArray(R.array.campo3); break;
                default: compromisos = getResources().getStringArray(R.array.campo1); break;
            }
        }
        public Fragment getItem (int position) {
            CompromisoFragment comp = new CompromisoFragment(superCtx, action, position);
            return comp;
        }

        @Override
        public int getCount() {
            return compromisos.length;
        }
    }
}
