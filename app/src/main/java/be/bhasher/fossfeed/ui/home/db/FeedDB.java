package be.bhasher.fossfeed.ui.home.db;

import android.os.AsyncTask;

import java.util.List;

import be.bhasher.fossfeed.ui.home.Feed;
import be.bhasher.fossfeed.utils.cache.AppDatabase;

public class FeedDB {
    public static class UpdateAllFeedItems extends AsyncTask<Void, Void, List<Feed>> {

        @Override
        protected List<Feed> doInBackground(Void... voids) {
            return AppDatabase.feedsDAO.getAll();
        }

        @Override
        protected void onPostExecute(List<Feed> feeds){
            new ItemsDB.UpdateItems().execute(feeds.toArray(new Feed[0]));
        }
    }
}

