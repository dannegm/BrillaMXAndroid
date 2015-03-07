package mx.ambmultimedia.brillamexico;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListLeaderBoard extends BaseAdapter {
    private Context mContext;
    private final JSONArray users;

    public ListLeaderBoard (Context c, JSONArray selfies) {
        mContext = c;
        this.users = selfies;
    }

    @Override
    public int getCount() {
        return users.length();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View list = null;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            try {
                list = new View(mContext);
                list = inflater.inflate(R.layout.adapter_item_leaderboard, null);
                final View listTmp = list;

                JSONObject userObj = users.getJSONObject(position);
                String username = userObj.getString("name");
                String points = userObj.getString("points");

                TextView tUsername = (TextView) list.findViewById(R.id.userName);
                TextView tPoints = (TextView) list.findViewById(R.id.userPoints);
                TextView tPosition = (TextView) list.findViewById(R.id.userPosition);

                tUsername.setText(username);
                tPoints.setText(points + " puntos");
                tPosition.setText("#" + String.valueOf(position + 1));

                LinearLayout container = (LinearLayout) list.findViewById(R.id.listContenedor);
                if (position == 0) {
                    container.setBackgroundColor(
                            list.getResources().getColor(R.color.bmx_green_1)
                    );
                } else if (position == 1) {
                    container.setBackgroundColor(
                            list.getResources().getColor(R.color.bmx_green_2)
                    );
                } else if (position == 2) {
                    container.setBackgroundColor(
                            list.getResources().getColor(R.color.bmx_green_3)
                    );
                }

                String avatarUrl = "https://graph.facebook.com/__fbid__/picture";
                avatarUrl = avatarUrl.replaceAll("__fbid__", userObj.getString("fbid"));
                CircleImageView imageView = (CircleImageView) listTmp.findViewById(R.id.luserAvatar);
                Picasso.with(mContext)
                        .load(avatarUrl)
                        .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                        .error(R.drawable.com_facebook_profile_picture_blank_square)
                        .into(imageView);

            } catch (JSONException e) {}

        } else {
            list = convertView;
        }
        return list;
    }
}
