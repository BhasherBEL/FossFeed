package be.bhasher.fossfeed.ui.home.db;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import be.bhasher.fossfeed.ui.home.Feed;
import be.bhasher.fossfeed.ui.home.FeedItem;
import be.bhasher.fossfeed.ui.home.parser.RSSParser;
import be.bhasher.fossfeed.utils.cache.AppDatabase;

public class ItemsDB{
    public static class UpdateItems extends AsyncTask<Feed, Void, Void> {

        @Override
        protected Void doInBackground(Feed... feeds) {
            for(Feed feed: feeds){
                try {
                    URL url = new URL(feed.url);
                    InputStream inputStream = url.openConnection().getInputStream();

                    for(FeedItem item: RSSParser.parseFeed(inputStream)){
                        if(!AppDatabase.itemsDAO.has(item.timestamp, item.title/*, item.feedChannel.title*/))
                            AppDatabase.itemsDAO.insert(item);
                    }
                } catch (IOException | XmlPullParserException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

    }

}