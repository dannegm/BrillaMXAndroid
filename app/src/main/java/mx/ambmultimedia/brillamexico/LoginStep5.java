package mx.ambmultimedia.brillamexico;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class LoginStep5 extends FragmentActivity {
    Context ctx;
    Config config;

    private String Nombre;
    private int CampoDeAccion;
    private Boolean isReturn;
    Boolean isLogin;

    private UiLifecycleHelper uiHelper;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState sessionState, Exception e) {
            onSessionChange (session, sessionState, e);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_step5);
        ctx = this;
        config = new Config(ctx);
        isLogin = Boolean.valueOf(config.get("isLogin", "false"));

        /***
         * Obteniendo datos del activity anterior
         */

        Bundle bundle = getIntent().getExtras();
        CampoDeAccion = bundle.getInt("CampoDeAccion");
        Nombre = bundle.getString("Nombre");

        isReturn = Boolean.valueOf(bundle.getString("ReturnUser"));

        String pHiText = getString(R.string.l_text_11);
        pHiText = pHiText.replaceAll("__username__", Nombre);

        TextView hiText = (TextView) findViewById(R.id.hiText);


        if (isReturn) {
            hiText.setText("¡Hola de nuevo!, por favor vuelve a iniciar sesión");
        } else {
            hiText.setText(Html.fromHtml(pHiText));
        }

        /***
         * Creando Login de facebook
         */

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        LoginButton loginBtn = (LoginButton) findViewById(R.id.authButton);
        loginBtn.setPublishPermissions(Arrays.asList("email", "public_profile", "publish_actions"));

        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "mx.ambmultimedia.brillamexico",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        Session session = Session.getActiveSession();
        if (session != null && (session.isClosed() || session.isOpened())) {
            onSessionChange(session, session.getState(), null);
        }

        uiHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        uiHelper.onSaveInstanceState(bundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    // Facebook Methods

    public void onSessionChange (Session session, SessionState sessionState, Exception e) {
        if (session != null && session.isOpened() && !isLogin) {
            Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (user != null) {
                        final String fbID = user.getId();
                        if (!isReturn) {
                            String email = user.getProperty("email").toString();

                            if (Nombre.isEmpty()) {
                                Nombre = user.getFirstName();
                            }

                            RequestParams nuevoUsuario = new RequestParams();
                                nuevoUsuario.put("fbid", fbID);
                                nuevoUsuario.put("twid", "");
                                nuevoUsuario.put("name", Nombre);
                                nuevoUsuario.put("email", email);
                                nuevoUsuario.put("fieldaction_id", CampoDeAccion);
                                nuevoUsuario.put("gender", user.getProperty("gender").toString());
                                nuevoUsuario.put("age", "");

                            final String hostname = getString(R.string.hostname);
                            final AsyncHttpClient client = new AsyncHttpClient();
                            client.post(hostname + "/user/register", nuevoUsuario, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                                    config.set("isLogin", "true");
                                    config.set("fbID", fbID);
                                    config.set("isReload", "true");

                                    try {
                                        JSONObject user = response.getJSONObject(0);
                                        config.set("user", user.toString());
                                    } catch (JSONException e) {}

                                    Intent intent = new Intent(LoginStep5.this, Logro.class);
                                    intent.putExtra("Reference", "Register");
                                    intent.putExtra("LogroID", "1");
                                    startActivity(intent);
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, String response, Throwable e) {
                                    Toast.makeText(ctx, "¡Ups! Something was wrong :(", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            config.set("isLogin", "true");
                            config.set("fbID", fbID);
                            config.set("isReload", "true");

                            Intent intent = new Intent(LoginStep5.this, UserProfile.class);
                            startActivity(intent);
                        }
                    }
                }
            }).executeAsync();
        } else {
            config.set("isLogin", "false");
            config.set("fbID", "");
        }
    }
}
