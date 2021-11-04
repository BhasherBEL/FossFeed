package be.bhasher.fossfeed.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public abstract class DownloadImage extends AsyncTask<String, Void, Bitmap> {
    @Override
    public abstract void onPostExecute(Bitmap bitmap);

    @Override
    protected Bitmap doInBackground(String... strings) {
        return getImageBitmap(strings[0]);
    }

    private static Bitmap getImageBitmap(String url) {
        Bitmap bm = null;
        try {
            URL aURL = new URL(url);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();
        } catch (IOException e) {
            Log.e("FeedItem.getImageBitmap", "Error getting bitmap", e);
        }
        return bm;
    }
}
