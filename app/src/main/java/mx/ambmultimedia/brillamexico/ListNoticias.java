package mx.ambmultimedia.brillamexico;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;

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

                AsyncHttpClient client = new AsyncHttpClient();
                String coverUrl = noticiaObj.getString("imagen");
                String[] allowedContentTypes = new String[]{"image/png", "image/jpeg", "image/gif"};
                client.get(coverUrl, new BinaryHttpResponseHandler(allowedContentTypes) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                        Bitmap selfieThumb = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
                        ImageView imageView = (ImageView) listTmp.findViewById(R.id.nCover);
                        imageView.setImageBitmap(selfieThumb);
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
                    }
                });
            } catch (JSONException e) {}

        } else {
            list = convertView;
        }
        return list;
    }
}
