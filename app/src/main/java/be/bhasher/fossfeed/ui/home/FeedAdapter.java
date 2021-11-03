package be.bhasher.fossfeed.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import be.bhasher.fossfeed.R;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final ArrayList<FeedItem> feedItems;

    public FeedAdapter(ArrayList<FeedItem> feedItems){
        this.feedItems = feedItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater view = LayoutInflater.from(parent.getContext());
        return new ViewHolderFeed(view.inflate(R.layout.card_feed, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderFeed viewHolderFeed = (ViewHolderFeed) holder;
        FeedItem feedItem = this.feedItems.get(position);
        viewHolderFeed.title.setText(feedItem.title);
        viewHolderFeed.subtitle.setText(feedItem.subtitle);
        if(feedItem.imageUrl != null) new DownloadImageTask(viewHolderFeed.image).execute(feedItem.imageUrl);
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap>{
        private final ImageView imageView;

        public DownloadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        @Override
        public void onPostExecute(Bitmap bitmap){
            imageView.setImageBitmap(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            return getImageBitmap(strings[0]);
        }
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
            Log.e("FeedAdapter.getImageBitmap", "Error getting bitmap", e);
        }
        return bm;
    }

    @Override
    public int getItemCount() {
        return this.feedItems.size();
    }

    static class ViewHolderFeed extends RecyclerView.ViewHolder /*implements View.OnClickListener*/ {
        private final ImageView image;
        private final TextView title;
        private final TextView subtitle;

        public ViewHolderFeed(View view) {
            super(view);
            this.image = view.findViewById(R.id.image);
            this.title = view.findViewById(R.id.title);
            this.subtitle = view.findViewById(R.id.subtitle);
        }
    }
}