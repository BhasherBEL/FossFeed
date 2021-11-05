package be.bhasher.fossfeed.ui.home.parser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import be.bhasher.fossfeed.ui.home.FeedChannel;
import be.bhasher.fossfeed.ui.home.FeedItem;
import be.bhasher.fossfeed.ui.home.RSSKeywords;

public class RSSParser {
    public static ArrayList<FeedItem> parseFeed(InputStream inputStream) throws XmlPullParserException, IOException {
        ArrayList<FeedItem> feedItems = new ArrayList<>();

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser xmlPullParser = factory.newPullParser();
        Reader reader = new InputStreamReader(inputStream);
        xmlPullParser.setInput(reader);

        FeedChannel feedChannel = new FeedChannel();
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
                    if(inItem) current.setDate(xmlPullParser.nextText().trim());

                }else if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_ITEM_GUID)){
                    if(inItem) current.guid = xmlPullParser.nextText().trim();

                }else if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_CHANNEL_LAST_BUILD_DATE)){
                    if(inChannel) feedChannel.lastBuild = xmlPullParser.nextText().trim();
                }

            }else if(eventType == XmlPullParser.END_TAG){
                if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_ITEM)){
                    inItem = false;
                    feedItems.add(current);
                    feedChannel.add(current);
                    current = new FeedItem(feedChannel);
                }else if(xmlPullParser.getName().equalsIgnoreCase(RSSKeywords.RSS_CHANNEL)){
                    inChannel = false;
                }
            }
            eventType = xmlPullParser.next();
        }

        return feedItems;
    }
}
