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

import be.bhasher.fossfeed.MainActivity;
import be.bhasher.fossfeed.R;
import be.bhasher.fossfeed.utils.cache.AppDatabase;

public class RssParser extends AsyncTask<Void, Void, Boolean> {

    private final FeedChannel feedChannel = new FeedChannel();
    private final View view;
    private final SwipeRefreshLayout swipeRefreshLayout;

    public RssParser(View view){
        this(view, null);
    }

    public RssParser(View view, SwipeRefreshLayout swipeRefreshLayout){
        this.view = view;
        this.swipeRefreshLayout = swipeRefreshLayout;
    }

    private void parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xmlPullParser = factory.newPullParser();
        Reader reader = new InputStreamReader(inputStream);
        xmlPullParser.setInput(reader);

        FeedItem current = new FeedItem(feedChannel);

        boolean inItem = false;
        boolean inChannel = false;

        int eventType = xmlPullParser.getEventType();

        while(eventType != XmlPullParser.END_DOCUMENT){
            if(eventType == XmlPullParser.START_TAG){
                if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_CHANNEL)) inChannel = true;

                else if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_ITEM)){
                    inItem = true;
                }

                else if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_ITEM_TITLE)){
                    if(inItem) current.title = xmlPullParser.nextText().trim();
                    else if(inChannel) feedChannel.title = xmlPullParser.nextText().trim();

                }else if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_ITEM_LINK)){
                    if(inItem) current.link = xmlPullParser.nextText().trim();
                    else if(inChannel) feedChannel.link = xmlPullParser.nextText().trim();

                }else if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_ITEM_AUTHOR)){
                    if(inItem) current.author = xmlPullParser.nextText().trim();

                }else if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_ITEM_CATEGORY)){
                    if(inItem) current.categories.add(xmlPullParser.nextText().trim());

                }else if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_ITEM_THUMBNAIL)
                || xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_ITEM_MEDIA_CONTENT)
                        || xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_ITEM_ENCLOSURE) //TODO Can be a video/audio
                        || xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_ITEM_IMAGE_NEWS)){
                    if(inItem) current.imageUrl = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, RSSKeywords.RSS_ITEM_URL);

                }else if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_ITEM_DESCRIPTION)){
                    if(inItem) current.description = xmlPullParser.nextText().trim();
                    else if(inChannel) feedChannel.description = xmlPullParser.nextText().trim();

                }else if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_ITEM_PUB_DATE)
                || xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_ITEM_TIME)){
                    if(inItem) current.date = xmlPullParser.nextText().trim();

                }else if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_ITEM_GUID)){
                    if(inItem) current.guid = xmlPullParser.nextText().trim();

                }else if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_CHANNEL_LAST_BUILD_DATE)){
                    if(inChannel) feedChannel.lastBuild = xmlPullParser.nextText().trim();
                }

            }else if(eventType == XmlPullParser.END_TAG){
                 if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_ITEM)){
                     inItem = false;
                     feedChannel.add(current);
                     current = new FeedItem(feedChannel);
                 }else if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_CHANNEL)){
                     inChannel = false;
                 }
            }
            eventType = xmlPullParser.next();
        }
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean success = true;
        for(Feed feed: AppDatabase.getInstance().getAll()){
            try {
                URL url = new URL(feed.url);
                InputStream inputStream = url.openConnection().getInputStream();
                parseFeed(inputStream);
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
            final RecyclerView recyclerFeeds = view.findViewById(R.id.recyclerFeeds);
            FeedAdapter feedAdapter = new FeedAdapter(feedChannel);
            recyclerFeeds.setAdapter(feedAdapter);
        }else{
            Toast.makeText(MainActivity.context, "Error while scrapping", Toast.LENGTH_LONG).show();
        }
        if(swipeRefreshLayout != null) swipeRefreshLayout.setRefreshing(false);
    }
}
