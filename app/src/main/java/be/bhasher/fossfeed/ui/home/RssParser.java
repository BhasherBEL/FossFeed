package be.bhasher.fossfeed.ui.home;

import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

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

public class RssParser extends AsyncTask<Void, Void, Boolean> {

    private final Feed feed = new Feed();
    private final View view;

    public RssParser(View view){
        this.view = view;
    }

    private void parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xmlPullParser = factory.newPullParser();
        Reader reader = new InputStreamReader(inputStream);
        xmlPullParser.setInput(reader);

        FeedItem current = new FeedItem(feed);

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
                    else if(inChannel) feed.title = xmlPullParser.nextText().trim();

                }else if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_ITEM_LINK)){
                    if(inItem) current.link = xmlPullParser.nextText().trim();
                    else if(inChannel) feed.link = xmlPullParser.nextText().trim();

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
                    else if(inChannel) feed.description = xmlPullParser.nextText().trim();

                }else if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_ITEM_PUB_DATE)
                || xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_ITEM_TIME)){
                    if(inItem) current.date = xmlPullParser.nextText().trim();

                }else if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_ITEM_GUID)){
                    if(inItem) current.guid = xmlPullParser.nextText().trim();

                }else if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_CHANNEL_LAST_BUILD_DATE)){
                    if(inChannel) feed.lastBuild = xmlPullParser.nextText().trim();
                }

            }else if(eventType == XmlPullParser.END_TAG){
                 if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_ITEM)){
                     inItem = false;
                     feed.add(current);
                     current = new FeedItem(feed);
                 }else if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_CHANNEL)){
                     inChannel = false;
                 }
            }
            eventType = xmlPullParser.next();
        }
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            URL url = new URL("https://www.nextinpact.com/rss-complet/254737/bcfad039771f7a519271d2d2ea905bd41c084e70b9e8fee3b4c0896ed64c3594");
            InputStream inputStream = url.openConnection().getInputStream();
            parseFeed(inputStream);
            return true;
        } catch (IOException | XmlPullParserException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean success){
        if(success){
            final RecyclerView recyclerFeeds = view.findViewById(R.id.recyclerFeeds);
            FeedAdapter feedAdapter = new FeedAdapter(feed);
            recyclerFeeds.setAdapter(feedAdapter);
        }else{
            Toast.makeText(MainActivity.context, "Error while scrapping", Toast.LENGTH_LONG).show();
        }
    }
}
