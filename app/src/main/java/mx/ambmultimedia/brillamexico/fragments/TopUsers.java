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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cuneytayyildiz.widget.PullRefreshLayout;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.ambmultimedia.brillamexico.R;
import mx.ambmultimedia.brillamexico.activities.UserViewer;
import mx.ambmultimedia.brillamexico.activities.Compromisos;
import mx.ambmultimedia.brillamexico.adapters.ListTopUsers;
import mx.ambmultimedia.brillamexico.customViews.ExtendableGridView;
import mx.ambmultimedia.brillamexico.utils.Config;


@SuppressLint("ValidFragment")
public class TopUsers extends Fragment {
    Context ctx;
    Config config;

    PullRefreshLayout refreshLayout;

    public TopUsers (Context _ctx) {
        ctx = _ctx;
    }
    public View onCreateView (LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View layout;
        layout = inflater.inflate(R.layout.fragment_top_users, container, false);
        config = new Config(ctx);

        GetLeaderBoard(layout);
        GeneralEvents(layout);

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

        AsyncHttpClient client = new AsyncHttpClient();
        String hostname = ctx.getString(R.string.hostname);
        client.get(hostname + "/users/leaders", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                try {
                    final JSONArray _users = response;
                    final JSONObject firstUser = _users.getJSONObject(0);
                    final JSONArray users = RemoveJSONArray(_users, 0);

                    // ============

                    String avatarUrl = ctx.getString(R.string.fb_avatar_link);
                    avatarUrl = avatarUrl.replaceAll("__fbid__", firstUser.getString("fbid"));
                    CircleImageView userAvatar = (CircleImageView) _view.findViewById(R.id.faUserAvatar);
                    Picasso.with(ctx)
                            .load(avatarUrl)
                            .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                            .error(R.drawable.com_facebook_profile_picture_blank_square)
                            .into(userAvatar);

                    TextView userName = (TextView) _view.findViewById(R.id.faUserName);
                    userName.setText(firstUser.getString("name"));

                    TextView userPoints = (TextView) _view.findViewById(R.id.faUserPoints);
                    userPoints.setText(firstUser.getString("points") + " puntos");

                    TextView userPosition = (TextView) _view.findViewById(R.id.faUserPosition);
                    userPosition.setText("1");

                    final String userID = firstUser.getString("fbid");
                    RelativeLayout toFirstUser = (RelativeLayout) _view.findViewById(R.id.toFirstUser);
                    toFirstUser.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(ctx, UserViewer.class);
                            intent.putExtra("userID", userID);
                            startActivity(intent);
                        }
                    });

                    // ============

                    ListTopUsers adapter = new ListTopUsers(ctx, users);
                    ExtendableGridView listUsers = (ExtendableGridView) _view.findViewById(R.id.usersGrid);

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
                String msg = "[" + statusCode + "|us/leaders] " + e.getMessage();
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

    public static JSONArray RemoveJSONArray (JSONArray jarray, int pos) {
        JSONArray Njarray=new JSONArray();
        try {
            for (int i=0;i<jarray.length();i++){
                if (i!=pos) {
                    Njarray.put(jarray.get(i));
                }
            }
        } catch (Exception e) {e.printStackTrace();}
        return Njarray;
    }

}
