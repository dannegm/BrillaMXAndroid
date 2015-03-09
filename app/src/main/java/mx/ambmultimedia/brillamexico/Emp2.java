package mx.ambmultimedia.brillamexico;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LikeView;


public class Emp2 extends ActionBarActivity {
    Context ctx;

    private UiLifecycleHelper uiHelper;
    private LikeView like_view;
    private String like_url = "http://www.brillamexico.org/jahir-mojica-hernandez/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emp2);
        ctx = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**
         * Facebook like
         */

        uiHelper = new UiLifecycleHelper(this, mStatusCallback);

        like_view = (LikeView) findViewById(R.id.likeEmp1);
        like_view.setObjectId(like_url);
        like_view.setLikeViewStyle(LikeView.Style.STANDARD);
        like_view.setAuxiliaryViewPosition(LikeView.AuxiliaryViewPosition.INLINE);
        like_view.setHorizontalAlignment(LikeView.HorizontalAlignment.LEFT);
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

    Session.StatusCallback mStatusCallback = new Session.StatusCallback (){
        @Override
        public void call(Session session, SessionState state, Exception exception) {

        }
    };

    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, null);
    }
}