package mx.ambmultimedia.brillamexico;

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

import com.cuneytayyildiz.widget.PullRefreshLayout;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


@SuppressLint("ValidFragment")
public class LeaderBoard extends Fragment {
    Context ctx;
    Config config;

    PullRefreshLayout refreshLayout;
    String CampoDeAccion;

    public LeaderBoard (Context _ctx) {
        ctx = _ctx;
    }

    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout;
        layout = inflater.inflate(R.layout.activity_leader_board, container, false);
        config = new Config(ctx);

        GeneralEvents(layout);
        GetLeaderBoard(layout);

        refreshLayout = (PullRefreshLayout) layout.findViewById(R.id.swipeRefreshLayout);
        refreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetLeaderBoard(layout);
            }
        });
        return layout;
    }

    public void GetLeaderBoard (View view) {
        final View _view = view;
        try {
            JSONObject selfuser = new JSONObject(config.get("user", "null"));
            CampoDeAccion = selfuser.getString("fieldaction_id");
        } catch (JSONException e) {}

        AsyncHttpClient client = new AsyncHttpClient();
        String hostname = ctx.getString(R.string.hostname);
        client.get(hostname + "/users/leaderboard/" + CampoDeAccion, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    final JSONArray users = response;

                    ListLeaderBoard adapter = new ListLeaderBoard(ctx, users);
                    ExtendableListView listUsers = (ExtendableListView) _view.findViewById(R.id.lisLeaderBoard);

                    listUsers.setAdapter(adapter);
                    listUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            try {
                                JSONObject user = users.getJSONObject(position);
                                String userID = user.getString("fbid");

                                Intent intent = new Intent(ctx, UserViewer.class);
                                intent.putExtra("userID", userID);
                                startActivity(intent);
                            } catch (JSONException e) {
                            }
                        }
                    });

                    refreshLayout.setRefreshing(false);

                } catch (Exception e) {
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String response, Throwable e) {
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
