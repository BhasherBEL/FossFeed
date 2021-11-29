package be.bhasher.fossfeed.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import be.bhasher.fossfeed.FeedItemActivity;
import be.bhasher.fossfeed.R;
import be.bhasher.fossfeed.utils.DownloadImageView;

public class FeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final ArrayList<FeedItem> feedItems;

    public FeedAdapter(ArrayList<FeedItem> feedItems){
        this.feedItems = feedItems;
        //this.feedItems.removeIf(feedItem -> feedItem.read);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater view = LayoutInflater.from(parent.getContext());
        return new ViewHolderFeed(view.inflate(R.layout.card_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderFeed viewHolderFeed = (ViewHolderFeed) holder;
        FeedItem feedItem = this.feedItems.get(position);
        viewHolderFeed.title.setText(feedItem.title);
        if(feedItem.read) viewHolderFeed.title.setTextColor(0xFF555555);
        else viewHolderFeed.title.setTextColor(0xFFFFFFFF);
        viewHolderFeed.subtitle.setText(feedItem.getSubtitle());
        if(feedItem.read) viewHolderFeed.subtitle.setTextColor(0xFF555555);
        else viewHolderFeed.subtitle.setTextColor(0xFFFFFFFF);
        if(feedItem.imageUrl != null) Picasso.get().load(feedItem.imageUrl).into(viewHolderFeed.image);
        if(feedItem.read) viewHolderFeed.image.setImageAlpha(0x55);
        else viewHolderFeed.image.setImageAlpha(0xFF);
        System.out.println(position + " - " + feedItem.read + " - " + feedItem.title);
    }

    @Override
    public int getItemCount() {
        return this.feedItems.size();
    }

    public void notifyAsRead(int i){
        this.feedItems.get(i).read = true;
        this.notifyItemChanged(i);
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
            FeedItem.LAST_INDEX = position;
            Intent intent = new Intent(view.getContext(), FeedItemActivity.class);
            intent.putExtra("FeedItem", feedItems.get(position));
            view.getContext().startActivity(intent);
        }
    }
}
