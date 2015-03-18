package mx.ambmultimedia.brillamexico;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ListNoticias extends BaseAdapter {
    private Context mContext;
    private final JSONArray noticias;

    public ListNoticias (Context c, JSONArray noticias) {
        mContext = c;
        this.noticias = noticias;
    }

    @Override
    public int getCount() {
        return noticias.length();
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
                list = inflater.inflate(R.layout.adapter_noticia_card, null);
                final View listTmp = list;

                JSONObject noticiaObj = noticias.getJSONObject(position);

                TextView title = (TextView) list.findViewById(R.id.nTitle);
                TextView date = (TextView) list.findViewById(R.id.nDate);
                TextView content = (TextView) list.findViewById(R.id.nContent);

                title.setText( noticiaObj.getString("title") );
                date.setText( noticiaObj.getString("date") );
                content.setText( noticiaObj.getString("content") );

                String coverUrl = noticiaObj.getString("imagen");
                ImageView imageView = (ImageView) listTmp.findViewById(R.id.nCover);
                Picasso.with(mContext)
                        .load(coverUrl)
                        .placeholder(R.drawable.img_placeholder)
                        .error(R.drawable.paisaje_error)
                        .into(imageView);

                final String _title = noticiaObj.getString("title");
                final String _link = noticiaObj.getString("title");
                TextView nCompartir = (TextView) list.findViewById(R.id.nCompartir);
                nCompartir.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");

                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, _title);
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, _link);

                        mContext.startActivity(Intent.createChooser(sharingIntent, "Compartir"));
                    }
                });
            } catch (JSONException e) {}

        } else {
            list = convertView;
        }
        return list;
    }
}
