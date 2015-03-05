package mx.ambmultimedia.brillamexico;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
                grid = inflater.inflate(R.layout.selfie_grid_single, null);
                final View gridTmp = grid;

                JSONObject selfieObj = selfiesObj.getJSONObject(position);
                String linkPicture = selfieObj.getString("picture");

                AsyncHttpClient client = new AsyncHttpClient();
                String hostname = "http://api.brillamexico.org";
                String urlPicture = hostname + "/pictures/" + linkPicture;
                String[] allowedContentTypes = new String[]{"image/png", "image/jpeg", "image/gif"};

                client.get(urlPicture, new BinaryHttpResponseHandler(allowedContentTypes) {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                        Bitmap selfieThumb = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);

                        ImageView imageView = (ImageView) gridTmp.findViewById(R.id.thumbPicture);
                        imageView.setImageBitmap(selfieThumb);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {
                    }
                });
            } catch (JSONException e) {}

        } else {
            grid = convertView;
        }
        return grid;
    }
}
