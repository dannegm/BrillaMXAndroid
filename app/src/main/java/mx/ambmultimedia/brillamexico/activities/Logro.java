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

/**
 * Clase para el Activity de los logros
 */
public class Logro extends ActionBarActivity {
    // Shalala, shalala... almacenamos información del activity
    Context ctx;
    Config config;
    String fbID;

    String hostname;
    AsyncHttpClient client;

    int pointsLogro = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logro);
        ctx = this;
        config = new Config(ctx);
        fbID = config.get("fbID", "0");

        hostname = getString(R.string.hostname);
        client = new AsyncHttpClient();

        // Obtenemos información del activity anterior, en este caso, la referencia y el ID del logro
        Bundle bundle = getIntent().getExtras();
        String logroID = bundle.getString("LogroID");
        final String Reference = bundle.getString("Reference");

        // Construimos vista del logro
        ImageView icon = (ImageView) findViewById(R.id.iconLogro);
        String LogroName = "";
        switch (logroID) {
            case "1":
                LogroName = "Nuevo Usuario";
                pointsLogro = 50;
                icon.setImageResource(R.drawable.l_newuser);
                break;
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

        // Agregamos logro al usuario
        addLogro(logroID);

        // Click para continuar
        Button toContinue = (Button) findViewById(R.id.toContinue);
        toContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                continueActivity(Reference);
            }
        });
    }

    /**
     * Método para continuar con la navegación
     * @param ref a dónde nos llevará el activity
     */
    private void continueActivity (String ref) {
        config.get("isReload", "true");
        Intent intent;
        switch (ref) {
            case "EditUser":
                // Si venimos de editar al usuario, lo llevamos al perfil
                intent = new Intent(Logro.this, UserProfile.class);
                break;
            case "Register":
                // Si venimos del registro, lo mandamos a la primera selfie
                intent = new Intent(Logro.this, Preselfie.class);
                break;
            case "Selfie":
                // Si venimos de subir una selfie, lo mandamos al landing de la selfie que se acaba de subir
                Bundle bundle = getIntent().getExtras();
                String selfieID = bundle.getString("selfieID");
                intent = new Intent(Logro.this, Selfie.class);
                intent.putExtra("selfieID", selfieID);
                break;
            case "TwitterLinked":
                // Si venimos de vincular twitter, lo mandamos al view de editar perfil
                intent = new Intent(Logro.this, EditUserInfo.class);
                break;
            default:
                // Si viene de algún otro lado, lo mandamos al perfil de usuario
                intent = new Intent(Logro.this, UserProfile.class);
                break;
        }
        startActivity(intent);
    }

    /**
     * Método para añadir puntos a los usuarios
     * @param points puntos que se le asignan
     */
    private void addPoints (int points) {
        // Construímos parámetros
        RequestParams params = new RequestParams();
        params.put("points", points);
        // Generamos petición
        client.post(hostname + "/user/points/" + fbID, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                // Si sale bien, mostramos mensaje
                Toast.makeText(ctx, "Puntos añadidos", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable e) {
                // Si algo sale mal, informamos
                String msg = "[" + statusCode + "|u/points] " + e.getMessage();
                Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Método para añadir logro
     * @param logroID ID del logro a añadir
     */
    private void addLogro (String logroID) {
        // COnstruímos parámetros POST
        RequestParams params = new RequestParams();
        params.put("logro", logroID);
        // Hacemos la petición
        client.post(hostname + "/user/logro/" + fbID, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Si va bien, probamos
                try {
                    // Obtenemos el status que devuelve
                    String status = response.getString("status");
                    // Si es "succes", añadimos los puntos
                    if (status == "success") {
                        addPoints(pointsLogro);
                    }
                // Si no se encuentra "status", significa que el logro ya existía. Avisamos
                } catch (JSONException e) {
                    Toast.makeText(ctx, "Ya tenías este logro", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable e) {
                // Si algo sale mal, informamos
                String msg = "[" + statusCode + "|u/logro] " + e.getMessage();
                Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
