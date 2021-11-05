package be.bhasher.fossfeed.ui.home;

import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;

import be.bhasher.fossfeed.MainActivity;
import be.bhasher.fossfeed.R;
import be.bhasher.fossfeed.utils.cache.AppDatabase;
/*
public class RssParser extends AsyncTask<Void, Void, Boolean> {
    private final View view;
    private final SwipeRefreshLayout swipeRefreshLayout;

    public RssParser(View view){
        this(view, null);
    }

    public RssParser(View view, SwipeRefreshLayout swipeRefreshLayout){
        this.view = view;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean success = true;

        for(Feed feed: AppDatabase.feedsDAO.getAll()){
            try {
                URL url = new URL(feed.url);
                InputStream inputStream = url.openConnection().getInputStream();
                be.bhasher.fossfeed.ui.home.parser.RSSParser.parseFeed(inputStream);
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
                success = false;
            }
        }
        return success;
    }

    @Override
    protected void onPostExecute(Boolean success){
        if(success){
            feedItems.sort((o1, o2) -> (int) ((o2.getCalendarDate().getTimeInMillis() - o1.getCalendarDate().getTimeInMillis())/1000));

            final RecyclerView recyclerFeeds = view.findViewById(R.id.recyclerFeeds);
            FeedAdapter feedAdapter = new FeedAdapter(feedItems);
            recyclerFeeds.setAdapter(feedAdapter);
        }else{
            Toast.makeText(MainActivity.context, "Error while scrapping", Toast.LENGTH_LONG).show();
        }
        if(swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
    }
}
*/