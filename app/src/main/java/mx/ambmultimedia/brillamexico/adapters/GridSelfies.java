package mx.ambmultimedia.brillamexico.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.ambmultimedia.brillamexico.R;

public class GridSelfies extends BaseAdapter {
    private Context mContext;
    private final JSONArray selfiesObj;

    public GridSelfies (Context c, JSONArray selfies) {
        mContext = c;
        this.selfiesObj = selfies;
    }

    @Override
    public int getCount() {
        return selfiesObj.length();
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
        View grid = null;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            try {
                grid = new View(mContext);
                grid = inflater.inflate(R.layout.adapter_selfie_grid_single, null);
                final View gridTmp = grid;

                JSONObject selfieObj = selfiesObj.getJSONObject(position);
                String linkPicture = selfieObj.getString("picture");

                String hostname = mContext.getString(R.string.hostname);
                String urlPicture = hostname + "/pictures/" + linkPicture;

                ImageView imageView = (ImageView) gridTmp.findViewById(R.id.thumbPicture);
                Picasso.with(mContext)
                        .load(urlPicture)
                        .placeholder(R.drawable.foto_placeholder)
                        .error(R.drawable.foto_error)
                        .into(imageView);
            } catch (JSONException e) {}

        } else {
            grid = convertView;
        }
        return grid;
    }
}
