package mx.ambmultimedia.brillamexico;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewParent;

import java.util.HashMap;
import java.util.List;
import java.util.Vector;


public class Compromisos extends FragmentActivity {
    Context ctx;
    Config config;

    CompromisosAdapter adapter;

    int[][][] CamposAccion = {
        {},
        {
            {1, R.drawable.ic_1_1, R.string.c_1_1},
            {1, R.drawable.ic_1_2, R.string.c_1_2},
            {1, R.drawable.ic_1_3, R.string.c_1_3},
            {1, R.drawable.ic_1_4, R.string.c_1_4},
            {1, R.drawable.ic_1_5, R.string.c_1_5},
            {1, R.drawable.ic_1_6, R.string.c_1_6},
            {1, R.drawable.ic_1_7, R.string.c_1_7},
            {1, R.drawable.ic_1_8, R.string.c_1_8},
            {1, R.drawable.ic_1_9, R.string.c_1_9},
            {1, R.drawable.ic_1_10, R.string.c_1_10},
            {1, R.drawable.ic_1_11, R.string.c_1_11},
            {1, R.drawable.ic_1_12, R.string.c_1_12},
            {1, R.drawable.ic_1_13, R.string.c_1_13},
            {1, R.drawable.ic_1_14, R.string.c_1_14}
        },
        {
            {2, R.drawable.ic_2_15, R.string.c_2_15},
            {2, R.drawable.ic_2_16, R.string.c_2_16},
            {2, R.drawable.ic_2_17, R.string.c_2_17},
            {2, R.drawable.ic_2_18, R.string.c_2_18}
        },
        {
            {3, R.drawable.ic_3_19, R.string.c_3_19},
            {3, R.drawable.ic_3_20, R.string.c_3_20},
            {3, R.drawable.ic_3_21, R.string.c_3_21},
            {3, R.drawable.ic_3_22, R.string.c_3_22},
            {3, R.drawable.ic_3_23, R.string.c_3_23},
            {3, R.drawable.ic_3_24, R.string.c_3_24},
            {3, R.drawable.ic_3_25, R.string.c_3_25},
            {3, R.drawable.ic_3_26, R.string.c_3_26},
            {3, R.drawable.ic_3_27, R.string.c_3_27}
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compromisos);
        ctx = this;
        config = new Config(ctx);

        /*

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        */

        BuildPaginator();
    }

    public void BuildPaginator () {
        List<Fragment> pages = new Vector<>();
        String CampoDeAccion = config.get("CampoDeAccion", "1");

        pages.add(Fragment.instantiate(this, CompromisoPag.class.getName()));

        adapter = new CompromisosAdapter(this.getSupportFragmentManager(), pages);

        ViewPager pager = (ViewPager) findViewById(R.id.compromisosPaginator);
        pager.setAdapter(adapter);
    }
}
