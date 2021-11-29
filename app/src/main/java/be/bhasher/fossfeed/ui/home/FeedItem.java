package be.bhasher.fossfeed.ui.home;

import android.os.AsyncTask;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import be.bhasher.fossfeed.utils.DateUtils;
import be.bhasher.fossfeed.utils.cache.AppDatabase;

@Entity(tableName = "feeditems")
public class FeedItem implements Serializable{
    @PrimaryKey(autoGenerate = true) public int id;
    @ColumnInfo(name = "title") public String title = null;
    @ColumnInfo(name = "link") public String link = null;
    @ColumnInfo(name = "image_url") public String imageUrl = null;
    @ColumnInfo(name = "date") private String date = null;
    @ColumnInfo(name = "author") public String author = null;
    @ColumnInfo(name = "description") public String description = null;
    @ColumnInfo(name = "guid") public String guid = null;
    @ColumnInfo(name = "timestamp") public long timestamp;
    @ColumnInfo(name = "read") public boolean read = false;
    // TODO implement
    @Ignore public final List<String> categories = new ArrayList<>();
    @Embedded public final FeedChannel feedChannel;
    @Ignore public static int LAST_INDEX = Integer.MIN_VALUE;

    public FeedItem(FeedChannel feedChannel){
        this.feedChannel = feedChannel;
    }

    public void setDate(String date) {
        this.date = date;
        try{
            timestamp = getCalendarDate().getTimeInMillis();
        }catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public String getDate(){
        return this.date;
    }

    public void markAsRead(){
        read = true;
        new MarkRead().execute(this);
    }

    private static class MarkRead extends AsyncTask<FeedItem, Void, Void>{
        @Override
        protected Void doInBackground(FeedItem... feedItems) {
            AppDatabase.itemsDAO.update(feedItems[0]);
            return null;
        }
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
