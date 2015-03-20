package mx.ambmultimedia.brillamexico.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.ambmultimedia.brillamexico.R;
import mx.ambmultimedia.brillamexico.utils.Config;


public class Logro extends ActionBarActivity {
    Context ctx;
    Config config;
    String fbID;

    int pointsLogro = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logro);
        ctx = this;
        config = new Config(ctx);
        fbID = config.get("fbID", "0");

        Bundle bundle = getIntent().getExtras();
        String logroID = bundle.getString("LogroID");
        final String Reference = bundle.getString("Reference");

        ImageView icon = (ImageView) findViewById(R.id.iconLogro);
        String LogroName = "";
        switch (logroID) {
            case "1":
                LogroName = "Nuevo Usuario";
                pointsLogro = 50;
                icon.setImageResource(R.drawable.l_newuser); break;
            case "2":
                LogroName = "Perfil Completo";
                pointsLogro = 50;
                icon.setImageResource(R.drawable.l_perfilcompleto);
                break;
            case "3":
                LogroName = "Don Compromisos";
                pointsLogro = 50;
                icon.setImageResource(R.drawable.l_doncompromisos);
                break;
            case "4":
                LogroName = "Super Comprometido";
                pointsLogro = 100;
                icon.setImageResource(R.drawable.l_supercomprometido);
                break;
            case "5":
                LogroName = "Viralizador";
                pointsLogro = 100;
                icon.setImageResource(R.drawable.l_viralizador);
                break;
            case "6":
                LogroName = "Twitter Lover";
                pointsLogro = 50;
                icon.setImageResource(R.drawable.l_twitterlover);
                break;
        }

        TextView name = (TextView) findViewById(R.id.nameLogro);
        String LogroNameText = getString(R.string.l_text_logro);
        LogroNameText = LogroNameText.replaceAll("__logroname__", LogroName);
        name.setText(Html.fromHtml(LogroNameText));

        TextView points = (TextView) findViewById(R.id.pointsLogro);
        points.setText(pointsLogro + " puntos");

        addLogro(logroID, Reference);

        Button toContinue = (Button) findViewById(R.id.toContinue);
        toContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueActivity(Reference);
            }
        });
    }

    private void continueActivity (String ref) {
        config.get("isReload", "true");
        Intent intent;
        switch (ref) {
            case "EditUser":
                intent = new Intent(Logro.this, UserProfile.class);
                break;
            case "Register":
                intent = new Intent(Logro.this, Preselfie.class);
                break;
            case "Selfie":
                Bundle bundle = getIntent().getExtras();
                String selfieID = bundle.getString("selfieID");
                intent = new Intent(Logro.this, Selfie.class);
                intent.putExtra("selfieID", selfieID);
                break;
            case "TwitterLinked":
                intent = new Intent(Logro.this, EditUserInfo.class);
                break;
            default:
                intent = new Intent(Logro.this, UserProfile.class);
                break;
        }
        startActivity(intent);
    }

    private void addPoints (int points) {
        final String hostname = getString(R.string.hostname);
        final AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("points", points);
        client.post(hostname + "/user/points/" + fbID, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable e) {
                Toast.makeText(ctx, "Fail to connect", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addLogro (String logroID, String ref) {
        final String hostname = getString(R.string.hostname);
        final AsyncHttpClient client = new AsyncHttpClient();
        final String Reference = ref;

        RequestParams params = new RequestParams();
        params.put("logro", logroID);
        client.post(hostname + "/user/logro/" + fbID, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String status = response.getString("status");
                    if (status.toString() == "success") {
                        addPoints(pointsLogro);
                    } else {
                        Toast.makeText(ctx, "Ya tenías este logro", Toast.LENGTH_SHORT).show();
                        //continueActivity(Reference);
                    }
                } catch (JSONException e) {
                    Toast.makeText(ctx, "Ya tenías este logro", Toast.LENGTH_SHORT).show();
                    //continueActivity(Reference);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable e) {
                Toast.makeText(ctx, "Fail to connect", Toast.LENGTH_LONG).show();
            }
        });
    }
}
