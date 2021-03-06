package mx.ambmultimedia.brillamexico.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.ambmultimedia.brillamexico.R;
import mx.ambmultimedia.brillamexico.activities.Compromisos;
import mx.ambmultimedia.brillamexico.activities.Selfie;
import mx.ambmultimedia.brillamexico.adapters.GridSelfies;
import mx.ambmultimedia.brillamexico.customViews.ExtendableGridView;
import mx.ambmultimedia.brillamexico.utils.Config;


@SuppressLint("ValidFragment")
public class Selfies extends Fragment {
    Context ctx;
    Config config;

    public Selfies (Context _ctx) {
        ctx = _ctx;
    }

    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout;
        layout = inflater.inflate(R.layout.fragment_selfies, container, false);
        config = new Config(ctx);

        GetSelfies(layout);
        GeneralEvents(layout);
        return layout;
    }

    public void GetSelfies (View view) {
        final View _view = view;
        AsyncHttpClient client = new AsyncHttpClient();
        String hostname = getString(R.string.hostname);
        client.get(hostname + "/users/selfie", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    final JSONArray selfies = response;

                    GridSelfies adapter = new GridSelfies(ctx, selfies);
                    ExtendableGridView gridSelfies = (ExtendableGridView) _view.findViewById(R.id.usersGrid);

                    gridSelfies.setAdapter(adapter);
                    gridSelfies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                JSONObject selfie = selfies.getJSONObject(position);
                                String selfieID = selfie.getString("id");

                                Intent intent = new Intent(ctx, Selfie.class);
                                intent.putExtra("selfieID", selfieID);
                                startActivity(intent);
                            } catch (JSONException e) {
                            }
                        }
                    });
                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable e) {
                super.onFailure(statusCode, headers, responseString, e);
                String msg = "[" + statusCode + "|us/selfie] " + e.getMessage();
                Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void GeneralEvents (View view) {
        FloatingActionButton toSelfie = (FloatingActionButton) view.findViewById(R.id.toSelfie);
        toSelfie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, Compromisos.class);
                startActivity(intent);
            }
        });
    }
}
