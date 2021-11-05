package be.bhasher.fossfeed.ui.managesubs;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import be.bhasher.fossfeed.R;
import be.bhasher.fossfeed.ui.home.Feed;
import be.bhasher.fossfeed.utils.cache.AppDatabase;

public class SubsAdapter extends RecyclerView.Adapter<SubsAdapter.ViewHolderFeed> {
    List<Feed> feeds;

    public SubsAdapter(List<Feed> feeds){
        this.feeds = feeds;
    }

    @NonNull
    @Override
    public SubsAdapter.ViewHolderFeed onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater view = LayoutInflater.from(parent.getContext());
        return new SubsAdapter.ViewHolderFeed(view.inflate(R.layout.card_feed, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubsAdapter.ViewHolderFeed holder, int position) {
        Feed feed = this.feeds.get(position);
        holder.title.setText(feed.title);
        holder.url.setText(feed.url);
        holder.delete.setOnClickListener(v -> {
            new RemoveSubscription(feed).execute();
            feeds.remove(position);
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return feeds.size();
    }

    class ViewHolderFeed extends RecyclerView.ViewHolder{
        private final TextView title;
        private final TextView url;
        private final Button delete;

        public ViewHolderFeed(@NonNull View view) {
            super(view);
            this.title = view.findViewById(R.id.cardfeed_title);
            this.url = view.findViewById(R.id.cardfeed_url);
            this.delete = view.findViewById(R.id.cardfeed_delete);
        }
    }

    class RemoveSubscription extends AsyncTask<String, Void, Boolean> {
        private final Feed feed;


        public RemoveSubscription(Feed feed){
            this.feed = feed;
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            AppDatabase.feedsDAO.delete(feed);
            return true;
        }
    }

    static public class Init extends AsyncTask<String, Void, List<Feed>>{
        private final View view;

        public Init(View view){
            this.view = view;
        }

        @Override
        protected List<Feed> doInBackground(String... strings) {
            return AppDatabase.feedsDAO.getAll();
        }

        @Override
        protected void onPostExecute(List<Feed> feeds){
            RecyclerView recyclerView = view.findViewById(R.id.managesubs_recycler);
            recyclerView.setAdapter(new SubsAdapter(feeds));
            System.out.println(feeds.size() + " feeds are in !");
        }
    }
}
