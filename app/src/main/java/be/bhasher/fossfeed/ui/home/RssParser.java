package be.bhasher.fossfeed.ui.home;

import android.os.AsyncTask;
import android.util.Xml;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import be.bhasher.fossfeed.MainActivity;
import be.bhasher.fossfeed.R;

public class RssParser extends AsyncTask<Void, Void, Boolean> {

    private final ArrayList<FeedItem> feedItems = new ArrayList<>();
    private final View view;

    public RssParser(View view){
        this.view = view;
    }

    private void parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        XmlPullParser xmlPullParser = Xml.newPullParser();
        xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        xmlPullParser.setInput(inputStream, null);

        boolean isItem = false;
        String title = null;
        String link = null;
        String description = null;
        String imageUrl = null;

        xmlPullParser.nextTag();
        while(xmlPullParser.next() != XmlPullParser.END_DOCUMENT){
            int eventType = xmlPullParser.getEventType();
            String name = xmlPullParser.getName();
            if(name == null) continue;
            if(eventType == XmlPullParser.END_TAG){
                if(name.equalsIgnoreCase("item")) isItem = false;
                continue;
            }else if(eventType == XmlPullParser.START_TAG && name.equalsIgnoreCase("item")){
                isItem = true;
                continue;
            }

            String text = "";


            if(xmlPullParser.next() == XmlPullParser.TEXT){
                text = xmlPullParser.getText();
                xmlPullParser.nextTag();
            }

            if(name.equalsIgnoreCase("title")) title = text;
            else if(name.equalsIgnoreCase("link")) link = text;
            else if(name.equalsIgnoreCase("description")) description = text;
            else if(name.equalsIgnoreCase("enclosure")) imageUrl = xmlPullParser.getAttributeValue(XmlPullParser.NO_NAMESPACE, "url");

            if(title != null && link != null && description != null){
                if(isItem){
                    FeedItem item = new FeedItem(title, description, link, imageUrl);
                    System.out.println(imageUrl);
                    feedItems.add(item);
                }

                title = null;
                link = null;
                description = null;
                isItem = false;
            }

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
            FeedAdapter feedAdapter = new FeedAdapter(feedItems);
            recyclerFeeds.setAdapter(feedAdapter);
        }else{
            Toast.makeText(MainActivity.context, "Error while scrapping", Toast.LENGTH_LONG).show();
        }
    }
}
