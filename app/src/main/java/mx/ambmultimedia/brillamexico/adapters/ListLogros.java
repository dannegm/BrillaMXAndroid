package mx.ambmultimedia.brillamexico.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mx.ambmultimedia.brillamexico.R;

public class ListLogros extends BaseAdapter {
    private Context mContext;
    private final JSONArray logros;

    public ListLogros (Context c, JSONArray _logros) {
        mContext = c;
        logros = _logros;
    }

    @Override
    public int getCount() {
        return logros.length();
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
                list = inflater.inflate(R.layout.adapter_minilogro, null);

                JSONObject logro = logros.getJSONObject(position);

                /*
                TextView name = (TextView) list.findViewById(R.id.logroName);
                name.setText( logro.getString("name") );
                */

                TextView points = (TextView) list.findViewById(R.id.logroPoints);
                points.setText( logro.getString("points_for_achievement") + " puntos" );

                ImageView icon = (ImageView) list.findViewById(R.id.logroIcon);
                switch (logro.getString("id_string")) {
                    case "l_newuser": icon.setImageResource(R.drawable.l_newuser); break;
                    case "l_perfilcompleto": icon.setImageResource(R.drawable.l_perfilcompleto); break;
                    case "l_doncompromisos": icon.setImageResource(R.drawable.l_doncompromisos); break;
                    case "l_supercomprometido": icon.setImageResource(R.drawable.l_supercomprometido); break;
                    case "l_viralizador": icon.setImageResource(R.drawable.l_viralizador); break;
                    case "l_twitterlover": icon.setImageResource(R.drawable.l_twitterlover); break;
                }

            } catch (JSONException e) {}

        } else {
            list = convertView;
        }
        return list;
    }
}
