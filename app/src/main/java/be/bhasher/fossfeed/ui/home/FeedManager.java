package be.bhasher.fossfeed.ui.home;

import android.os.AsyncTask;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import be.bhasher.fossfeed.R;
import be.bhasher.fossfeed.utils.cache.AppDatabase;

public class FeedManager {
    public static class UpdateDisplayedFeedItems extends AsyncTask<Void, Void, List<FeedItem>> {
        private final View view;
        private final SwipeRefreshLayout swipeRefreshLayout;

        public UpdateDisplayedFeedItems(View view, SwipeRefreshLayout swipeRefreshLayout){
            this.view = view;
            this.swipeRefreshLayout = swipeRefreshLayout;
        }

        @Override
        protected List<FeedItem> doInBackground(Void... voids) {
            return AppDatabase.itemsDAO.getAllByTime(10);
        }

        @Override
        protected void onPostExecute(List<FeedItem> feedItems){
            System.out.println(feedItems.size() + " items found !");
            feedItems.sort((o1, o2) -> (int) ((o2.getCalendarDate().getTimeInMillis() - o1.getCalendarDate().getTimeInMillis())/1000));

            final RecyclerView recyclerFeeds = view.findViewById(R.id.recyclerFeeds);
            FeedAdapter feedAdapter = new FeedAdapter((ArrayList<FeedItem>) feedItems);
            recyclerFeeds.setAdapter(feedAdapter);
            if(this.swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
        }
    }
}
