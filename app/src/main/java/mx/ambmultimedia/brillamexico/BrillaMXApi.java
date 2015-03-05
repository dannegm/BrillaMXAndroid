package mx.ambmultimedia.brillamexico;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class BrillaMXApi {

    private static String hostname = "http://api.brillamexico.org";
    private static AsyncHttpClient client = new AsyncHttpClient();

    // Usuario
    public static class user {
        private static JSONObject userObj;

        // Regresa usuario
        public static JSONObject find (String fbID) {
            client.get(hostname + "/user/" + fbID, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    userObj = response;
                }
            });
            return userObj;
        }

        private static JSONObject selfieObj;
        // Regresa selfie
        public static JSONObject selfie (String selfieID) {
            client.get(hostname + "/user/selfie/" + selfieID, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    selfieObj = response;
                }
            });
            return selfieObj;
        }

        // Postea selfie
        public static JSONObject postSelfie (String fbID, RequestParams params) {
            client.post(hostname + "/user/selfie/" + fbID, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    selfieObj = response;
                }
            });
            return selfieObj;
        }

        private static JSONArray selfiesObj;
        // Regresa todas las selfies de un usuario
        public static JSONArray selfies (String fbID) {
            client.get(hostname + "/user/selfies/" + fbID, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    selfiesObj = response;
                }
            });
            return selfiesObj;
        }

        // Registra un usuario
        public static JSONObject register (RequestParams params) {
            client.post(hostname + "/user/register", params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    try {
                        userObj = response.getJSONObject(0);
                    } catch (JSONException e) {
                    }
                }
            });
            return userObj;
        }

        private static int _points;
        // Añade puntos a un usuario
        public static int addPonts (String fbID, int points) {
            RequestParams params = new RequestParams();
            params.put("points", points);
            client.post(hostname + "/user/points/" + fbID, params, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    try {
                        userObj = response.getJSONObject(0);
                        _points = userObj.getInt("points");
                    } catch (JSONException e) {}
                }
            });
            return _points;
        }
    }

    // Usuarios
    public static class users {
        private static JSONArray usersObj;
        // LIsta todos los usuarios
        public static JSONArray list () {
            client.get(hostname + "/users", null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    usersObj = response;
                }
            });
            return usersObj;
        }

        private static JSONArray leaderboardsObj;
        // Regresa el leaderboard
        public static JSONArray leaderboard (String fieldaction) {
            client.get(hostname + "/users/leaderboard/" + fieldaction, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    leaderboardsObj = response;
                }
            });
            return leaderboardsObj;
        }

        private static JSONArray selfiesObj;
        // Regresa todas las selfies
        public static JSONArray selfie (String fieldaction) {
            client.get(hostname + "/users/selfie/" + fieldaction, null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    selfiesObj = response;
                }
            });
            return selfiesObj;
        }
    }

}
