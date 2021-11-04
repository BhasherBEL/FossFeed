package be.bhasher.fossfeed.ui.home;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "feed")
public class Feed implements Serializable {
    @PrimaryKey private int id;
    @ColumnInfo(name = "title") public String title;
    @ColumnInfo(name = "url") public String url;
    //@ColumnInfo(name = "channels") public ArrayList<FeedChannel> feedChannels;

    public Feed(){}

    public Feed(String title, String url){
        this.title = title;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
