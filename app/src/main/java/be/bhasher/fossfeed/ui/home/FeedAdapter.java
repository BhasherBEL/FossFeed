package be.bhasher.fossfeed.ui.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import be.bhasher.fossfeed.FeedItemActivity;
import be.bhasher.fossfeed.R;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final FeedChannel feedChannel;

    public FeedAdapter(FeedChannel feedChannel){
        this.feedChannel = feedChannel;
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
        FeedItem feedItem = this.feedChannel.get(position);
        viewHolderFeed.title.setText(feedItem.title);
        viewHolderFeed.subtitle.setText(feedItem.getSubtitle());
        if(feedItem.imageUrl != null) new DownloadImageTask(viewHolderFeed.image).execute(feedItem.imageUrl);
    }

    //TODO fix image bug
    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder){
        ViewHolderFeed viewHolderFeed = (ViewHolderFeed) holder;
        viewHolderFeed.image.setImageDrawable(null);
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
        return this.feedChannel.size();
    }

    class ViewHolderFeed extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView image;
        private final TextView title;
        private final TextView subtitle;

        public ViewHolderFeed(View view) {
            super(view);
            this.image = view.findViewById(R.id.image);
            this.title = view.findViewById(R.id.title);
            this.subtitle = view.findViewById(R.id.subtitle);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Intent intent = new Intent(view.getContext(), FeedItemActivity.class);
            intent.putExtra("FeedItem", feedChannel.get(position));
            view.getContext().startActivity(intent);
        }
    }
}
