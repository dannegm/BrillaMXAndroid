package mx.ambmultimedia.brillamexico.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;
import mx.ambmultimedia.brillamexico.R;

public class ListTopUsers extends BaseAdapter {
    private Context mContext;
    private final JSONArray users;

    public ListTopUsers (Context c, JSONArray _users) {
        mContext = c;
        users = _users;
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
                list = inflater.inflate(R.layout.adapter_miniprofile, null);

                JSONObject user = users.getJSONObject(position);


                String avatarUrl = mContext.getString(R.string.fb_avatar_link);
                avatarUrl = avatarUrl.replaceAll("__fbid__", user.getString("fbid"));
                CircleImageView userAvatar = (CircleImageView) list.findViewById(R.id.faUserAvatar);
                Picasso.with(mContext)
                        .load(avatarUrl)
                        .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                        .error(R.drawable.com_facebook_profile_picture_blank_square)
                        .into(userAvatar);

                TextView userName = (TextView) list.findViewById(R.id.faUserName);
                userName.setText(user.getString("name"));

                TextView userPoints = (TextView) list.findViewById(R.id.aUserPoints);
                userPoints.setText(user.getString("points") + " puntos");

                TextView userPosition = (TextView) list.findViewById(R.id.aUserPosition);
                userPosition.setText(String.valueOf(position + 2));

            } catch (JSONException e) {}

        } else {
            list = convertView;
        }
        return list;
    }
}
