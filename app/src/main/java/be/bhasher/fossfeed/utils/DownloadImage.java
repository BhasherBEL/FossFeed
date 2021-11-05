package be.bhasher.fossfeed.utils;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.IOException;

public abstract class DownloadImage extends AsyncTask<String, Void, Bitmap> {
    @Override
    public abstract void onPostExecute(Bitmap bitmap);

    @Override
    protected Bitmap doInBackground(String... strings) {
        try {
            return Picasso.get().load(strings[0]).get();
        } catch (IOException e) {
            Log.e("DownloadImage", "Error getting bitmap: " + e.getMessage(), e);
        }
        return null;
    }
}
