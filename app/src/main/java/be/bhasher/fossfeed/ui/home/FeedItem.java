package be.bhasher.fossfeed.ui.home;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

import be.bhasher.fossfeed.utils.DateUtils;

public class FeedItem implements Serializable {
    public String title = null;
    public String link = null;
    public String imageUrl = null;
    public String date = null;
    public String author = null;
    public String description = null;
    public String guid = null;
    public final ArrayList<String> categories = new ArrayList<>();
    public final FeedChannel feedChannel;

    public FeedItem(FeedChannel feedChannel){
        this.feedChannel = feedChannel;
    }

    public Calendar getCalendarDate() {
        try {
            return DateUtils.parseDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getDateDiff(){
        try {
            return DateUtils.formatDate(DateUtils.parseDate(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public String getSubtitle(){
        return getDateDiff() + " · " + feedChannel.title;
    }
}
